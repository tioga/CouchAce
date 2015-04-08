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
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.api.response.CouchErrorContent;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.net.URI;

/**
 * User: harlan
 * Date: 2/7/14
 * Time: 9:52 AM
 */
public class CouchHttpResponseBuilder {
    private final URI uri;
    private final CouchHttpStatus httpStatus;
    private final CouchMethodType methodType;
    private String documentId;
    private String etag;
    private CouchMediaType contentType;
    private int contentLength;
    private Object content;

    public CouchHttpResponseBuilder(CouchMethodType methodType, URI uri, CouchHttpStatus httpStatus) {
        ArgUtil.assertNotNull(methodType, "HttpMethodType");
        ArgUtil.assertNotNull(uri, "URI");
        ArgUtil.assertNotNull(httpStatus, "HttpStatus");
        this.uri = uri;
        this.httpStatus = httpStatus;
        this.methodType = methodType;
    }

    public CouchHttpResponse build() {
        return new CouchHttpResponse(methodType, uri, httpStatus, documentId, etag, contentType, content);
    }

    public CouchHttpResponseBuilder setContent(CouchMediaType mediaType, int contentLength, Object content) {
        this.contentType = mediaType;
        this.contentLength = contentLength;
        this.content = content;
        return this;
    }

    public CouchHttpResponseBuilder setErrorContent(CouchErrorContent errorContent) {
        this.contentType = CouchMediaType.APPLICATION_JSON;
        String json = errorContent.toJson();
        this.content = json;
        this.contentLength = json.length();
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public CouchHttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDocumentId() {
        return documentId;
    }

    public CouchHttpResponseBuilder setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public CouchHttpResponseBuilder setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public CouchMediaType getContentType() {
        return contentType;
    }

    public Object getContent() {
        return content;
    }

    public CouchMethodType getMethodType() {
        return methodType;
    }

    public int getContentLength() {
        return contentLength;
    }
}
