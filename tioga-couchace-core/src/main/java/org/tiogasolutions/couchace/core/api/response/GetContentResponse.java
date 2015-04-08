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

import java.net.URI;

/**
 * User: harlan
 * Date: 7/27/14
 * Time: 3:51 PM
 */
public class GetContentResponse extends GetResponse {

    public static GetContentResponse withError(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, CouchErrorContent errorContent) {
        return new GetContentResponse(uri, statusCode, contentType, null, errorContent);
    }

    public static GetContentResponse withContent(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Object content) {
        return new GetContentResponse(uri, statusCode, contentType, content, null);
    }

    private final Object content;

    public GetContentResponse(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Object content, CouchErrorContent errorContent) {
        super(uri, statusCode, contentType, errorContent);
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public String getStringContent() {
        return (content != null) ? content.toString() : null;
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
