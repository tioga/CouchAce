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

package org.tiogasolutions.couchace.all.document;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.api.response.GetDocumentResponse;
import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
public class DocumentTest {
    CouchDatabase couchDatabase;
    String docOneId = "DocumentOne";
    String docOneJson = "{\"direction\" : \"NORTH\",\"city\" : \"Toronto\"}";
    String docOneUpdateJson = "{\"direction\" : \"NORTH EAST\",\"city\" : \"Toronto\"}";
    String docOneVersion;
    String docTwoId = "DocumentTwo";
    String docTwoJson = "{\"direction\" : \"SOUTH\",\"city\" : \"Austin\"}";
    String docTwoVersion;
    String docThreeJson = "{\"direction\" : \"WEST\",\"city\" : \"Fresno\"}";
    String docThreeId;
    String docThreeVersion;
    WriteResponse putResponse;

    @BeforeClass()
    public void setup() {
        couchDatabase = TestSetup.couchDatabase();
    }

    @Test
    public void putTest() {

        // Put one
        putResponse = couchDatabase.put().document(docOneId, docOneJson).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putResponse.isCreated());
        assertNotNull(putResponse.getDocumentRevision());
        assertEquals(putResponse.getMethodType(), CouchMethodType.PUT);
        docOneVersion = putResponse.getDocumentRevision();

        // Put two
        putResponse = couchDatabase.put().document(docTwoId, docTwoJson).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putResponse.isCreated());
        assertNotNull(putResponse.getDocumentRevision());
        assertEquals(putResponse.getMethodType(), CouchMethodType.PUT);
        docTwoVersion = putResponse.getDocumentRevision();

        // Put update to document one
        putResponse = couchDatabase.put().document(docOneId, docOneUpdateJson, docOneVersion).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putResponse.isCreated());
        assertNotNull(putResponse.getDocumentRevision());
        assertEquals(putResponse.getMethodType(), CouchMethodType.PUT);
        docOneVersion = putResponse.getDocumentRevision();

        // Get updated doc one
        GetDocumentResponse getResponse = couchDatabase.get().document(docOneId).execute();
        assertEquals(getResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getResponse.getDocumentCount(), 1);
        TextDocument document = getResponse.getFirstDocument();
        assertEquals(document.getDocumentId(), docOneId);
        assertEquals(document.getDocumentRevision(), docOneVersion);
        assertTrue(document.getContent().contains("\"direction\":\"NORTH EAST\",\"city\":\"Toronto\""), document.getContent());

        // Put update to document one, back to original
        putResponse = couchDatabase.put().document(docOneId, docOneJson, docOneVersion).execute();
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putResponse.isCreated());
        assertNotNull(putResponse.getDocumentRevision());
        assertEquals(putResponse.getMethodType(), CouchMethodType.PUT);
        docOneVersion = putResponse.getDocumentRevision();

    }

    @Test
    public void postTest() {

        // Post the document
        WriteResponse response = couchDatabase.post().document(docThreeJson).execute();

        // Verify
        assertNotNull(response.getDocumentId());
        docThreeId = response.getDocumentId();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(response.isCreated());
        assertNotNull(response.getDocumentRevision());
        assertEquals(response.getMethodType(), CouchMethodType.POST);
        docThreeVersion = response.getDocumentRevision();

        // Get and verify
        GetDocumentResponse docResponse = couchDatabase.get().document(docThreeId).execute();
        assertEquals(docResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertTrue(docResponse.isOk());
        assertEquals(docResponse.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(docResponse.getDocumentCount(), 1);
        TextDocument document = docResponse.getFirstDocument();
        assertNotNull(document.getDocumentRevision());
        assertEquals(document.getDocumentRevision(), docThreeVersion);
        assertEquals(document.getContent(), buildFresnoGetResponseContent());

    }

    @Test(dependsOnMethods = {"putTest", "postTest"})
    public void getLatestTest() {

        GetDocumentResponse response = couchDatabase.get().document(docOneId).execute();

        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK);
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(response.getDocumentCount(), 1);
        TextDocument document = response.getFirstDocument();
        assertNotNull(document.getDocumentRevision());
        assertEquals(document.getContent(), buildTorontoGetResponseContent());

    }

    @Test(dependsOnMethods = {"putTest", "postTest"})
    public void getVersionTest() {

        GetDocumentResponse response = couchDatabase.get().document(docOneId, docOneVersion).execute();

        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK);
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.APPLICATION_JSON);
        assertEquals(response.getDocumentCount(), 1);
        TextDocument document = response.getFirstDocument();
        assertNotNull(document.getDocumentRevision());
        assertEquals(document.getContent(), buildTorontoGetResponseContent());

    }

    @Test(dependsOnMethods = {"putTest", "postTest"})
    public void getCitiesViewTest() {

        CouchViewQuery viewQuery = CouchViewQuery.builder("city", "cityName").build();
        GetDocumentResponse response = couchDatabase.get()
            .document(viewQuery)
            .execute();

        // Verify basics of response.
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK);
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.APPLICATION_JSON);

        // Should have three documents
        assertEquals(response.getDocumentCount(), 3);

        // Should be ordered by city name
        List<TextDocument> documents = response.getDocumentList();
        assertEquals(documents.get(0).getDocumentId(), docTwoId);
        assertEquals(documents.get(1).getDocumentId(), docThreeId);
        assertEquals(documents.get(2).getDocumentId(), docOneId);
        for (TextDocument document : documents) {
            assertNotNull(document.getDocumentRevision());
        }

        // Content should be correct
        assertEquals(documents.get(0).getContent().trim(), buildAustinGetResponseContent().trim());
        assertEquals(documents.get(1).getContent().trim(), buildFresnoGetResponseContent().trim());
        assertEquals(documents.get(2).getContent().trim(), buildTorontoGetResponseContent().trim());
    }

    private String buildTorontoGetResponseContent() {
        return String.format("{\"_id\":\"%s\",\"_rev\":\"%s\",\"direction\":\"NORTH\",\"city\":\"Toronto\"}\n", docOneId, docOneVersion);
    }

    private String buildAustinGetResponseContent() {
        return String.format("{\"_id\":\"%s\",\"_rev\":\"%s\",\"direction\":\"SOUTH\",\"city\":\"Austin\"}\n", docTwoId, docTwoVersion);
    }

    private String buildFresnoGetResponseContent() {
        return String.format("{\"_id\":\"%s\",\"_rev\":\"%s\",\"direction\":\"WEST\",\"city\":\"Fresno\"}\n", docThreeId, docThreeVersion);
    }

    private String buildWriteResponseContent(String id, String revision) {
        return String.format("{\"ok\":true,\"id\":\"%s\",\"rev\":\"%s\"}\n", id, revision);
    }

}
