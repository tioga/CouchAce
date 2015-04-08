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

import org.tiogasolutions.couchace.core.api.query.CouchJsonKey;

public class TextDocument implements CouchDocument<String> {
    private final String documentId;
    private final String documentRevision;
    private final CouchJsonKey key;
    private final String content;

    public TextDocument(String documentId, String documentRevision, CouchJsonKey key, String content) {
        this.documentId = documentId;
        this.documentRevision = documentRevision;
        this.key = key;
        this.content = content;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public String getDocumentRevision() {
        return documentRevision;
    }

    @Override
    public CouchJsonKey getKey() {
        return key;
    }

    @Override
    public String getContent() {
        return content;
    }

    public Integer getContentAsInteger() {
        return (content != null) ? Integer.valueOf(content) : null;
    }

    public Long getContentAsLong() {
        return (content != null) ? Long.valueOf(content) : null;
    }
}
