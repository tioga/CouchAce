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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.response.CouchResponseHandler;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class PutEntityRequest implements PutRequest {
    private final RequestExecutor requestExecutor;
    private final String documentId;
    private final String documentRevision;
    private final String entityType;
    private final Object entity;
    private final CouchAttachmentInfoMap attachmentInfoMap;
    private CouchResponseHandler<WriteResponse> onResponse;
    private CouchResponseHandler<WriteResponse> onSuccess;
    private CouchResponseHandler<WriteResponse> onError;

    public PutEntityRequest(RequestExecutor requestExecutor,
                            String documentId,
                            String entityType,
                            Object entity,
                            String documentRevision,
                            CouchAttachmentInfoMap attachmentInfoMap) {
        this.requestExecutor = requestExecutor;
        this.documentId = documentId;
        this.documentRevision = documentRevision;
        this.entityType = entityType;
        this.entity = entity;
        this.attachmentInfoMap = attachmentInfoMap;
    }

    public PutEntityRequest(RequestExecutor requestExecutor,
                            String documentId,
                            String entityType,
                            Object entity,
                            String documentRevision) {
        this(requestExecutor, documentId, entityType, entity, documentRevision, null);
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public String getEntityType() {
        return entityType;
    }

    public Object getEntity() {
        return entity;
    }

    public CouchAttachmentInfoMap getAttachmentInfoMap() {
        return attachmentInfoMap;
    }

    public Class<?> getEntityClass() {
        return (entity != null) ? entity.getClass() : Object.class;
    }

    public WriteResponse execute() {
        return requestExecutor.execute(this);
    }

    public PutEntityRequest onResponse(CouchResponseHandler<WriteResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public PutEntityRequest onSuccess(CouchResponseHandler<WriteResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public PutEntityRequest onError(CouchResponseHandler<WriteResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<WriteResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<WriteResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<WriteResponse> getOnError() {
        return onError;
    }

}
