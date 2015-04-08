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

import org.tiogasolutions.couchace.core.api.http.CouchHttpQuery;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public abstract class HttpReadRequest extends HttpRequest {
    private final CouchMediaType acceptType;
    private final String documentId;

    public HttpReadRequest(String path,
                           CouchHttpQuery httpQuery,
                           String documentId,
                           CouchMediaType acceptType) {

        super(path, httpQuery);
        this.documentId = documentId;
        this.acceptType = acceptType;
    }

    public HttpReadRequest() {

        super();
        this.documentId = null;
        this.acceptType = CouchMediaType.APPLICATION_JSON;
    }

    public String getDocumentId() {
        return documentId;
    }

    public CouchMediaType getAcceptType() {
        return acceptType;
    }
}
