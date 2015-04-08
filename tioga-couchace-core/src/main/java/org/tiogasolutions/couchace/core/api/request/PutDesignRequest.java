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
 * Date: 2/8/14
 * Time: 10:29 AM
 */
public class PutDesignRequest implements PutRequest {
    private final RequestExecutor requestExecutor;
    private final String designName;
    private final String documentRevision;
    private final String designContent;
    private CouchResponseHandler<WriteResponse> onResponse;
    private CouchResponseHandler<WriteResponse> onSuccess;
    private CouchResponseHandler<WriteResponse> onError;

    public PutDesignRequest(RequestExecutor requestExecutor, String designName, String designContent, String documentRevision) {
        this.requestExecutor = requestExecutor;
        this.designName = designName;
        this.designContent = designContent;
        this.documentRevision = documentRevision;
    }

    public String getDesignName() {
        return designName;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public String getDesignContent() {
        return designContent;
    }

    public WriteResponse execute() {
        return requestExecutor.execute(this);
    }

    public PutDesignRequest onResponse(CouchResponseHandler<WriteResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public PutDesignRequest onSuccess(CouchResponseHandler<WriteResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public PutDesignRequest onError(CouchResponseHandler<WriteResponse> onError) {
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
