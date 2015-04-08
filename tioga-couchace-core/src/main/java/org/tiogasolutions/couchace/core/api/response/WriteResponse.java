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
import org.tiogasolutions.couchace.core.spi.http.CouchHttpResponse;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.net.URI;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public class WriteResponse implements CouchResponse {
    private final URI uri;
    private final CouchHttpStatus httpStatus;
    private final CouchMethodType methodType;
    private final CouchMediaType contentType;
    private final String documentId;
    private final String documentRevision;
    private final String content;

    public WriteResponse(CouchHttpResponse couchHttpResponse) {
        this.uri = couchHttpResponse.getUri();
        this.methodType = couchHttpResponse.getMethodType();
        this.httpStatus = couchHttpResponse.getHttpStatus();
        this.contentType = couchHttpResponse.getContentType();
        this.documentId = couchHttpResponse.getDocumentId();
        this.documentRevision = couchHttpResponse.getEtag();
        this.content = couchHttpResponse.getStringContent();
    }

    public WriteResponse(CouchMethodType methodType, URI uri, CouchHttpStatus httpStatus, CouchMediaType contentType, String documentId, String documentRevision, String content) {
        ArgUtil.assertNotNull(methodType, "HttpMethodType");
        ArgUtil.assertNotNull(uri, "URI");
        ArgUtil.assertNotNull(httpStatus, "HttpStatus");
        this.uri = uri;
        this.methodType = methodType;
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.documentId = documentId;
        this.documentRevision = documentRevision;
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public boolean hasDocumentRevision() {
        return documentRevision != null;
    }

    public String getContent() {
        return content;
    }

    @Override
    public CouchMethodType getMethodType() {
        return methodType;
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
    public URI getUri() {
        return uri;
    }

    @Override
    public CouchMediaType getContentType() {
        return contentType;
    }

    @Override
    public boolean isOk() {
        return httpStatus.isOk();
    }

    @Override
    public boolean isNotFound() {
        return httpStatus.isNotFound();
    }

    @Override
    public boolean isCreated() {
        return httpStatus.isCreated();
    }

    @Override
    public boolean isConflict() {
        return httpStatus.isConflict();
    }

    @Override
    public boolean isUnauthorized() {
        return httpStatus.isUnauthorized();
    }

    @Override
    public boolean isSuccess() {
        return httpStatus == CouchHttpStatus.CREATED || httpStatus == CouchHttpStatus.OK;
    }

    @Override
    public boolean isError() {
        return httpStatus != CouchHttpStatus.CREATED && httpStatus != CouchHttpStatus.OK;
    }

    @Override
    public CouchErrorContent getErrorContent() {
        return CouchErrorContent.parseJson(getContent());
    }

    @Override
    public String getErrorReason() {
        return getErrorContent().getReason();
    }

}
