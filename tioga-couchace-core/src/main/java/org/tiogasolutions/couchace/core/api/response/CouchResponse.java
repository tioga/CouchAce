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
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;

import java.net.URI;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public interface CouchResponse {

    URI getUri();

    CouchMediaType getContentType();

    CouchMethodType getMethodType();

    CouchHttpStatus getHttpStatus();

    int getHttpStatusCode();

    boolean isOk();

    boolean isError();

    boolean isCreated();

    boolean isNotFound();

    boolean isSuccess();

    boolean isConflict();

    boolean isUnauthorized();

    CouchErrorContent getErrorContent();

    String getErrorReason();


}
