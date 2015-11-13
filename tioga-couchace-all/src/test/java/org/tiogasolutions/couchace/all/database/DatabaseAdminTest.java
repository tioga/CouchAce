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

package org.tiogasolutions.couchace.all.database;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.http.CouchHttpException;
import org.tiogasolutions.couchace.core.api.http.CouchHttpQuery;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.api.request.CouchFeature;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.couchace.core.api.response.GetContentResponse;
import org.tiogasolutions.couchace.core.api.response.GetDocumentResponse;
import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jersey.JerseyCouchHttpClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.*;

@Test
public class DatabaseAdminTest {

    private CouchServer couchServer;
    private CouchDatabase database;

    @BeforeClass
    public void setup() {
        CouchSetup couchSetup = new CouchSetup(TestSetup.couchUrl)
                .setUserName(TestSetup.userName)
                .setPassword(TestSetup.password)
                .setHttpClient(JerseyCouchHttpClient.class)
                .setJsonStrategy(JacksonCouchJsonStrategy.class);

        couchServer = new CouchServer(couchSetup);
        CouchFeatureSet featureSet = CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build();
        database = couchServer.database("test-couchace-admin", featureSet);

        // Ensure database does not exist
        if (database.exists()) {
            database.deleteDatabase();
        }
        try {
            database.databaseInfo();
        } catch (CouchException ex) {
            assertEquals(ex.getHttpStatus(), CouchHttpStatus.NOT_FOUND);
        }

        // Recreate the database
        database.recreateDatabase();
        assertTrue(database.exists());

    }

    public void databaseLifeCycleTest() {
        CouchFeatureSet featureSet = CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build();
        CouchDatabase oneTestDatabase = couchServer.database("couchace-one-test", featureSet);
        // Ensure database does not exist
        if (oneTestDatabase.exists()) {
            oneTestDatabase.deleteDatabase();
        }

        // Here we use "raw" database put and delete for the database create and delete
        WriteResponse writeResponse = oneTestDatabase.put().database().execute();
        assertEquals(writeResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Get the database
        GetContentResponse getContentResponse = oneTestDatabase.get().database().execute();
        assertEquals(getContentResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getContentResponse.getContentType(), CouchMediaType.APPLICATION_JSON);

        // Delete the database
        WriteResponse deleteResponse = oneTestDatabase.delete().database().execute();
        assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);

        // Database should no longer exist.
        getContentResponse = oneTestDatabase.get().database().execute();
        assertEquals(getContentResponse.getHttpStatus(), CouchHttpStatus.NOT_FOUND);

    }

    public void compactDatabaseTest() {
        WriteResponse response = database.post().database("_compact").execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.ACCEPTED);
    }

    public void viewCleanupDatabaseTest() {
        WriteResponse response = database.post().database("_view_cleanup").execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.ACCEPTED);
    }

    public void documentChangesTest() {
        CouchHttpQuery httpQuery = CouchHttpQuery.Builder()
                .add("limit", 10)
                .build();
        database.get().database("_changes", httpQuery);

    }

    public void createAndDeleteDocuments() {

        // Create a view
        URL designUrl = getClass().getClassLoader().getResource("design/simple-design.json");
        WriteResponse response = database
                .put()
                .design("simple", designUrl)
                .execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.CREATED);
        GetDocumentResponse documentResponse = database
                .get()
                .document("_design/simple")
                .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);

        // Create a simple document
        String docId = "707";
        response = database
                .put()
                .document(docId, "{\"direction\" : \"NORTH\",\"city\" : \"Toronto\"}")
                .execute();
        assertTrue(response.isCreated(), response.getErrorReason());
        documentResponse = database
                .get()
                .document(docId)
                .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);

        // Get count using built in _all_docs
        documentResponse = database
            .get()
            .document("_all_docs")
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 2);
        for (TextDocument doc : documentResponse.getDocumentList()) {
            assertNotNull(doc.getDocumentId());
            assertNotNull(doc.getDocumentRevision());
        }

        // Get count using all view
        CouchViewQuery viewQuery = CouchViewQuery.builder("simple", "all").build();
        documentResponse = database
                .get()
                .document(viewQuery)
                .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);

        // Delete all non design
        response = database
                .delete()
                .allNonDesigns()
                .execute();
        assertTrue(response.isOk(), response.getErrorReason());

        // Count should be zero using all view
        documentResponse = database
                .get()
                .document(viewQuery)
                .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 0);

        // Count using _all_docs should be one
        documentResponse = database
            .get()
            .document("_all_docs")
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);
        for (TextDocument doc : documentResponse.getDocumentList()) {
            assertNotNull(doc.getDocumentId());
            assertNotNull(doc.getDocumentRevision());
        }

        // Create a simple document again
        docId = "909";
        response = database
            .put()
            .document(docId, "{\"direction\" : \"NORTH\",\"city\" : \"Toronto\"}")
            .execute();
        assertTrue(response.isCreated(), response.getErrorReason());
        documentResponse = database
            .get()
            .document(docId)
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);

        // Count using _all_docs should be two
        documentResponse = database
            .get()
            .document("_all_docs")
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 2);
        for (TextDocument doc : documentResponse.getDocumentList()) {
            assertNotNull(doc.getDocumentId());
            assertNotNull(doc.getDocumentRevision());
        }

        // Delete all design
        response = database
            .delete()
            .allDesigns()
            .execute();
        assertTrue(response.isOk(), response.getErrorReason());

        // Count using _all_docs should be one
        documentResponse = database
            .get()
            .document("_all_docs")
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 1);
        for (TextDocument doc : documentResponse.getDocumentList()) {
            assertNotNull(doc.getDocumentId());
            assertNotNull(doc.getDocumentRevision());
        }

        // Delete all docs
        response = database
            .delete()
            .allDocuments()
            .execute();
        assertTrue(response.isOk(), response.getErrorReason());

        // Count using _all_docs should be one
        documentResponse = database
            .get()
            .document("_all_docs")
            .execute();
        assertTrue(documentResponse.isOk(), documentResponse.getErrorReason());
        assertEquals(documentResponse.getDocumentCount(), 0);
        for (TextDocument doc : documentResponse.getDocumentList()) {
            assertNotNull(doc.getDocumentId());
            assertNotNull(doc.getDocumentRevision());
        }

    }

    public void goodServerTest() {
        CouchSetup couchSetup = new CouchSetup(TestSetup.couchUrl)
            .setUserName(TestSetup.userName)
            .setPassword(TestSetup.password)
            .setHttpClient(JerseyCouchHttpClient.class)
            .setJsonStrategy(JacksonCouchJsonStrategy.class);

        CouchServer couchServer = new CouchServer(couchSetup);

        couchServer.assertConnection();
    }

    public void badServerTest() {
        // Bad port / no connection
        CouchSetup couchSetup = new CouchSetup("http://localhost:9999")
            .setUserName(TestSetup.userName)
            .setPassword(TestSetup.password)
            .setHttpClient(JerseyCouchHttpClient.class)
            .setJsonStrategy(JacksonCouchJsonStrategy.class);
        try {
            new CouchServer(couchSetup).assertConnection();
            fail("Should have thrown CouchHttpException.");
        } catch (CouchHttpException ex) {
            // OK
        }

        // Not a couch http server
        couchSetup = new CouchSetup("http://example.com")
            .setUserName(TestSetup.userName)
            .setPassword(TestSetup.password)
            .setHttpClient(JerseyCouchHttpClient.class)
            .setJsonStrategy(JacksonCouchJsonStrategy.class);
        try {
            new CouchServer(couchSetup).assertConnection();
            fail("Should have thrown CouchHttpException.");
        } catch (CouchHttpException ex) {
            // OK
        }
    }

    public void badCredentialsTest() {
        // Bad credentials
        CouchSetup couchSetup = new CouchSetup(TestSetup.couchUrl)
            .setUserName("junk")
            .setPassword("junk")
            .setHttpClient(JerseyCouchHttpClient.class)
            .setJsonStrategy(JacksonCouchJsonStrategy.class);
        try {
            new CouchServer(couchSetup).assertConnection();
            fail("Should have thrown CouchHttpException.");
        } catch (CouchHttpException ex) {
            // OK
        }
    }
}
