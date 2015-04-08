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
public class HeadResponse implements CouchResponse {
    private final URI uri;
    private final CouchHttpStatus httpStatus;
    private final CouchMediaType contentType;
    private final String documentId;
    private final String documentRevision;

    public HeadResponse(CouchHttpResponse response) {
        this(response.getUri(),
                response.getHttpStatus(),
                response.getContentType(),
                response.getDocumentId(),
                response.getEtag());
    }

    public HeadResponse(URI uri, CouchHttpStatus httpStatus, CouchMediaType contentType, String documentId, String documentRevision) {
        ArgUtil.assertNotNull(uri, "URI");
        ArgUtil.assertNotNull(httpStatus, "HttpStatus");
        this.uri = uri;
        this.httpStatus = httpStatus;
        this.contentType = (contentType != null) ? contentType : CouchMediaType.UNDEFINED;
        this.documentId = documentId;
        this.documentRevision = documentRevision;
    }

    public URI getUri() {
        return uri;
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

    @Override
    public CouchMediaType getContentType() {
        return contentType;
    }

    @Override
    public CouchMethodType getMethodType() {
        return CouchMethodType.HEAD;
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
    public boolean isOk() {
        return httpStatus.isOk();
    }

    @Override
    public boolean isNotFound() {
        return httpStatus.isNotFound();
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
    public boolean isCreated() {
        return httpStatus.isCreated();
    }

    @Override
    public boolean isConflict() {
        return httpStatus.isConflict();
    }

    @Override
    public CouchErrorContent getErrorContent() {
        return CouchErrorContent.noError;
    }

    @Override
    public String getErrorReason() {
        return CouchErrorContent.noError.getReason();
    }

}
