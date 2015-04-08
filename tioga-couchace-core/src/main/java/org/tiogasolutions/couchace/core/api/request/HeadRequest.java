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
import org.tiogasolutions.couchace.core.api.response.HeadResponse;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class HeadRequest extends ReadDocumentRequest {

    private final RequestExecutor requestExecutor;
    private CouchResponseHandler<HeadResponse> onResponse;
    private CouchResponseHandler<HeadResponse> onSuccess;
    private CouchResponseHandler<HeadResponse> onError;

    public HeadRequest(RequestExecutor requestExecutor, String documentId, String documentRevision) {
        super(documentId, documentRevision);
        this.requestExecutor = requestExecutor;
    }

    public HeadRequest(RequestExecutor requestExecutor, CouchViewQuery viewQuery) {
        super(viewQuery);
        this.requestExecutor = requestExecutor;
    }

    public HeadRequest(RequestExecutor requestExecutor, CouchPageQuery pageQuery) {
        super(pageQuery);
        this.requestExecutor = requestExecutor;
    }

    public HeadResponse execute() {
        return requestExecutor.execute(this);
    }

    public HeadRequest onResponse(CouchResponseHandler<HeadResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public HeadRequest onSuccess(CouchResponseHandler<HeadResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public HeadRequest onError(CouchResponseHandler<HeadResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<HeadResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<HeadResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<HeadResponse> getOnError() {
        return onError;
    }


}
