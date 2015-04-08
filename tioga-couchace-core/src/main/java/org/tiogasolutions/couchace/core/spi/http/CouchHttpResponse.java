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
package org.tiogasolutions.couchace.core.spi.http;

import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.response.CouchErrorContent;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.net.URI;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public class CouchHttpResponse {
    private final URI uri;
    private final CouchHttpStatus httpStatus;
    private final CouchMethodType methodType;
    private final String documentId;
    private final String etag;
    private final CouchMediaType contentType;
    private final Object content;

    public static CouchHttpResponseBuilder builder(CouchMethodType methodType, URI uri, CouchHttpStatus statusCode) {
        return new CouchHttpResponseBuilder(methodType, uri, statusCode);
    }

    public CouchHttpResponse(CouchMethodType methodType,
                             URI uri,
                             CouchHttpStatus httpStatus,
                             String documentId,
                             String etag,
                             CouchMediaType contentType,
                             Object content) {
        ArgUtil.assertNotNull(methodType, "HttpMethodType");
        ArgUtil.assertNotNull(uri, "URI");
        ArgUtil.assertNotNull(httpStatus, "HttpStatusCode");
        this.uri = uri;
        this.methodType = methodType;
        this.httpStatus = httpStatus;
        this.documentId = documentId;
        this.etag = etag;
        this.contentType = contentType;
        this.content = content;
    }

    public CouchMethodType getMethodType() {
        return methodType;
    }

    public URI getUri() {
        return uri;
    }

    public CouchHttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.getCode();
    }

    public boolean isOk() {
        return httpStatus.isOk();
    }

    public boolean isNotFound() {
        return httpStatus.isNotFound();
    }

    public boolean isCreated() {
        return httpStatus.isCreated();
    }

    public boolean isConflict() {
        return httpStatus.isConflict();
    }

    public boolean isSuccess() {
        return httpStatus == CouchHttpStatus.CREATED || httpStatus == CouchHttpStatus.OK;
    }

    public boolean isError() {
        return httpStatus != CouchHttpStatus.CREATED && httpStatus != CouchHttpStatus.OK;
    }

    public CouchMediaType getContentType() {
        return contentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getEtag() {
        return etag;
    }

    public boolean hasEtag() {
        return etag != null;
    }

    public Object getContent() {
        return content;
    }

    public String getStringContent() {
        return (content != null) ? content.toString() : null;
    }

    public Long getLongContent() {
        return (content != null) ? Long.valueOf(content.toString()) : null;
    }

    public CouchErrorContent getErrorContent() {
        return CouchErrorContent.parseJson(getStringContent());
    }

    public String getErrorReason() {
        return getErrorContent().getReason();
    }

    public boolean isEmpty() {
        if (content == null) {
            return true;
        } else if (content instanceof String) {
            return ((String) content).isEmpty();
        } else {
            return false;
        }
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

}
