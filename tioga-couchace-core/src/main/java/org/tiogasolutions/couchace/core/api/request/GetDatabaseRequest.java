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
import org.tiogasolutions.couchace.core.api.response.CouchResponseHandler;
import org.tiogasolutions.couchace.core.api.response.GetContentResponse;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class GetDatabaseRequest implements ReadRequest {

    private final RequestExecutor requestExecutor;
    private final String path;
    private final CouchHttpQuery httpQuery;
    private final CouchMediaType contentType;
    private CouchResponseHandler<GetContentResponse> onResponse;
    private CouchResponseHandler<GetContentResponse> onSuccess;
    private CouchResponseHandler<GetContentResponse> onError;

    public GetDatabaseRequest(RequestExecutor requestExecutor, String path) {
        this(requestExecutor, path, null, null);
    }

    public GetDatabaseRequest(RequestExecutor requestExecutor, String path, CouchHttpQuery httpQuery) {
        this(requestExecutor, path, httpQuery, null);
    }

    public GetDatabaseRequest(RequestExecutor requestExecutor, String path, CouchHttpQuery httpQuery, CouchMediaType contentType) {
        this.requestExecutor = requestExecutor;
        this.path = path;
        this.httpQuery = (httpQuery == null) ? CouchHttpQuery.EMPTY : httpQuery;
        this.contentType = (contentType == null) ? CouchMediaType.APPLICATION_JSON : contentType;
    }

    public GetContentResponse execute() {
        return requestExecutor.execute(this);
    }

    public String getPath() {
        return path;
    }

    public CouchHttpQuery getHttpQuery() {
        return httpQuery;
    }

    public CouchMediaType getContentType() {
        return contentType;
    }

    public GetDatabaseRequest onResponse(CouchResponseHandler<GetContentResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public GetDatabaseRequest onSuccess(CouchResponseHandler<GetContentResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public GetDatabaseRequest onError(CouchResponseHandler<GetContentResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<GetContentResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<GetContentResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<GetContentResponse> getOnError() {
        return onError;
    }

}
