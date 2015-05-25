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

import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.api.http.CouchHttpQuery;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.util.UriUtil;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;
import org.tiogasolutions.couchace.core.api.request.*;

/**
 * User: harlan
 * Date: 2/8/14
 * Time: 12:27 PM
 */
public class HttpRequestFactory {

    private final String databaseName;
    private final CouchJsonStrategy jsonStrategy;

    public HttpRequestFactory(String databaseName, CouchJsonStrategy jsonStrategy) {
        this.databaseName = databaseName;
        this.jsonStrategy = jsonStrategy;
    }

    public HttpHeadRequest newHttpHeadRequest(HeadRequest request) {
        PathAndQuery pathAndQuery = buildPathAndQuery(request);
        return new HttpHeadRequest(pathAndQuery.path, pathAndQuery.httpQuery, request.getDocumentId());
    }

    public HttpGetRequest newHttpGetRequest(ReadDocumentRequest request) {
        PathAndQuery pathAndQuery = buildPathAndQuery(request);

        CouchMediaType acceptType = CouchMediaType.APPLICATION_JSON;

        if (request instanceof GetAttachmentRequest) {
            // REVIEW - Not sure why I'm doing this but too scared to remove it.
            acceptType = null;
        }

        return new HttpGetRequest(pathAndQuery.path, pathAndQuery.httpQuery, request.getDocumentId(), acceptType);
    }

    public HttpGetRequest newHttpGetRequest(GetDatabaseRequest request) {
        String path = UriUtil.buildPathIgnoreNull(databaseName, request.getPath());

        return new HttpGetRequest(path, request.getHttpQuery(), null, request.getContentType());
    }

    protected PathAndQuery buildPathAndQuery(ReadDocumentRequest request) {
        String path;
        CouchHttpQuery.Builder queryBuilder = CouchHttpQuery.Builder();

        if (request instanceof GetAttachmentRequest) {
            // Attachment
            GetAttachmentRequest getAttachmentRequest = (GetAttachmentRequest) request;
            path = UriUtil.buildPath(databaseName, request.getDocumentId(), getAttachmentRequest.getAttachmentName());
            if (request.getDocumentRevision() != null) {
                queryBuilder.add("rev", request.getDocumentRevision());
            }
        } else if (request.getDocumentId() != null) {
            // Document id
            path = UriUtil.buildPath(databaseName, request.getDocumentId());
            if (request.getDocumentRevision() != null) {
                queryBuilder.add("rev", request.getDocumentRevision());
            }
        } else if (request.getViewQuery() != null) {
            // View query
            CouchViewQuery viewQuery = request.getViewQuery();
            path = UriUtil.buildPath(
                    databaseName,
                    "_design",
                    viewQuery.getDesignName(),
                    "_view",
                    viewQuery.getViewName());

            queryBuilder.add("include_docs", String.valueOf(viewQuery.isIncludeDocs()));
            if (viewQuery.hasKey()) {
                queryBuilder.add("key", viewQuery.getKeyJson());
            }
            if (viewQuery.hasStartKey()) {
                queryBuilder.add("startkey", viewQuery.getStartKeyJson());
            }
            if (viewQuery.hasEndKey()) {
                queryBuilder.add("endkey", viewQuery.getEndKeyJson());
            }
            if (viewQuery.getLimit() > 0) {
                queryBuilder.add("limit", String.valueOf(viewQuery.getLimit() + 1));
            }
            if (viewQuery.getSkip() > 0) {
                queryBuilder.add("skip", String.valueOf(viewQuery.getSkip()));
            }
            if (viewQuery.isDescending()) {
                queryBuilder.add("descending", String.valueOf(viewQuery.isDescending()));
            }
            if (viewQuery.isGroup()) {
                queryBuilder.add("group", String.valueOf(viewQuery.isGroup()));
            }

        } else if (request.getPageQuery() != null) {
            // Page query
            CouchPageQuery pageQuery = request.getPageQuery();
            path = UriUtil.buildPath(databaseName, pageQuery.getRequestedPage());
            queryBuilder.add("include_docs", String.valueOf(pageQuery.isIncludeDocs()));
            queryBuilder.add("limit", String.valueOf(pageQuery.getPageSize() + 1));

        } else {
            // Unsupported
            throw CouchException.badRequest("ReadRequest did not supply a document id, view query or page query");
        }

        return new PathAndQuery(path, queryBuilder.build());

    }

    public HttpDeleteRequest newHttpDeleteRequest(DeleteDocumentRequest request) {
        String path;
        if (request.getType() == DeleteDocumentRequest.Type.DATABASE) {
            path = UriUtil.buildPath(databaseName);
            return new HttpDeleteRequest(path);
        } else {
            path = UriUtil.buildPath(databaseName, request.getDocumentId());
            CouchHttpQuery.Builder builder = CouchHttpQuery.Builder();
            builder.add("rev", request.getDocumentRevision());
            return new HttpDeleteRequest(path, request.getDocumentId(), builder.build());
        }
    }

    public HttpDeleteRequest newHttpDeleteRequest(DeleteAttachmentRequest request) {
        String path = UriUtil.buildPath(databaseName, request.getDocumentId(), request.getAttachmentName());
        CouchHttpQuery.Builder builder = CouchHttpQuery.Builder();
        builder.add("rev", request.getDocumentRevision());
        return new HttpDeleteRequest(path, request.getDocumentId(), builder.build());
    }

    public HttpPostRequest newHttpPostRequest(PostRequest request) {

        if (request instanceof PostEntityRequest) {
            return newHttpPostEntityRequest((PostEntityRequest) request);

        } else if (request instanceof PostDocumentRequest) {
            return newHttpPostDocumentRequest((PostDocumentRequest) request);

        } else if (request instanceof PostDatabaseRequest) {
            return newHttpPostDatabaseRequest((PostDatabaseRequest) request);

        } else {
            String className = (request == null) ? "null" : request.getClass().getName();
            String msg = String.format("The request %s is not supported.", className);
            throw CouchException.badRequest(msg);
        }
    }

    protected HttpPostRequest newHttpPostEntityRequest(PostEntityRequest request) {
        String json = jsonStrategy.createJsonForPost(request);
        return new HttpPostRequest(databaseName, json);
    }

    protected HttpPostRequest newHttpPostDocumentRequest(PostDocumentRequest request) {
        String json = request.getDocument();
        return new HttpPostRequest(databaseName, json);
    }

    protected HttpPostRequest newHttpPostDatabaseRequest(PostDatabaseRequest request) {
        String path = UriUtil.buildPathIgnoreNull(databaseName, request.getPath());
        return new HttpPostRequest(path, request.getContent());
    }

    public HttpPutRequest newHttpPutRequest(PutRequest request) {

        if (request instanceof PutEntityRequest) {
            return newHttpPutEntityRequest((PutEntityRequest) request);

        } else if (request instanceof PutDocumentRequest) {
            return newHttpPutDocumentRequest((PutDocumentRequest) request);

        } else if (request instanceof PutAttachmentRequest) {
            return newHttpPutAttachmentRequest((PutAttachmentRequest) request);

        } else if (request instanceof PutDesignRequest) {
            return newHttpPutDesignRequest((PutDesignRequest) request);

        } else if (request instanceof PutDatabaseRequest) {
            return newHttpPutDatabaseRequest((PutDatabaseRequest) request);

        } else {
            String className = (request == null) ? "null" : request.getClass().getName();
            String msg = String.format("The request %s is not supported.", className);
            throw CouchException.badRequest(msg);
        }
    }

    protected HttpPutRequest newHttpPutEntityRequest(PutEntityRequest request) {

        // Create json document.
        String json = jsonStrategy.createJsonForPut(request);
        CouchHttpQuery.Builder queryBuilder = CouchHttpQuery.Builder();
        if (request.getDocumentRevision() != null) {
            queryBuilder.add("rev", request.getDocumentRevision());
        }

        String path = UriUtil.buildPath(databaseName, request.getDocumentId());
        return new HttpPutRequest(
            path,
            queryBuilder.build(),
            request.getDocumentId(),
            CouchMediaType.APPLICATION_JSON,
            json);
    }

    protected HttpPutRequest newHttpPutDesignRequest(PutDesignRequest request) {
        String designContent = request.getDesignContent();
        String designName = request.getDesignName();
        CouchHttpQuery.Builder queryBuilder = CouchHttpQuery.Builder();

        if (request.getDocumentRevision() != null) {
            queryBuilder.add("rev", request.getDocumentRevision());
        }
        String path = UriUtil.buildPath(databaseName, "_design", designName);
        return new HttpPutRequest(
            path,
            queryBuilder.build(),
            designName,
            CouchMediaType.APPLICATION_JSON,
            designContent);
    }

    protected HttpPutRequest newHttpPutDocumentRequest(PutDocumentRequest request) {
        String documentId = request.getDocumentId();
        CouchHttpQuery.Builder queryBuilder = CouchHttpQuery.Builder();
        if (request.getDocumentRevision() != null) {
            queryBuilder.add("rev", request.getDocumentRevision());
        }
        String path = UriUtil.buildPath(databaseName, documentId);
        return new HttpPutRequest(
            path,
            queryBuilder.build(),
            documentId,
            CouchMediaType.APPLICATION_JSON,
            request.getDocument());
    }

    protected HttpPutRequest newHttpPutDatabaseRequest(PutDatabaseRequest request) {
        String path = UriUtil.buildPathIgnoreNull(databaseName, request.getPath());
        return new HttpPutRequest(
                path,
                null,
                null,
                CouchMediaType.APPLICATION_JSON,
                request.getContent());
    }

    protected HttpPutRequest newHttpPutAttachmentRequest(PutAttachmentRequest request) {
        String path = UriUtil.buildPath(databaseName, request.getDocumentId(), request.getAttachmentName());
        String documentId = request.getDocumentId();
        CouchHttpQuery.Builder queryBuilder = CouchHttpQuery.Builder();
        if (request.getDocumentRevision() != null) {
            queryBuilder.add("rev", request.getDocumentRevision());
        }

        Object content = (request.getContent() != null) ? request.getContent() : null;

        return new HttpPutRequest(
            path,
            queryBuilder.build(),
            documentId,
            request.getContentType(),
            content);
    }

    private static class PathAndQuery {
        private final String path;
        private final CouchHttpQuery httpQuery;

        private PathAndQuery(String path, CouchHttpQuery httpQuery) {
            this.path = path;
            this.httpQuery = httpQuery;
        }
    }

}
