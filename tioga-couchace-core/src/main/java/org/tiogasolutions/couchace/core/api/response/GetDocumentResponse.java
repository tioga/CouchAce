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
import org.tiogasolutions.couchace.core.api.query.CouchPageNavigation;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public class GetDocumentResponse extends GetResponse implements Iterable<TextDocument> {
    private final CouchPageNavigation couchPageNavigation;
    private final List<TextDocument> documentList;

    public static GetDocumentResponse withError(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, CouchErrorContent errorContent) {
        return new GetDocumentResponse(uri, statusCode, contentType, errorContent);
    }

    public static GetDocumentResponse withDocument(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, TextDocument document) {
        return new GetDocumentResponse(uri, statusCode, contentType, null, Collections.singletonList(document));
    }

    public static GetDocumentResponse withDocuments(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, List<TextDocument> documents) {
        return new GetDocumentResponse(uri, statusCode, contentType, null, documents);
    }

    public static GetDocumentResponse withDocuments(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, CouchPageNavigation couchPageNavigation, List<TextDocument> documents) {
        return new GetDocumentResponse(uri, statusCode, contentType, couchPageNavigation, documents);
    }

    private GetDocumentResponse(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, CouchErrorContent errorContent) {
        super(uri, statusCode, contentType, errorContent);
        this.couchPageNavigation = CouchPageNavigation.empty();
        this.documentList = Collections.emptyList();
    }

    private GetDocumentResponse(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, CouchPageNavigation couchPageNavigation, List<TextDocument> documentList) {
        super(uri, statusCode, contentType, null);
        this.couchPageNavigation = couchPageNavigation;
        if (documentList == null) {
            this.documentList = Collections.emptyList();
        } else {
            this.documentList = Collections.unmodifiableList(documentList);
        }
    }

    public List<TextDocument> getDocumentList() {
        return documentList;
    }

    public int getDocumentCount() {
        return documentList.size();
    }

    public boolean isEmpty() {
        return getDocumentList().isEmpty();
    }

    public boolean isNotEmpty() {
        return !getDocumentList().isEmpty();
    }

    public int getSize() {
        return getDocumentList().size();
    }

    public TextDocument getFirstDocument() {
        return getDocumentList().get(0);
    }

    public CouchPageNavigation getCouchPageNavigation() {
        return couchPageNavigation;
    }


    public Long getFirstContentAsLong() {
        if (isEmpty()) {
            return 0L;
        } else {
            return getFirstDocument().getContentAsLong();
        }
    }

    @Override
    public Iterator<TextDocument> iterator() {
        return documentList.iterator();
    }
}
