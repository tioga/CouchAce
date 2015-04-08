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

import org.tiogasolutions.couchace.core.internal.RequestExecutor;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

/**
 * User: harlan
 * Date: 2/20/14
 * Time: 9:52 AM
 */
public class DeleteRequestFactory {

    private final RequestExecutor requestExecutor;

    public DeleteRequestFactory(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public DeleteDocumentRequest document(String documentId, String revision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(revision, "revision");
        return DeleteDocumentRequest.document(requestExecutor, documentId, revision);
    }

    public DeleteDocumentRequest database() {
        return DeleteDocumentRequest.database(requestExecutor);
    }

    public DeleteDocumentRequest allDesigns() {
        return DeleteDocumentRequest.allDesigns(requestExecutor);
    }

    public DeleteDocumentRequest allDocuments() {
        return DeleteDocumentRequest.allDocuments(requestExecutor);
    }

    public DeleteDocumentRequest allNonDesigns() {
        return DeleteDocumentRequest.allNonDesigns(requestExecutor);
    }

    public DeleteAttachmentRequest attachment(String documentId, String revision, String attachmentName) {
        return new DeleteAttachmentRequest(requestExecutor, documentId, revision, attachmentName);
    }

    public DeleteEntityRequest entity(Object entity) {
        return new DeleteEntityRequest(requestExecutor, entity);
    }
}
