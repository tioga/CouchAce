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

import org.tiogasolutions.couchace.core.api.http.CouchHttpQuery;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 2/19/14
 * Time: 9:52 AM
 */
public class GetRequestFactory {

    private final RequestExecutor requestExecutor;

    public GetRequestFactory(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public GetDatabaseRequest database() {
        return new GetDatabaseRequest(requestExecutor, null);
    }

    public GetDatabaseRequest database(String path) {
        return new GetDatabaseRequest(requestExecutor, path);
    }

    public GetDatabaseRequest database(String path, CouchHttpQuery httpQuery) {
        return new GetDatabaseRequest(requestExecutor, path, httpQuery);
    }

    public GetDatabaseRequest database(String path, CouchMediaType contentType) {
        return new GetDatabaseRequest(requestExecutor, path, null, contentType);
    }

    public GetDatabaseRequest database(String path, CouchHttpQuery httpQuery, CouchMediaType contentType) {
        return new GetDatabaseRequest(requestExecutor, path, httpQuery, contentType);
    }

    public GetDocumentRequest document(String documentId) {
        return new GetDocumentRequest(requestExecutor, documentId, null);
    }

    public GetDocumentRequest document(String documentId, String revision) {
        return new GetDocumentRequest(requestExecutor, documentId, revision);
    }

    public GetDocumentRequest document(CouchViewQuery viewQuery) {
        return new GetDocumentRequest(requestExecutor, viewQuery);
    }

    public GetDocumentRequest document(CouchPageQuery pageQuery) {
        return new GetDocumentRequest(requestExecutor, pageQuery);
    }

    public GetAttachmentRequest attachment(String documentId, String attachmentName) {
        return new GetAttachmentRequest(requestExecutor, documentId, null, attachmentName);
    }

    public GetAttachmentRequest attachment(String documentId, String documentRevision, String attachmentName) {
        return new GetAttachmentRequest(requestExecutor, documentId, documentRevision, attachmentName);
    }

    public <T> GetEntityRequest<T> entity(Class<T> entityClass, String documentId) {
        return new GetEntityRequest<>(requestExecutor, entityClass, documentId, null);
    }

    public <T> GetEntityRequest<T> entity(Class<T> entityClass, String documentId, String revision) {
        return new GetEntityRequest<>(requestExecutor, entityClass, documentId, revision);
    }

    public <T> GetEntityRequest<T> entity(Class<T> entityClass, CouchViewQuery viewQuery) {
        return new GetEntityRequest<>(requestExecutor, entityClass, viewQuery);
    }

    public <T> GetEntityRequest<T> entity(Class<T> entityClass, CouchPageQuery pageQuery) {
        return new GetEntityRequest<>(requestExecutor, entityClass, pageQuery);
    }


}
