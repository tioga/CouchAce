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

package org.tiogasolutions.couchace.jackson.entity;

import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.injectable.MissingInjectableResponse;
import org.tiogasolutions.couchace.core.api.injectable.SimpleCouchInjectables;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.tiogasolutions.couchace.core.api.request.GetAttachmentRequest;
import org.tiogasolutions.couchace.core.api.request.GetRequestFactory;
import org.tiogasolutions.couchace.core.api.response.EntityDocument;
import org.tiogasolutions.couchace.core.api.response.GetAttachmentResponse;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.JacksonTestSetup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class ReadEntityTest {

    private final JacksonCouchJsonStrategy strategy = JacksonTestSetup.strategy;
    private final CouchMetaRepository couchMetaRepository = new CouchMetaRepository();
    private GetRequestFactory getRequestFactory;

    @BeforeClass
    public void beforeClass() throws Exception {

      // All of this tom-foolerly is simply because @CouchAttachment was added to LocationEntity for the
      // sake of the write test. Here we just want to fake it out so that this part of the test doesn't fail.
      final GetAttachmentResponse response = new GetAttachmentResponse(URI.create("http://whatever.com"), CouchHttpStatus.OK, "x", "x", CouchMediaType.TEXT_PLAIN, "stuff", null);
      getRequestFactory = new GetRequestFactory(null) {
        @Override public GetAttachmentRequest attachment(String documentId, String documentRevision, String attachmentName) {
          return new GetAttachmentRequest(null, documentId, documentRevision, attachmentName) {
            @Override public GetAttachmentResponse execute() {
              return response;
            }
          };
        }
      };
    }

    public void entityDocumentTest() {
        CouchEntityMeta<LocationEntity> entityMeta = couchMetaRepository.getOrCreateEntityMeta(LocationEntity.class);
        EntityDocument<LocationEntity> entityDoc = strategy.readEntityDocument(getRequestFactory, entityMeta, JacksonTestSetup.locationEntityDocJson);

        assertEquals(entityDoc.getDocumentId(), "1122");
        assertEquals(entityDoc.getEntityType(), "Location");
        assertEquals(entityDoc.getDocumentRevision(), "1-03ba856139647f1ed4694ceb1fe21751");
        LocationEntity location = entityDoc.getEntity();
        assertNotNull(location);
        assertEquals(location.getCity(), "Toronto");
        assertEquals(location.getDirection(), "NORTH");
    }

    public void entityDocumentWithInjectableTest() {
        CouchEntityMeta<LocationInjectedEntity> entityMeta = couchMetaRepository.getOrCreateEntityMeta(LocationInjectedEntity.class);

        JacksonCouchJsonStrategy strategy = new JacksonCouchJsonStrategy();
        CouchSetup couchSetup = new CouchSetup("http:\\something")
                .setJsonStrategy(strategy);

        strategy.setMissingInjectableResponse(MissingInjectableResponse.RETURN_NULL);
        SimpleCouchInjectables injectables = new SimpleCouchInjectables();
        injectables.addValue("injected", "injectedValue");
        strategy.setCouchInjectables(injectables);
        EntityDocument<LocationInjectedEntity> entityDoc = strategy.readEntityDocument(null, entityMeta, JacksonTestSetup.locationEntityDocJson);

        assertEquals(entityDoc.getDocumentId(), "1122");
        assertEquals(entityDoc.getEntityType(), "Location");
        assertEquals(entityDoc.getDocumentRevision(), "1-03ba856139647f1ed4694ceb1fe21751");
        LocationInjectedEntity location = entityDoc.getEntity();
        assertNotNull(location);
        assertEquals(location.getCity(), "Toronto");
        assertEquals(location.getDirection(), "NORTH");
        assertEquals(location.getInjected(), "injectedValue");
    }

    public void viewResultsWithDocsTest() {
        CouchEntityMeta<LocationEntity> entityMeta = couchMetaRepository.getOrCreateEntityMeta(LocationEntity.class);
        List<EntityDocument<LocationEntity>> entityDocuments = strategy.readEntityDocuments(getRequestFactory, entityMeta, JacksonTestSetup.viewResultsWithDocsJson);
        assertEquals(entityDocuments.size(), 2);

        EntityDocument<LocationEntity> entityDoc = entityDocuments.get(0);
        assertEquals(entityDoc.getDocumentId(), "1122");
        assertEquals(entityDoc.getEntityType(), "Location");
        assertEquals(entityDoc.getDocumentRevision(), "1-03ba856139647f1ed4694ceb1fe21751");
        LocationEntity location = entityDoc.getEntity();
        assertNotNull(location);
        assertEquals(location.getCity(), "Toronto");
        assertEquals(location.getDirection(), "NORTH");

        entityDoc = entityDocuments.get(1);
        location = entityDoc.getEntity();
        assertNotNull(location);
        assertEquals(entityDoc.getEntityType(), "Location");
        assertEquals(entityDoc.getDocumentId(), "3344");
        assertEquals(entityDoc.getDocumentRevision(), "1-be5026e8a8aec25fb4eb67f8047460b3");
        assertEquals(location.getCity(), "Austin");
        assertEquals(location.getDirection(), "SOUTH");
    }

}
