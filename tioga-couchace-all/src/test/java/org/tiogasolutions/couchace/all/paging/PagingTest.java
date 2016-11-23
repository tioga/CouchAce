/*
 * Copyright 2012 Harlan Noonkester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tiogasolutions.couchace.all.paging;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.response.GetEntityResponse;
import org.tiogasolutions.couchace.core.api.query.CouchJsonKey;
import org.tiogasolutions.couchace.core.api.query.CouchPageNavigation;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
public class PagingTest {

    CouchDatabase couchDatabase;

    PetEntity[] pets;

    @BeforeClass()
    public void setup() {
        couchDatabase = TestSetup.couchDatabase();

        // PUT 10 pet entities for paging
        pets = new PetEntity[]{
            PetEntity.newPet("One", PetEntity.PetType.CAT),
            PetEntity.newPet("Two", PetEntity.PetType.CAT),
            PetEntity.newPet("Three", PetEntity.PetType.DOG),
            PetEntity.newPet("Four", PetEntity.PetType.DOG),
            PetEntity.newPet("Five", PetEntity.PetType.DOG),
            PetEntity.newPet("Six", PetEntity.PetType.DOG),
            PetEntity.newPet("Seven", PetEntity.PetType.DOG),
            PetEntity.newPet("Eight", PetEntity.PetType.FISH),
            PetEntity.newPet("Nine", PetEntity.PetType.FISH),
            PetEntity.newPet("Ten", PetEntity.PetType.FISH)
        };
        for (PetEntity pet : pets) {
            String docId = String.format("Pet:%s", pet.getId());
            couchDatabase.put().entity(docId, "Pet", pet).execute();
        }

    }

    @Test()
    public void byIdTest() {

        // First page
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byId").limit(3).key(2).build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 1);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[1]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
    }

    @Test()
    public void byTypePageTest() {

        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byType").limit(2).key("DOG").build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 2);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[2]);
        assertEquals(foundPets.get(1), pets[3]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        // Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 2);
        foundPets = er.getEntityList();
        pageNavigation = er.getCouchPageNavigation();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A5&skip=1&descending=true&key=%22DOG%22");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A6&skip=1&descending=false&key=%22DOG%22");

        // Third page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        pageNavigation = er.getCouchPageNavigation();
        foundPets = er.getEntityList();
        assertEquals(er.getSize(), 1);
        assertEquals(foundPets.get(0), pets[6]);
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A7&skip=1&descending=true&key=%22DOG%22");

        // Previous to Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 2);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        // Previous to first page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 2);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[2]);
        assertEquals(foundPets.get(1), pets[3]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
    }

    @Test()
    public void byTypeUsingOffsetPageTest() {
        int skip = 0;
        int pageSize = 2;
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byType").limit(pageSize + 1).skip(skip).key("DOG").build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), pageSize + 1);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[2]);
        assertEquals(foundPets.get(1), pets[3]);

        // Second page
        skip += pageSize;
        viewQuery = CouchViewQuery.builder("pet", "byType").limit(pageSize + 1).skip(skip).key("DOG").build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), pageSize + 1);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);

        // Third page
        skip += pageSize;
        viewQuery = CouchViewQuery.builder("pet", "byType").limit(pageSize + 1).skip(skip).key("DOG").build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        foundPets = er.getEntityList();
        assertEquals(er.getSize(), 1);
        assertEquals(foundPets.get(0), pets[6]);

        // Previous to Second page
        skip -= pageSize;
        viewQuery = CouchViewQuery.builder("pet", "byType").limit(pageSize + 1).skip(skip).key("DOG").build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), pageSize + 1);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);

        // Previous to first page
        skip -= pageSize;
        viewQuery = CouchViewQuery.builder("pet", "byType").limit(pageSize + 1).skip(skip).key("DOG").build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), pageSize + 1);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[2]);
        assertEquals(foundPets.get(1), pets[3]);
    }

    @Test()
    public void byTypeStartEndPageTest() {

        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byType").limit(3).start("CAT").end("DOG").build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 3);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[0]);
        assertEquals(foundPets.get(1), pets[1]);
        assertEquals(foundPets.get(2), pets[2]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A3&skip=1&descending=false&endkey=%22DOG%22");

        // Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        pageNavigation = er.getCouchPageNavigation();
        assertEquals(foundPets.get(0), pets[3]);
        assertEquals(foundPets.get(1), pets[4]);
        assertEquals(foundPets.get(2), pets[5]);
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A4&skip=1&descending=true&endkey=%22DOG%22");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A6&skip=1&descending=false&endkey=%22DOG%22");

        // Third page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        pageNavigation = er.getCouchPageNavigation();
        foundPets = er.getEntityList();
        assertEquals(er.getSize(), 1);
        assertEquals(foundPets.get(0), pets[6]);
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet%3A7&skip=1&descending=true&endkey=%22DOG%22");
//    assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet:7&skip=1&descending=true&key=%22DOG%22")

        // Previous to Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[3]);
        assertEquals(foundPets.get(1), pets[4]);
        assertEquals(foundPets.get(2), pets[5]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        // CRITICAL - problem is that startKey has been changed and does not include the original value.

/*
    assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet:7&skip=1&descending=true&endkey=%22DOG%22")
    assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byType?startkey=%22DOG%22&startkey_docid=Pet:7&skip=1&descending=true&key=%22DOG%22")

    // Previous to first page
    getRequest = GetEntityRequest.byPage(PetEntity.class, pageNavigation.queryPreviousPage())
    er = couchStrategy.getEntity(getRequest)
    assertEquals(er.getEntityCount(), 3)
    foundPets = er.getEntityList()
    assertEquals(foundPets[0], pets[0])
    assertEquals(foundPets[1], pets[1])
    assertEquals(foundPets[2], pets[2])
    pageNavigation = er.getCouchPageNavigation()
    assertNotNull(pageNavigation)
    assertFalse(pageNavigation.hasPreviousPage())
    assertTrue(pageNavigation.hasNextPage())
    assertTrue(pageNavigation.hasRequestedPage())
*/
    }

    @Test()
    public void byTypeAndIdTest() {

//    http://127.0.0.1:5984/couch-test/_design/pet/_view/byTypeAndId?key=[%22CAT%22,%202]
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byTypeAndId").limit(3).key("CAT", 2).build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 1);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[1]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        // http://127.0.0.1:5984/couch-test/_design/pet/_view/byTypeAndId?startkey=[%22DOG%22,%205]
        viewQuery = CouchViewQuery.builder("pet", "byTypeAndId").limit(10).start("DOG", 5).build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 6);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);
        assertEquals(foundPets.get(2), pets[6]);
        assertEquals(foundPets.get(3), pets[7]);
        assertEquals(foundPets.get(4), pets[8]);
        assertEquals(foundPets.get(5), pets[9]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        // http://127.0.0.1:5984/couch-test/_design/pet/_view/byTypeAndId?startkey=[%22DOG%22,%205]&endkey=[%22DOG%22,%207]
        viewQuery = CouchViewQuery.builder("pet", "byTypeAndId").limit(10).start(new CouchJsonKey("DOG", 5)).end(new CouchJsonKey("DOG", 7)).build();
        er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        pageNavigation = er.getCouchPageNavigation();
//    assertEquals(pageNavigation.requestedPage, "_design/pet/_view/byTypeAndId?include_docs=true&startkey=%5B%[%22DOG%22,%205]&endkey=%5B%[%22DOG%22,%207]&limit=10")
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[4]);
        assertEquals(foundPets.get(1), pets[5]);
        assertEquals(foundPets.get(2), pets[6]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

    }

    @Test()
    public void pageNextByIdTest() {

        // First page
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byId").limit(3).build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 3);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[0]);
        assertEquals(foundPets.get(1), pets[1]);
        assertEquals(foundPets.get(2), pets[2]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?include_docs=true&limit=4");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=3&startkey_docid=Pet%3A3&skip=1&descending=false");

        // Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[3]);
        assertEquals(foundPets.get(1), pets[4]);
        assertEquals(foundPets.get(2), pets[5]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=3&startkey_docid=Pet%3A3&skip=1&descending=false");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=4&startkey_docid=Pet%3A4&skip=1&descending=true");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=6&startkey_docid=Pet%3A6&skip=1&descending=false");

        // Third page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[6]);
        assertEquals(foundPets.get(1), pets[7]);
        assertEquals(foundPets.get(2), pets[8]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=6&startkey_docid=Pet%3A6&skip=1&descending=false");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=7&startkey_docid=Pet%3A7&skip=1&descending=true");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=9&startkey_docid=Pet%3A9&skip=1&descending=false");

        // Fourth page

        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 1);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[9]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=9&startkey_docid=Pet%3A9&skip=1&descending=false");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=10&startkey_docid=Pet%3A10&skip=1&descending=true");

    }

    @Test()
    public void pageNextByCreatedAtTest() {

        // First page
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byCreatedAt").limit(3).build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        assertEquals(er.getSize(), 3);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[0]);
        assertEquals(foundPets.get(1), pets[1]);
        assertEquals(foundPets.get(2), pets[2]);
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byCreatedAt?include_docs=true&limit=4");

        String actualNextPage = pageNavigation.getNextPage();
        String expectedNextPage = "_design/pet/_view/byCreatedAt?startkey=%22";
        String createdAt = foundPets.get(2).getCreatedAt().toString();
        createdAt = createdAt.replace(":", "%3A");
        expectedNextPage += createdAt;
        expectedNextPage += "%22&startkey_docid=Pet%3A3&skip=1&descending=false";
        assertEquals(actualNextPage, expectedNextPage);

        // Second page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[3]);
        assertEquals(foundPets.get(1), pets[4]);
        assertEquals(foundPets.get(2), pets[5]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), expectedNextPage);

        createdAt = foundPets.get(0).getCreatedAt().toString().replace(":", "%3A");
        String previousPage = String.format("_design/pet/_view/byCreatedAt?startkey=%%22%s%%22&startkey_docid=Pet%%3A4&skip=1&descending=true", createdAt);
        assertEquals(pageNavigation.getPreviousPage(), previousPage);

        createdAt = foundPets.get(2).getCreatedAt().toString().replace(":", "%3A");
        expectedNextPage = String.format("_design/pet/_view/byCreatedAt?startkey=%%22%s%%22&startkey_docid=Pet%%3A6&skip=1&descending=false", createdAt);
        assertEquals(pageNavigation.getNextPage(), expectedNextPage);

        // Third page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[6]);
        assertEquals(foundPets.get(1), pets[7]);
        assertEquals(foundPets.get(2), pets[8]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), expectedNextPage);

        createdAt = foundPets.get(0).getCreatedAt().toString().replace(":", "%3A");
        previousPage = String.format("_design/pet/_view/byCreatedAt?startkey=%%22%s%%22&startkey_docid=Pet%%3A7&skip=1&descending=true", createdAt);
        assertEquals(pageNavigation.getPreviousPage(), previousPage);

        createdAt = foundPets.get(2).getCreatedAt().toString().replace(":", "%3A");
        expectedNextPage = String.format("_design/pet/_view/byCreatedAt?startkey=%%22%s%%22&startkey_docid=Pet%%3A9&skip=1&descending=false", createdAt);
        assertEquals(pageNavigation.getNextPage(), expectedNextPage);

        // Fourth page
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
        assertEquals(er.getSize(), 1);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[9]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());

        createdAt = foundPets.get(0).getCreatedAt().toString().replace(":", "%3A");
        previousPage = String.format("_design/pet/_view/byCreatedAt?startkey=%%22%s%%22&startkey_docid=Pet%%3A10&skip=1&descending=true", createdAt);
        assertEquals(pageNavigation.getPreviousPage(), previousPage);

    }

    @Test()
    public void pagePreviousByIdTest() {

        // Skip to last (page 4)
        CouchViewQuery viewQuery = CouchViewQuery.builder("pet", "byId").limit(3).build();
        GetEntityResponse<PetEntity> er = couchDatabase.get().entity(PetEntity.class, viewQuery).execute();
        CouchPageNavigation pageNavigation = er.getCouchPageNavigation();
        while (pageNavigation.hasNextPage()) {
            er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryNextPage()).execute();
            pageNavigation = er.getCouchPageNavigation();
        }
        assertEquals(er.getSize(), 1);
        List<PetEntity> foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[9]);
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertFalse(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=9&startkey_docid=Pet%3A9&skip=1&descending=false");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=10&startkey_docid=Pet%3A10&skip=1&descending=true");

        // Previous (page 3)

        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[6]);
        assertEquals(foundPets.get(1), pets[7]);
        assertEquals(foundPets.get(2), pets[8]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=10&startkey_docid=Pet%3A10&skip=1&descending=true");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=7&startkey_docid=Pet%3A7&skip=1&descending=true");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=9&startkey_docid=Pet%3A9&skip=1&descending=false");

        // Previous (page 2)
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[3]);
        assertEquals(foundPets.get(1), pets[4]);
        assertEquals(foundPets.get(2), pets[5]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertTrue(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=7&startkey_docid=Pet%3A7&skip=1&descending=true");
        assertEquals(pageNavigation.getPreviousPage(), "_design/pet/_view/byId?startkey=4&startkey_docid=Pet%3A4&skip=1&descending=true");
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=6&startkey_docid=Pet%3A6&skip=1&descending=false");

        // Previous (page 1)
        er = couchDatabase.get().entity(PetEntity.class, pageNavigation.queryPreviousPage()).execute();
        assertEquals(er.getSize(), 3);
        foundPets = er.getEntityList();
        assertEquals(foundPets.get(0), pets[0]);
        assertEquals(foundPets.get(1), pets[1]);
        assertEquals(foundPets.get(2), pets[2]);
        pageNavigation = er.getCouchPageNavigation();
        assertNotNull(pageNavigation);
        assertFalse(pageNavigation.hasPreviousPage());
        assertTrue(pageNavigation.hasNextPage());
        assertTrue(pageNavigation.hasRequestedPage());
        assertEquals(pageNavigation.getRequestedPage(), "_design/pet/_view/byId?startkey=4&startkey_docid=Pet%3A4&skip=1&descending=true");
        assertNull(pageNavigation.getPreviousPage());
        assertEquals(pageNavigation.getNextPage(), "_design/pet/_view/byId?startkey=3&startkey_docid=Pet%3A3&skip=1&descending=false");
    }

}
