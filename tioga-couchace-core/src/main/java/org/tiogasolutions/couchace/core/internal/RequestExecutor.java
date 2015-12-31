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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.api.meta.CouchEmbeddedAttachmentMeta;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.couchace.core.api.request.*;
import org.tiogasolutions.couchace.core.api.response.*;
import org.tiogasolutions.couchace.core.spi.http.*;

public class RequestExecutor {
    private static final Logger log = LoggerFactory.getLogger(RequestExecutor.class);

    private final CouchDatabase couch;
    private final CouchHttpClient httpClient;
    private final HttpRequestFactory httpRequestFactory;
    private final GetResponseBuilder getResponseBuilder;
    private final CouchMetaRepository metaRepository;

    public RequestExecutor(CouchDatabase couch) {
        this.couch = couch;
        this.httpClient = couch.getHttpClient();
        this.metaRepository = couch.getMetaRepository();
        this.httpRequestFactory = new HttpRequestFactory(couch.getDatabaseName(), couch.getJsonStrategy());
        this.getResponseBuilder = new GetResponseBuilder(couch);
    }

    public HeadResponse execute(HeadRequest request) {
        HttpHeadRequest httpHeadRequest = httpRequestFactory.newHttpHeadRequest(request);
        CouchHttpResponse httpResponse = httpClient.head(httpHeadRequest);
        HeadResponse response = new HeadResponse(httpResponse);

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;
    }

    public GetDocumentResponse execute(GetDocumentRequest request) {
        HttpGetRequest httpGetRequest = httpRequestFactory.newHttpGetRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.get(httpGetRequest);

        GetDocumentResponse response = getResponseBuilder.buildDocumentResponse(request, couchHttpResponse);

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;

    }

    public GetContentResponse execute(GetDatabaseRequest request) {
        HttpGetRequest httpGetRequest = httpRequestFactory.newHttpGetRequest(request);
        CouchHttpResponse httpResponse = httpClient.get(httpGetRequest);

        CouchErrorContent errorContent = CouchErrorContent.noError;
        if (httpResponse.isError() && httpResponse.getContentType() == CouchMediaType.APPLICATION_JSON) {
            errorContent = CouchErrorContent.parseJson(httpResponse.getStringContent());
        }

        GetContentResponse response = new GetContentResponse(httpResponse.getUri(),
                httpResponse.getHttpStatus(),
                httpResponse.getContentType(),
                httpResponse.getContent(),
                errorContent);

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;
    }

    public GetAttachmentResponse execute(GetAttachmentRequest request) {
        HttpGetRequest httpGetRequest = httpRequestFactory.newHttpGetRequest(request);
        CouchHttpResponse httpResponse = httpClient.get(httpGetRequest);

        CouchErrorContent errorContent = CouchErrorContent.noError;
        if (httpResponse.isError() && httpResponse.getContentType() == CouchMediaType.APPLICATION_JSON) {
            errorContent = CouchErrorContent.parseJson(httpResponse.getStringContent());
        }

        GetAttachmentResponse response = new GetAttachmentResponse(
                httpResponse.getUri(),
                httpResponse.getHttpStatus(),
                httpResponse.getDocumentId(),
                httpResponse.getEtag(),
                httpResponse.getContentType(),
                httpResponse.getContent(),
                errorContent);

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;

    }

    public <T> GetEntityResponse<T> execute(GetEntityRequest<T> request) {
        // Execute the GET
        HttpGetRequest httpGetRequest = httpRequestFactory.newHttpGetRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.get(httpGetRequest);

        GetEntityResponse<T> response = getResponseBuilder.buildEntityResponse(request, couchHttpResponse);

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;
    }

    public WriteResponse execute(PutRequest request) {

        WriteResponse response;
        if (request instanceof PutEntityRequest) {
            response = executePutEntity((PutEntityRequest) request);

        } else if (request instanceof PutDocumentRequest) {
            response =  executePutDocument((PutDocumentRequest) request);

        } else if (request instanceof PutAttachmentRequest) {
            response =  executePutAttachment((PutAttachmentRequest) request);

        } else if (request instanceof PutDesignRequest) {
            response =  executePutDesign((PutDesignRequest) request);

        } else if (request instanceof PutDatabaseRequest) {
            response =  executePutDatabase((PutDatabaseRequest) request);

        } else {
            String className = (request == null) ? "null" : request.getClass().getName();
            String msg = String.format("The request %s is not supported.", className);
            throw new UnsupportedOperationException(msg);
        }

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;

    }

    protected WriteResponse executePutEntity(PutEntityRequest request) {
        CouchEntityMeta entityMeta = metaRepository.getOrCreateEntityMeta(request.getEntityClass());

        if (request.getDocumentId() == null && request.getDocumentRevision() == null) {
            // Request does not define documentId or revision so look to the EntityMeta
            if (entityMeta.hasId()) {
                // Read the id and revision
                Object entity = request.getEntity();
                String id = entityMeta.readDocumentId(entity);
                String revision = entityMeta.readDocumentRevision(entity);
                String entityType = entityMeta.getEntityType();
                CouchAttachmentInfoMap attachmentInfoMap = entityMeta.readAttachmentInfoMap(entity);

                // Create the PutEntityRequest
                if (revision != null) {
                    request = couch.put().entity(id, entityType, entity, revision, attachmentInfoMap);
                } else {
                    request = couch.put().entity(id, entityType, entity, attachmentInfoMap);
                }
            } else {
                throw CouchException.internalServerError("Cannot build PutEntityRequest for entity " + request.getEntityClass() + ", @CouchId is not specified.");
            }
        }

        HttpPutRequest httpPutRequest = httpRequestFactory.newHttpPutRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.put(httpPutRequest);

        if (couchHttpResponse.isSuccess() && entityMeta.hasEmbeddedAttachments()) {
            couchHttpResponse = writeEntityAttachments(entityMeta, couchHttpResponse, request.getEntity());
        }

        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executePutDocument(PutDocumentRequest request) {
        HttpPutRequest httpPutRequest = httpRequestFactory.newHttpPutRequest(request);
        return new WriteResponse(httpClient.put(httpPutRequest));
    }

    protected WriteResponse executePutAttachment(PutAttachmentRequest request) {
        HttpPutRequest httpPutRequest = httpRequestFactory.newHttpPutRequest(request);
        CouchHttpResponse response = httpClient.put(httpPutRequest);
        return new WriteResponse(response);
    }

    protected WriteResponse executePutDesign(PutDesignRequest request) {
        HttpPutRequest httpPutRequest = httpRequestFactory.newHttpPutRequest(request);
        CouchHttpResponse response = httpClient.put(httpPutRequest);
        return new WriteResponse(response);
    }

    protected WriteResponse executePutDatabase(PutDatabaseRequest request) {
        HttpPutRequest httpPutRequest = httpRequestFactory.newHttpPutRequest(request);
        return new WriteResponse(httpClient.put(httpPutRequest));
    }

    public WriteResponse execute(PostRequest request) {
        WriteResponse response;
        if (request instanceof PostEntityRequest) {
            response = executePostEntity((PostEntityRequest) request);

        } else if (request instanceof PostDocumentRequest) {
            response = executePostDocument((PostDocumentRequest)request);

        } else if (request instanceof PostDatabaseRequest) {
            response = executePostDatabase((PostDatabaseRequest)request);

        } else {
            String className = (request == null) ? "null" : request.getClass().getName();
            String msg = String.format("The request %s is not supported.", className);
            throw new UnsupportedOperationException(msg);
        }

        // Call onError, onSuccess and onResponse
        if (response.isError() && request.getOnError() != null) {
            request.getOnError().handle(response);
        } else if (request.getOnSuccess() != null) {
            request.getOnSuccess().handle(response);
        }
        if (request.getOnResponse() != null) {
            request.getOnResponse().handle(response);
        }

        return response;

    }

    protected WriteResponse executePostEntity(PostEntityRequest request) {
        CouchEntityMeta entityMeta = metaRepository.getOrCreateEntityMeta(request.getEntityClass());
        HttpPostRequest httpPostRequest = httpRequestFactory.newHttpPostRequest(request);

        CouchHttpResponse couchHttpResponse = httpClient.post(httpPostRequest);

        if (couchHttpResponse.isSuccess() && entityMeta.hasEmbeddedAttachments()) {
            couchHttpResponse = writeEntityAttachments(entityMeta, couchHttpResponse, request.getEntity());
        }

        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executePostDocument(PostDocumentRequest request) {
        HttpPostRequest httpPostRequest = httpRequestFactory.newHttpPostRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.post(httpPostRequest);
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executePostDatabase(PostDatabaseRequest request) {
        HttpPostRequest httpPostRequest = httpRequestFactory.newHttpPostRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.post(httpPostRequest);
        return new WriteResponse(couchHttpResponse);
    }

    public WriteResponse execute(DeleteRequest request) {

        if (request instanceof DeleteEntityRequest) {
            return executeDeleteEntity((DeleteEntityRequest) request);

        } else if (request instanceof DeleteAttachmentRequest) {
            return executeDeleteAttachment((DeleteAttachmentRequest)request);

        } else if (request instanceof DeleteDocumentRequest) {
            DeleteDocumentRequest deleteRequest = (DeleteDocumentRequest)request;
            if (deleteRequest.getType() == DeleteDocumentRequest.Type.DOCUMENT) {
                return executeDeleteDocument(deleteRequest);

            } else if (deleteRequest.getType() == DeleteDocumentRequest.Type.DATABASE) {
                return executeDeleteDatabase(deleteRequest);

            } else if (deleteRequest.getType() == DeleteDocumentRequest.Type.ALL_DOCUMENTS) {
                return executeDeleteAllDocuments();

            } else if (deleteRequest.getType() == DeleteDocumentRequest.Type.ALL_DESIGNS) {
                return executeDeleteAllDesigns();

            } else if (deleteRequest.getType() == DeleteDocumentRequest.Type.ALL_NON_DESIGNS) {
                return executeDeleteAllNonDesigns();

            } else {
                throw CouchException.badRequest("Unsupported DeleteRequestType " + deleteRequest.getType());
            }
        } else {
            String className = (request == null) ? "null" : request.getClass().getName();
            String msg = String.format("The request %s is not supported.", className);
            throw CouchException.badRequest(msg);
        }
    }

    protected WriteResponse executeDeleteEntity(DeleteEntityRequest request) {
        CouchEntityMeta entityMeta = metaRepository.getOrCreateEntityMeta(request.getEntityClass());

        // Request does not define documentId or revision so look to the EntityMeta
        if (entityMeta.hasId() == false) {
            throw CouchException.internalServerError("Cannot build DeleteEntityRequest for entity " + request.getEntityClass() + ", @CouchId is not specified.");
        }

        // Read the id and revision
        Object entity = request.getEntity();
        String id = entityMeta.readDocumentId(entity);
        String revision = entityMeta.readDocumentRevision(entity);

        // Use DeleteDocumentRequest to actually perform the delete.
        DeleteDocumentRequest deleteDocumentRequest = couch.delete().document(id, revision);
        HttpDeleteRequest httpDeleteRequest = httpRequestFactory.newHttpDeleteRequest(deleteDocumentRequest);
        CouchHttpResponse couchHttpResponse = httpClient.delete(httpDeleteRequest);
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteAttachment(DeleteAttachmentRequest request) {
        HttpDeleteRequest httpDeleteRequest = httpRequestFactory.newHttpDeleteRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.delete(httpDeleteRequest);
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteDocument(DeleteDocumentRequest request) {
        HttpDeleteRequest httpDeleteRequest = httpRequestFactory.newHttpDeleteRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.delete(httpDeleteRequest);
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteDatabase(DeleteDocumentRequest request) {
        HttpDeleteRequest httpDeleteRequest = httpRequestFactory.newHttpDeleteRequest(request);
        CouchHttpResponse couchHttpResponse = httpClient.delete(httpDeleteRequest);
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteAllDocuments() {
        CouchHttpResponse couchHttpResponse;
        String path = "_all_docs";
        GetDocumentResponse getResponse = new GetDocumentRequest(this, path, null).execute();
        for (TextDocument document : getResponse) {
            String id = document.getDocumentId();
            String revision = document.getDocumentRevision();
            WriteResponse deleteResponse = new DeleteDocumentRequest(this, DeleteDocumentRequest.Type.DOCUMENT, id, revision)
                    .execute();
            if (deleteResponse.isError()) {
                throw new CouchException(deleteResponse.getHttpStatus(), "Error deleting design document " + id);
            }
        }
        couchHttpResponse = CouchHttpResponse.builder(CouchMethodType.DELETE, httpClient.uri(path), CouchHttpStatus.OK).build();
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteAllDesigns() {
        CouchHttpResponse couchHttpResponse;
        String path = "/_all_docs?startkey=%22_design/%22&endkey=%22_design0%22";
        GetDocumentResponse getResponse = new GetDocumentRequest(this, path, null).execute();
        for (TextDocument document : getResponse) {
            String id = document.getDocumentId();
            if (id.startsWith("_design")) {
                String revision = document.getDocumentRevision();
                WriteResponse deleteResponse = new DeleteDocumentRequest(this, DeleteDocumentRequest.Type.DOCUMENT, id, revision)
                        .execute();
                if (deleteResponse.isError()) {
                    throw new CouchException(deleteResponse.getHttpStatus(), "Error deleting design document " + id);
                }
            }
        }
        couchHttpResponse = CouchHttpResponse.builder(CouchMethodType.DELETE, httpClient.uri(path), CouchHttpStatus.OK).build();
        return new WriteResponse(couchHttpResponse);
    }

    protected WriteResponse executeDeleteAllNonDesigns() {
        CouchHttpResponse couchHttpResponse;
        String path = "_all_docs";
        GetDocumentResponse getResponse = new GetDocumentRequest(this, path, null).execute();
        for (TextDocument document : getResponse) {
            String id = document.getDocumentId();
            if (id != null && !id.startsWith("_design")) {
                String revision = document.getDocumentRevision();
                WriteResponse deleteResponse = new DeleteDocumentRequest(this, DeleteDocumentRequest.Type.DOCUMENT, id, revision)
                        .execute();
                if (deleteResponse.isError()) {
                    String msg = String.format("Error deleting document %s rev(%s) - %s", id, revision, deleteResponse.getErrorReason());
                    throw new CouchException(deleteResponse.getHttpStatus(), msg);
                }
            }
        }
        couchHttpResponse = CouchHttpResponse.builder(CouchMethodType.DELETE, httpClient.uri(path), CouchHttpStatus.OK).build();
        return new WriteResponse(couchHttpResponse);
    }

    protected CouchHttpResponse writeEntityAttachments(CouchEntityMeta<?> entityMeta, CouchHttpResponse couchHttpResponse, Object entity) {
        // Put any attachments
        for (CouchEmbeddedAttachmentMeta attachmentMeta : entityMeta.getEmbeddedAttachmentMetaList()) {
            try {
                Object value = attachmentMeta.readValue(entity);
                if (value != null) {
                    couchHttpResponse = writeAttachment(attachmentMeta, couchHttpResponse.getDocumentId(), couchHttpResponse.getEtag(), value);
                }
            } catch (CouchException e) {
                log.error("Exception writing entity attachment: " + e.getMessage(), e);
                throw new CouchException(CouchHttpStatus.INTERNAL_SERVER_ERROR, "Exception saving attachment: " + e.getMessage(), e);
            }
        }
        return couchHttpResponse;
    }

    protected CouchHttpResponse writeAttachment(CouchEmbeddedAttachmentMeta attachmentMeta, String documentId, String revision, Object content) {
        PutAttachmentRequest putAttachmentRequest = new PutAttachmentRequest(
                this,
                documentId,
                revision,
                attachmentMeta.getAttachmentName(),
                attachmentMeta.getContentType(),
                content);

        CouchHttpResponse couchHttpResponse = httpClient.put(httpRequestFactory.newHttpPutRequest(putAttachmentRequest));

        if (couchHttpResponse.isError()) {
            CouchErrorContent errorContent = CouchErrorContent.parseJson(couchHttpResponse.getStringContent());
            String msg = String.format("Store of document successful but failed in storage of attachment with status: %s and error content: %s",
                    couchHttpResponse.getHttpStatus().getReason(),
                    errorContent.toString());
            throw new CouchException(couchHttpResponse.getHttpStatus(), msg);
        }

        return couchHttpResponse;

    }


}
