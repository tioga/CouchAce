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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;
import org.tiogasolutions.couchace.core.internal.util.IOUtil;

import java.net.URL;
import java.nio.file.Path;

/**
 * User: harlan
 * Date: 2/19/14
 * Time: 9:52 AM
 */
public class PutRequestFactory {

    private final RequestExecutor requestExecutor;

    public PutRequestFactory(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    /**
     * Creates or updates an entity based upon its annotated values.
     * @param entity -
     * @return PutEntityRequest
     */
    public PutEntityRequest entity(Object entity) {
        ArgUtil.assertNotNull(entity, "entity");
        return new PutEntityRequest(requestExecutor, null, null, entity, null, null);
    }

    /**
     * Creates an document for the given entity with the specified documentId and entityType
     * @param documentId -
     * @param entityType -
     * @param entity -
     * @return PutEntityRequest
     */
    public PutEntityRequest entity(String documentId,
                                   String entityType,
                                   Object entity) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, null, null);
    }

    /**
     * Updates a document for the given entity with the specified documentId and entityType
     * @param documentId -
     * @param entityType -
     * @param entity  -
     * @param documentRevision -
     * @return PutEntityRequest
     */
    public PutEntityRequest entity(String documentId,
                                   String entityType,
                                   Object entity,
                                   String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, documentRevision, null);
    }

    /**
     * Creates an document for the given entity with the specified documentId and entityType
     * @param documentId -
     * @param entityType -
     * @param entity -
     * @param attachmentInfoMap -
     * @return PutEntityRequest
     */
    public PutEntityRequest entity(String documentId,
                                   String entityType,
                                   Object entity,
                                   CouchAttachmentInfoMap attachmentInfoMap) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, null, attachmentInfoMap);
    }

    /**
     * Updates a document for the given entity with the specified documentId and entityType
     * @param documentId -
     * @param entityType -
     * @param entity  -
     * @param documentRevision -
     * @param attachmentInfoMap -
     * @return PutEntityRequest
     */
    public PutEntityRequest entity(String documentId,
                                   String entityType,
                                   Object entity,
                                   String documentRevision,
                                   CouchAttachmentInfoMap attachmentInfoMap) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, documentRevision, attachmentInfoMap);
    }


    /**
     * Updates or creates a document for the given entity with the specified documentId and entityType
     * @param documentId  -
     * @param entityType -
     * @param entity -
     * @param documentRevision -
     * @return PutEntityRequest
     */
    public PutEntityRequest entityCreateOrUpdate(String documentId,
                                   String entityType,
                                   Object entity,
                                   String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, documentRevision, null);
    }

    /**
     * Updates or creates a document for the given entity with the specified documentId and entityType
     * @param documentId  -
     * @param entityType -
     * @param entity -
     * @param documentRevision -
     * @param attachmentInfoMap -
     * @return PutEntityRequest
     */
    public PutEntityRequest entityCreateOrUpdate(String documentId,
                                                 String entityType,
                                                 Object entity,
                                                 String documentRevision,
                                                 CouchAttachmentInfoMap attachmentInfoMap) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");
        return new PutEntityRequest(requestExecutor, documentId, entityType, entity, documentRevision, attachmentInfoMap);
    }

    public PutDocumentRequest document(String documentId, String document) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(document, "document");
        return new PutDocumentRequest(requestExecutor, documentId, document, null);
    }

    public PutDocumentRequest document(String documentId, String document, String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(document, "document");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        return new PutDocumentRequest(requestExecutor, documentId, document, documentRevision);
    }

    public PutDocumentRequest documentCreateOrUpdate(String documentId, String document, String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(document, "document");
        return new PutDocumentRequest(requestExecutor, documentId, document, documentRevision);
    }

    public PutAttachmentRequest attachment(String documentId, String documentRevision, String attachmentName, CouchMediaType contentType, Object content) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        ArgUtil.assertNotNull(attachmentName, "attachmentName");
        ArgUtil.assertNotNull(contentType, "contentType");
        ArgUtil.assertNotNull(content, "content");
        return new PutAttachmentRequest(requestExecutor, documentId, documentRevision, attachmentName, contentType, content);
    }

    public PutDatabaseRequest database(String path, String content) {
        ArgUtil.assertNotNull(path, "path");
        ArgUtil.assertNotNull(content, "content");
        return new PutDatabaseRequest(requestExecutor, path, content);
    }

    public PutDatabaseRequest database(String path) {
        ArgUtil.assertNotNull(path, "path");
        return new PutDatabaseRequest(requestExecutor, path, null);
    }

    public PutDatabaseRequest database() {
        return new PutDatabaseRequest(requestExecutor, null, null);
    }

    public PutDesignRequest design(String designName, String designContent) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designContent, "designContent");
        return new PutDesignRequest(requestExecutor, designName, designContent, null);
    }

    public PutDesignRequest design(String designName, Path designFile) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designFile, "designFile");
        String designBody = IOUtil.readText(designFile);
        return new PutDesignRequest(requestExecutor, designName, designBody, null);
    }

    public PutDesignRequest design(String designName, URL designFile) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designFile, "designFile");
        String designBody = IOUtil.readText(designFile);
        return new PutDesignRequest(requestExecutor, designName, designBody, null);
    }

    public PutDesignRequest design(String designName, String designContent, String documentRevision) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designContent, "designContent");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        return new PutDesignRequest(requestExecutor, designName, designContent, documentRevision);
    }

    public PutDesignRequest design(String designName, Path designFile, String documentRevision) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designFile, "designContent");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        String designBody = IOUtil.readText(designFile);
        return new PutDesignRequest(requestExecutor, designName, designBody, documentRevision);
    }

    public PutDesignRequest design(String designName, URL designFile, String documentRevision) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(designFile, "designFile");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        String designBody = IOUtil.readText(designFile);
        return new PutDesignRequest(requestExecutor, designName, designBody, documentRevision);
    }

}
