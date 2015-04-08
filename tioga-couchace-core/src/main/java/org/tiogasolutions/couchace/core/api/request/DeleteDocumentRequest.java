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

import org.tiogasolutions.couchace.core.api.response.CouchResponseHandler;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class DeleteDocumentRequest implements DeleteRequest {

    public enum Type {DATABASE, DOCUMENT, ALL_DESIGNS, ALL_DOCUMENTS, ALL_NON_DESIGNS}
    private final RequestExecutor requestExecutor;
    private final Type type;
    private final String documentId;
    private final String documentRevision;
    private CouchResponseHandler<WriteResponse> onResponse;
    private CouchResponseHandler<WriteResponse> onSuccess;
    private CouchResponseHandler<WriteResponse> onError;

    public static DeleteDocumentRequest document(RequestExecutor requestExecutor, String documentId, String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        ArgUtil.assertNotNull(documentRevision, "documentRevision");
        return new DeleteDocumentRequest(requestExecutor, Type.DOCUMENT, documentId, documentRevision);
    }

    public static DeleteDocumentRequest database(RequestExecutor requestExecutor) {
        return new DeleteDocumentRequest(requestExecutor, Type.DATABASE, null, null);
    }

    public static DeleteDocumentRequest allDesigns(RequestExecutor requestExecutor) {
        return new DeleteDocumentRequest(requestExecutor, Type.ALL_DESIGNS, null, null);
    }

    public static DeleteDocumentRequest allDocuments(RequestExecutor requestExecutor) {
        return new DeleteDocumentRequest(requestExecutor, Type.ALL_DOCUMENTS, null, null);
    }

    public static DeleteDocumentRequest allNonDesigns(RequestExecutor requestExecutor) {
        return new DeleteDocumentRequest(requestExecutor, Type.ALL_NON_DESIGNS, null, null);
    }

    public DeleteDocumentRequest(RequestExecutor requestExecutor, Type type, String documentId, String documentRevision) {
        this.requestExecutor = requestExecutor;
        this.type = type;
        this.documentId = documentId;
        this.documentRevision = documentRevision;
    }

    public Type getType() {
        return type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public boolean hasRevision() {
        return documentRevision != null;
    }

    public WriteResponse execute() {
        return requestExecutor.execute(this);
    }

    public DeleteDocumentRequest onResponse(CouchResponseHandler<WriteResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public DeleteDocumentRequest onSuccess(CouchResponseHandler<WriteResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public DeleteDocumentRequest onError(CouchResponseHandler<WriteResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<WriteResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<WriteResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<WriteResponse> getOnError() {
        return onError;
    }

}
