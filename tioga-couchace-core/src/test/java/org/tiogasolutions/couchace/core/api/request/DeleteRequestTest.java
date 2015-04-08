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
package org.tiogasolutions.couchace.core.api.request;

import org.tiogasolutions.couchace.core.MockCouchServer;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class DeleteRequestTest {

    private CouchServer server = new MockCouchServer();
    private CouchDatabase database = server.database("unit-tests");
    private RequestExecutor requestExecutor = new RequestExecutor(database);

    @BeforeClass
    public void beforeClass() throws Exception {
        server = new MockCouchServer();
        database = server.database("unit-tests");
        requestExecutor = new RequestExecutor(database);
    }


    public void deleteById() throws Exception {

        DeleteDocumentRequest request = DeleteDocumentRequest.document(requestExecutor, "some-id", "some-revision");
        Assert.assertNotNull(request);

        try {
            DeleteDocumentRequest.document(requestExecutor, null, "some-revision");
            Assert.fail("Expected " + IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Argument \"documentId\" is null.");
        }

        try {
            DeleteDocumentRequest.document(requestExecutor, "some-id", null);
            Assert.fail("Expected " + IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Argument \"documentRevision\" is null.");
        }
    }
}
