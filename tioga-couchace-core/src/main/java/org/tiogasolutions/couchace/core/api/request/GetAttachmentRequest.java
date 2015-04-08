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

import org.tiogasolutions.couchace.core.api.response.CouchResponseHandler;
import org.tiogasolutions.couchace.core.api.response.GetAttachmentResponse;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public class GetAttachmentRequest extends ReadDocumentRequest {

    private final RequestExecutor requestExecutor;
    private final String attachmentName;
    private CouchResponseHandler<GetAttachmentResponse> onResponse;
    private CouchResponseHandler<GetAttachmentResponse> onSuccess;
    private CouchResponseHandler<GetAttachmentResponse> onError;

    public GetAttachmentRequest(RequestExecutor requestExecutor, String documentId, String documentRevision, String attachmentName) {
        super(documentId, documentRevision);
        this.requestExecutor = requestExecutor;
        this.attachmentName = attachmentName;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public GetAttachmentResponse execute() {
        return requestExecutor.execute(this);
    }

    public GetAttachmentRequest onResponse(CouchResponseHandler<GetAttachmentResponse> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public GetAttachmentRequest onSuccess(CouchResponseHandler<GetAttachmentResponse> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public GetAttachmentRequest onError(CouchResponseHandler<GetAttachmentResponse> onError) {
        this.onError = onError;
        return this;
    }

    public CouchResponseHandler<GetAttachmentResponse> getOnResponse() {
        return onResponse;
    }

    public CouchResponseHandler<GetAttachmentResponse> getOnSuccess() {
        return onSuccess;
    }

    public CouchResponseHandler<GetAttachmentResponse> getOnError() {
        return onError;
    }

}
