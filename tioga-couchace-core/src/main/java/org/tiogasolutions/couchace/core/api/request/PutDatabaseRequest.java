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

/**
 * User: harlan
 * Date: 7/27/14
 * Time: 3:02 PM
 */
public class PutDatabaseRequest implements PutRequest {
    private final RequestExecutor requestExecutor;
    private final String path;
    private final String content;
    private CouchResponseHandler<WriteResponse> onResponse;
    private CouchResponseHandler<WriteResponse> onSuccess;
    private CouchResponseHandler<WriteResponse> onError;

    public PutDatabaseRequest(RequestExecutor requestExecutor, String path, String content) {
        this.requestExecutor = requestExecutor;
        this.path = path;
        this.content = content;
    }

    public WriteResponse execute() {
        return requestExecutor.execute(this);
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public PutDatabaseRequest onResponse(CouchResponseHandler<WriteResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public PutDatabaseRequest onSuccess(CouchResponseHandler<WriteResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public PutDatabaseRequest onError(CouchResponseHandler<WriteResponse> onError) {
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
