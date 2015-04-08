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

package org.tiogasolutions.couchace.core.internal;

import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.request.GetDocumentRequest;
import org.tiogasolutions.couchace.core.api.response.*;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpResponse;
import org.tiogasolutions.couchace.core.api.query.CouchPageNavigation;
import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.api.request.GetEntityRequest;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchException;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class GetResponseBuilder {
    private static final Logger log = LoggerFactory.getLogger(GetResponseBuilder.class);

    private final CouchDatabase couch;
    private final CouchHttpClient httpClient;
    private final CouchJsonStrategy jsonStrategy;
    private final CouchMetaRepository metaRepository;

    public GetResponseBuilder(CouchDatabase couch) {
        this.couch = couch;
        this.httpClient = couch.getHttpClient();
        this.jsonStrategy = couch.getJsonStrategy();
        this.metaRepository = couch.getMetaRepository();
    }

    public <T> GetEntityResponse<T> buildEntityResponse(GetEntityRequest<T> request, CouchHttpResponse httpResponse) {
        CouchMediaType contentType = CouchMediaType.APPLICATION_JSON;

        CouchEntityMeta<T> entityMeta = metaRepository.getOrCreateEntityMeta(request.getEntityClass());

        GetEntityResponse<T> entityResponse;
        if (httpResponse.isSuccess()) {
            if (request.getDocumentId() != null) {
                EntityDocument<T> entityDocument = jsonStrategy.readEntityDocument(
                        couch.get(),
                        entityMeta,
                        httpResponse.getStringContent());
                entityResponse = GetEntityResponse.withEntity(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        contentType,
                        entityMeta.getEntityClass(),
                        entityDocument);

            } else {
                // Parse entity documents
                List<EntityDocument<T>> entityDocuments = jsonStrategy.readEntityDocuments(
                        couch.get(),
                        entityMeta,
                        httpResponse.getStringContent());
                CouchPageNavigation couchPageNavigation;
                if (request.getViewQuery() != null) {
                    couchPageNavigation = buildPageNavigationForViewQuery(httpResponse.getUri(), entityDocuments, request.getViewQuery());
                } else if (request.getPageQuery() != null) {
                    couchPageNavigation = buildPageNavigationForPageQuery(httpResponse.getUri(), entityDocuments, request.getPageQuery());
                } else {
                    throw new CouchException(CouchHttpStatus.BAD_REQUEST, "Get request did not supply document id, view query or page query.");
                }
                // Return our response.
                entityResponse = GetEntityResponse.withEntities(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        contentType,
                        entityMeta.getEntityClass(),
                        couchPageNavigation,
                        entityDocuments);
            }
        } else {
            CouchErrorContent errorContent = CouchErrorContent.parseJson(httpResponse.getStringContent());
            entityResponse = GetEntityResponse.withError(
                    httpResponse.getUri(),
                    httpResponse.getHttpStatus(),
                    contentType,
                    entityMeta.getEntityClass(),
                    errorContent);
        }

        return entityResponse;


    }

    public GetDocumentResponse buildDocumentResponse(GetDocumentRequest request, CouchHttpResponse httpResponse) {
        GetDocumentResponse docResponse;
        if (httpResponse.isSuccess()) {
            if (request.getDocumentId() != null &&
                (request.getDocumentId().equals("_all_docs") ||
                 request.getDocumentId().startsWith("/_all_docs?"))) {
                List<TextDocument> documentList = jsonStrategy.readTextDocuments(httpResponse.getStringContent());
                docResponse = GetDocumentResponse.withDocuments(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        httpResponse.getContentType(),
                        CouchPageNavigation.empty(),
                        documentList);

            } else if (request.getDocumentId() != null) {
                TextDocument textDocument = jsonStrategy.readTextDocument(httpResponse.getStringContent());
                docResponse = GetDocumentResponse.withDocument(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        httpResponse.getContentType(),
                        textDocument);

            } else if (request.getDocumentId() != null &&
                       (request.getDocumentId().equals("_all_docs") ||
                        request.getDocumentId().startsWith("/_all_docs?"))) {
                List<TextDocument> documentList = jsonStrategy.readTextDocuments(httpResponse.getStringContent());
                docResponse = GetDocumentResponse.withDocuments(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        httpResponse.getContentType(),
                        CouchPageNavigation.empty(),
                        documentList);

            } else {
                List<TextDocument> documentList = jsonStrategy.readTextDocuments(httpResponse.getStringContent());
                CouchPageNavigation couchPageNavigation;
                if (request.getViewQuery() != null) {
                    couchPageNavigation = buildPageNavigationForViewQuery(httpResponse.getUri(), documentList, request.getViewQuery());
                } else if (request.getPageQuery() != null) {
                    couchPageNavigation = buildPageNavigationForPageQuery(httpResponse.getUri(), documentList, request.getPageQuery());
                } else {
                    throw new CouchException(CouchHttpStatus.BAD_REQUEST, "Get request did not supply document id, view query or page query.");
                }
                docResponse = GetDocumentResponse.withDocuments(
                        httpResponse.getUri(),
                        httpResponse.getHttpStatus(),
                        httpResponse.getContentType(),
                        couchPageNavigation,
                        documentList);
            }
        } else {
            CouchErrorContent errorContent = CouchErrorContent.parseJson(httpResponse.getStringContent());
            docResponse = GetDocumentResponse.withError(
                    httpResponse.getUri(),
                    httpResponse.getHttpStatus(),
                    CouchMediaType.APPLICATION_JSON,
                    errorContent);
        }

        return docResponse;
    }

    protected CouchPageNavigation buildPageNavigationForPageQuery(URI requestedPageUri, List<? extends CouchDocument> documentList, CouchPageQuery pageQuery) {

        // Paging
        String backPageUri = null;
        if (!documentList.isEmpty()) {
            CouchDocument pageBeginEntity = documentList.get(0);
            backPageUri = httpClient.pageUri(requestedPageUri, pageBeginEntity, pageQuery.isForward()).toString();
        }

        String forwardPageUri = null;
        if (documentList.size() > pageQuery.getPageSize()) {
            // We found one more that requested so remove last and create pageEndUri.
            documentList.remove(pageQuery.getPageSize());
            CouchDocument pageEndEntity = documentList.get(documentList.size() - 1);
            forwardPageUri = httpClient.pageUri(requestedPageUri, pageEndEntity, pageQuery.isReverse()).toString();
        }

        CouchPageNavigation pageNavigation;
        if (pageQuery.isReverse()) {
            Collections.reverse(documentList);
            pageNavigation = new CouchPageNavigation(pageQuery.getPageSize(), pageQuery.getRequestedPage(), forwardPageUri, backPageUri, pageQuery.isIncludeDocs());
        } else {
            pageNavigation = new CouchPageNavigation(pageQuery.getPageSize(), pageQuery.getRequestedPage(), backPageUri, forwardPageUri, pageQuery.isIncludeDocs());
        }

        return pageNavigation;
    }

    protected CouchPageNavigation buildPageNavigationForViewQuery(URI requestedPageUri, List<? extends CouchDocument> documentList, CouchViewQuery viewQuery) {
        String nextPage = null;
        if (viewQuery.getLimit() > 0 && documentList.size() > viewQuery.getLimit()) {
            // We found one more that requested so we have a next page.
            documentList.remove(viewQuery.getLimit());
            CouchDocument pageEndEntityDoc = documentList.get(documentList.size() - 1);
            nextPage = httpClient.pageUri(requestedPageUri, pageEndEntityDoc, false).toString();
        }

        // With viewQuery it's always the first page so will never have previous, always have requested and may have next.
        String requestedPage = requestedPageUri.toString();
        int designIndex = requestedPage.indexOf("_design");
        if (designIndex > 0) {
            requestedPage = requestedPage.substring(designIndex);
        }
        return new CouchPageNavigation(viewQuery.getLimit(), requestedPage, null, nextPage, viewQuery.isIncludeDocs());
    }

}
