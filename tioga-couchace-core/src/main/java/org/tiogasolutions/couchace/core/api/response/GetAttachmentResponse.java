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
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.net.URI;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public class GetAttachmentResponse extends GetResponse {
    private final String documentId;
    private final String etag;
    private final Object content;

    public GetAttachmentResponse(URI uri,
                                 CouchHttpStatus statusCode,
                                 String documentId,
                                 String etag,
                                 CouchMediaType contentType,
                                 Object content,
                                 CouchErrorContent errorContent) {
        super(uri, statusCode, contentType, errorContent);
        ArgUtil.assertNotNull(uri, "URI");
        this.documentId = documentId;
        this.etag = etag;
        this.content = content;
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

    @Override
    public CouchErrorContent getErrorContent() {
        return CouchErrorContent.parseJson(getStringContent());
    }

    @Override
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
