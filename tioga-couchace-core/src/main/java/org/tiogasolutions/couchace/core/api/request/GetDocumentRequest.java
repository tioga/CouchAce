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
import org.tiogasolutions.couchace.core.api.response.GetDocumentResponse;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class GetDocumentRequest extends ReadDocumentRequest {

    private final RequestExecutor requestExecutor;
    private CouchResponseHandler<GetDocumentResponse> onResponse;
    private CouchResponseHandler<GetDocumentResponse> onSuccess;
    private CouchResponseHandler<GetDocumentResponse> onError;

    public GetDocumentRequest(RequestExecutor requestExecutor, String documentId, String documentRevision) {
        super(documentId, documentRevision);
        this.requestExecutor = requestExecutor;
    }

    public GetDocumentRequest(RequestExecutor requestExecutor, CouchViewQuery viewQuery) {
        super(viewQuery);
        this.requestExecutor = requestExecutor;
    }

    public GetDocumentRequest(RequestExecutor requestExecutor, CouchPageQuery pageQuery) {
        super(pageQuery);
        this.requestExecutor = requestExecutor;
    }

    public GetDocumentResponse execute() {
        return requestExecutor.execute(this);
    }

    public GetDocumentRequest onResponse(CouchResponseHandler<GetDocumentResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public GetDocumentRequest onSuccess(CouchResponseHandler<GetDocumentResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public GetDocumentRequest onError(CouchResponseHandler<GetDocumentResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<GetDocumentResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<GetDocumentResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<GetDocumentResponse> getOnError() {
        return onError;
    }

}
