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
package org.tiogasolutions.couchace.core.api.response;

import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.net.URI;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public abstract class GetResponse implements CouchResponse {
    private final URI uri;
    private final CouchHttpStatus httpStatus;
    private final CouchMediaType contentType;
    private final CouchErrorContent errorContent;

    protected GetResponse(URI uri, CouchHttpStatus httpStatus, CouchMediaType contentType, CouchErrorContent errorContent) {
        ArgUtil.assertNotNull(uri, "URI");
        this.uri = uri;
        this.httpStatus = (httpStatus != null) ? httpStatus : CouchHttpStatus.OK;
        this.contentType = (contentType != null) ? contentType : CouchMediaType.APPLICATION_JSON;
        this.errorContent = (errorContent != null) ? errorContent : CouchErrorContent.noError;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public boolean isSuccess() {
        return httpStatus == CouchHttpStatus.CREATED || httpStatus == CouchHttpStatus.OK;
    }

    @Override
    public boolean isConflict() {
        return httpStatus.isConflict();
    }

    @Override
    public boolean isError() {
        return httpStatus != CouchHttpStatus.CREATED && httpStatus != CouchHttpStatus.OK;
    }

    @Override
    public boolean isCreated() {
        return httpStatus.isCreated();
    }

    @Override
    public boolean isOk() {
        return httpStatus.isOk();
    }

    @Override
    public boolean isUnauthorized() {
        return httpStatus.isUnauthorized();
    }

    @Override
    public boolean isNotFound() {
        return httpStatus.isNotFound();
    }

    @Override
    public CouchMediaType getContentType() {
        return contentType;
    }

    @Override
    public CouchMethodType getMethodType() {
        return CouchMethodType.GET;
    }

    @Override
    public CouchHttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatus.getCode();
    }

    @Override
    public CouchErrorContent getErrorContent() {
        return errorContent;
    }

    @Override
    public String getErrorReason() {
        return errorContent.getReason();
    }

}
