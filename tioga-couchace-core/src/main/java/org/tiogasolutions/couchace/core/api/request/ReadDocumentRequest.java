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
package org.tiogasolutions.couchace.core.api.request;

import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public abstract class ReadDocumentRequest implements ReadRequest {

    private final String documentId;
    private final String documentRevision;
    private final CouchViewQuery viewQuery;
    private final CouchPageQuery pageQuery;

    protected ReadDocumentRequest(String documentId, String documentRevision) {
        ArgUtil.assertNotNull(documentId, "documentId");
        this.documentId = documentId;
        this.documentRevision = documentRevision;
        this.viewQuery = null;
        this.pageQuery = null;
    }

    protected ReadDocumentRequest(CouchViewQuery viewQuery) {
        ArgUtil.assertNotNull(viewQuery, "viewQuery");
        this.viewQuery = viewQuery;
        this.documentId = null;
        this.documentRevision = null;
        this.pageQuery = null;
    }

    protected ReadDocumentRequest(CouchPageQuery pageQuery) {
        ArgUtil.assertNotNull(pageQuery, "pageQuery");
        this.pageQuery = pageQuery;
        this.viewQuery = null;
        this.documentId = null;
        this.documentRevision = null;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public CouchPageQuery getPageQuery() {
        return pageQuery;
    }

    public CouchViewQuery getViewQuery() {
        return viewQuery;
    }

}
