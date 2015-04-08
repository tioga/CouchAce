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
import org.tiogasolutions.couchace.core.api.response.GetEntityResponse;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class GetEntityRequest<T> extends ReadDocumentRequest {
    private final RequestExecutor requestExecutor;
    private final Class<T> entityClass;
    private CouchResponseHandler<GetEntityResponse<T>> onResponse;
    private CouchResponseHandler<GetEntityResponse<T>> onSuccess;
    private CouchResponseHandler<GetEntityResponse<T>> onError;

    public GetEntityRequest(RequestExecutor requestExecutor, Class<T> entityClass, String documentId, String documentRevision) {
        super(documentId, documentRevision);
        ArgUtil.assertNotNull(entityClass, "entityClass");
        this.requestExecutor = requestExecutor;
        this.entityClass = entityClass;
    }

    public GetEntityRequest(RequestExecutor requestExecutor, Class<T> entityClass, CouchPageQuery pageQuery) {
        super(pageQuery);
        ArgUtil.assertNotNull(entityClass, "entityClass");
        this.requestExecutor = requestExecutor;
        this.entityClass = entityClass;
    }


    public GetEntityRequest(RequestExecutor requestExecutor, Class<T> entityClass, CouchViewQuery viewQuery) {
        super(viewQuery);
        ArgUtil.assertNotNull(entityClass, "entityClass");
        this.requestExecutor = requestExecutor;
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public GetEntityResponse<T> execute() {
        return requestExecutor.execute(this);
    }

    public GetEntityRequest<T> onResponse(CouchResponseHandler<GetEntityResponse<T>> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public GetEntityRequest<T> onSuccess(CouchResponseHandler<GetEntityResponse<T>> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public GetEntityRequest<T> onError(CouchResponseHandler<GetEntityResponse<T>> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<GetEntityResponse<T>> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<GetEntityResponse<T>> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<GetEntityResponse<T>> getOnError() {
        return onError;
    }
}
