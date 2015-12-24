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

package org.tiogasolutions.couchace.all.entity.person;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.injectable.SimpleCouchInjectables;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.api.response.*;
import org.tiogasolutions.couchace.core.internal.util.IOUtil;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jersey.JerseyCouchHttpClient;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
@Test
public class PersonEntityTest {

    private static final InjectedLogger logger = new InjectedLogger();
    private static final InjectedEventBus eventBus = new InjectedEventBus();

    private static final String documentOneId = "FishFarmer808080";
    private static final String documentTwoId = "ElephantFarmer909090";
    private static final String documentThreeId = "DuckHunter707070";
    private static final AddressEntity addressEntityRef = new AddressEntity("PO Box 111", "", "Oakhurst", "CA", "93601");
    private static final String attachmentHtml = "<html><p>Hello Attachment</p></html>";
    private PersonEntity personEntityOne;
    private PersonEntity personEntityTwo;
    private PersonEntity personEntityThree;

    CouchDatabase couchDatabase;

    @BeforeClass()
    public void setup() {
        // Use much of CouchSetup from TestSetup with the addition of injectables.
        String couchUrl = TestSetup.couchUrl;

        // Configure injectables with logger.
        SimpleCouchInjectables injectables = new SimpleCouchInjectables();
        injectables.addValue(eventBus);
        injectables.addValue("logger", logger);

        // Create the strategy.
        JacksonCouchJsonStrategy jsonStrategy = new JacksonCouchJsonStrategy(new JSR310Module());
        jsonStrategy.setCouchInjectables(injectables);
        CouchSetup couchSetup = new CouchSetup(couchUrl)
                .setUserName(TestSetup.userName)
                .setPassword(TestSetup.password)
                .setHttpClient(JerseyCouchHttpClient.class)
                .setJsonStrategy(jsonStrategy);
        if (couchUrl.startsWith("https")) {
            File moduleDir = IOUtil.findDirNear(IOUtil.currentDir(), "couchace-all");
            File keystoreFile = new File(moduleDir, "src/test/resources/couch-test.jks");
            couchSetup.ssl(keystoreFile.getAbsolutePath(), TestSetup.storePass);
        }

        CouchServer couchServer = new CouchServer(couchSetup);
        couchDatabase = couchServer.database(TestSetup.databaseName);

        byte[] imageBytes = TestSetup.singleton().getImageBytes();
        personEntityOne = new PersonEntity(eventBus, logger, documentOneId, null, "OPEN", "Fish", "Farmer", attachmentHtml, imageBytes, addressEntityRef);
        personEntityTwo = new PersonEntity(eventBus, logger, documentTwoId, null, "OPEN", "Elephant", "Farmer", attachmentHtml, imageBytes, addressEntityRef);
        personEntityThree = new PersonEntity(eventBus, logger, documentThreeId, null, "CLOSED", "Duck", "Hunter", attachmentHtml, imageBytes, addressEntityRef);
    }

     @BeforeMethod()
     public void beforeMethod() {
         HeadResponse headResponse = couchDatabase.head().id(documentOneId).execute();
         if (headResponse.hasDocumentRevision()) {
             // It's there, delete it.
             WriteResponse deleteResponse = couchDatabase.delete().document(documentOneId, headResponse.getDocumentRevision()).execute();
             assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
             assertEquals(couchDatabase.head().id(documentOneId).execute().getHttpStatus(), CouchHttpStatus.NOT_FOUND);
         }

         headResponse = couchDatabase.head().id(documentTwoId).execute();
         if (headResponse.hasDocumentRevision()) {
             // It's there, delete it.
             WriteResponse deleteResponse = couchDatabase.delete().document(documentTwoId, headResponse.getDocumentRevision()).execute();
             assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
             assertEquals(couchDatabase.head().id(documentTwoId).execute().getHttpStatus(), CouchHttpStatus.NOT_FOUND);
         }

         headResponse = couchDatabase.head().id(documentThreeId).execute();
         if (headResponse.hasDocumentRevision()) {
             // It's there, delete it.
             WriteResponse deleteResponse = couchDatabase.delete().document(documentThreeId, headResponse.getDocumentRevision()).execute();
             assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
             assertEquals(couchDatabase.head().id(documentThreeId).execute().getHttpStatus(), CouchHttpStatus.NOT_FOUND);
         }
     }

    public void entityTest() {

        // PUT three entities
        WriteResponse putResponse = couchDatabase.put().entity(personEntityOne).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertEquals(putResponse.getDocumentId(), documentOneId);
        assertNotNull(putResponse.getDocumentRevision());
        String initialVersionOne = putResponse.getDocumentRevision();

        putResponse = couchDatabase.put().entity(personEntityTwo).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertEquals(putResponse.getDocumentId(), documentTwoId);
        assertNotNull(putResponse.getDocumentRevision());
        String initialVersionTwo = putResponse.getDocumentRevision();

        putResponse = couchDatabase.put().entity(personEntityThree).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertEquals(putResponse.getDocumentId(), documentThreeId);
        assertNotNull(putResponse.getDocumentRevision());
        String initialVersionThree = putResponse.getDocumentRevision();

        // Perform a count, ensure we have three.
        CouchViewQuery couchViewQuery = CouchViewQuery
            .builder("entity", "countEntityType")
            .key("person")
            .includeDocs(false)
            .build();

        GetDocumentResponse countResponse = couchDatabase
                .get()
                .document(couchViewQuery)
                .execute();

        assertTrue(countResponse.isOk(), countResponse.getErrorReason());
        assertEquals(countResponse.getFirstContentAsLong(), new Long(3));

        // GET one by id
        GetEntityResponse<PersonEntity> getResponse = couchDatabase
            .get()
            .entity(PersonEntity.class, documentOneId)
            .execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        EntityDocument<PersonEntity> entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity(), personEntityOne);
        assertNotNull(entityDocument.getDocumentRevision());
        assertNotNull(entityDocument.getEntity().getVersion());
        assertEquals(entityDocument.getDocumentRevision(), entityDocument.getEntity().getVersion());
        // Validate that the 3rd-party objects were injected.
        PersonEntity personEntity = entityDocument.getEntity();
        assertSame(personEntity.getLogger(), logger);
        assertSame(personEntity.getEventBus(), eventBus);

        // GET two by id
        getResponse = couchDatabase.get().entity(PersonEntity.class, documentTwoId).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity(), personEntityTwo);

        // GET as a Document
        GetDocumentResponse stringEntityResponse = couchDatabase.get().document(documentTwoId).execute();
        assertEquals(stringEntityResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(stringEntityResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        TextDocument stringEntityDoc = stringEntityResponse.getFirstDocument();
        assertEquals(stringEntityDoc.getDocumentId(), documentTwoId);
        assertEquals(stringEntityDoc.getDocumentRevision(), initialVersionTwo);

        // GET one by key
        CouchViewQuery viewQuery = CouchViewQuery.builder("person", "byName").limit(10).key("Farmer Fish").build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 1);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityOne);

        // GET two by key
        viewQuery = CouchViewQuery.builder("person", "byName").limit(10).key("Farmer Elephant").build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 1);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getDocumentList().get(0);
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityTwo);

        // GET all by name view (sorted by last + first so entity order should be 2, 1, 3)
        viewQuery = CouchViewQuery.builder("person", "byName").limit(100).build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 3);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityTwo);
        assertEquals(entityDocument.getEntity().getId(), documentTwoId);
        entityDocument = getResponse.getDocumentList().get(1);
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityOne);
        assertEquals(entityDocument.getEntity().getId(), documentOneId);
        entityDocument = getResponse.getDocumentList().get(2);
        assertEquals(entityDocument.getDocumentId(), documentThreeId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionThree);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityThree);
        assertEquals(entityDocument.getEntity().getId(), documentThreeId);

        // Now GET all by name DESCENDING ORDER (sorted by last + first so entity order should be 3, 1, 2)
        viewQuery = CouchViewQuery.builder("person", "byName").limit(100).descending(true).build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 3);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentThreeId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionThree);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityThree);
        assertEquals(entityDocument.getEntity().getId(), documentThreeId);
        entityDocument = getResponse.getDocumentList().get(1);
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityOne);
        assertEquals(entityDocument.getEntity().getId(), documentOneId);
        entityDocument = getResponse.getDocumentList().get(2);
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityTwo);
        assertEquals(entityDocument.getEntity().getId(), documentTwoId);

        // GET farmers both by key
        viewQuery = CouchViewQuery.builder("person", "byName").limit(100).start("Farmer").end("FarmerZ").build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 2);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityTwo);
        entityDocument = getResponse.getDocumentList().get(1);
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityOne);

        // GET by status - should just be farmers (one & two)
        viewQuery = CouchViewQuery.builder("person", "byStatus").limit(100).key("OPEN").build();
        getResponse = couchDatabase.get().entity(PersonEntity.class, viewQuery).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(getResponse.getSize(), 2);
        assertEquals(getResponse.getEntityClass(), PersonEntity.class);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentTwoId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionTwo);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityTwo);
        entityDocument = getResponse.getDocumentList().get(1);
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), initialVersionOne);
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity().getClass(), PersonEntity.class);
        assertEquals(entityDocument.getEntity(), personEntityOne);

        // PUT, updating the object
        PersonEntity updatePerson = new PersonEntity(personEntityOne);
        updatePerson.setFirstName("NewFirst");
        putResponse = couchDatabase.put().entity(documentOneId, "person", updatePerson, initialVersionOne).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertEquals(putResponse.getDocumentId(), documentOneId);
        assertFalse(putResponse.getDocumentRevision().equals(initialVersionOne));

        // GET - should see update
        getResponse = couchDatabase.get().entity(PersonEntity.class, documentOneId).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        entityDocument = getResponse.getFirstDocument();
        assertEquals(entityDocument.getDocumentId(), documentOneId);
        assertEquals(entityDocument.getDocumentRevision(), putResponse.getDocumentRevision());
        assertEquals(entityDocument.getEntityType(), "person");
        assertEquals(entityDocument.getEntity(), updatePerson);
        assertFalse(entityDocument.getEntity().equals(personEntityOne));

        // HEAD
        HeadResponse headResponse = couchDatabase.head().id(documentOneId).execute();
        assertEquals(headResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertNotNull(headResponse.getDocumentId());
        assertEquals(headResponse.getDocumentRevision(), putResponse.getDocumentRevision());

        // PUT on existing object without the version - response should be CONFLICT
        updatePerson = new PersonEntity(personEntityOne);
        updatePerson.setFirstName("Junk");
        putResponse = couchDatabase.put().entity(updatePerson).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CONFLICT);
    }

    public void deleteByIdAndRevision() {
        // This case is pretty well covered by beforeClass,
        // but this way this scenario is well documented.

        WriteResponse putResponse = couchDatabase.put().entity(personEntityOne).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        HeadResponse headResponse = couchDatabase.head().id(documentOneId).execute();
        String revision = headResponse.getDocumentRevision();

        WriteResponse deleteResponse = couchDatabase.delete().document(documentOneId, revision).execute();
        assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(couchDatabase.head().id(documentOneId).execute().getHttpStatus(), CouchHttpStatus.NOT_FOUND);
    }

    public void deleteByEntity() {
        WriteResponse putResponse = couchDatabase.put().entity(personEntityOne).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        try {
            // This should fail because the instance personEntityOne does not have a revision set.
            couchDatabase.delete().entity(personEntityOne);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Argument \"revision\" is null.");
        }

        GetEntityResponse<PersonEntity> response = couchDatabase.get().entity(PersonEntity.class, documentOneId).execute();
        PersonEntity personEntity = response.getFirstEntity();

        WriteResponse deleteResponse = couchDatabase.delete().entity(personEntity).execute();
        assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(couchDatabase.head().id(documentOneId).execute().getHttpStatus(), CouchHttpStatus.NOT_FOUND);
    }
}
