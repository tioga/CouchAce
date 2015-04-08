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

import org.tiogasolutions.couchace.core.api.response.CouchDocument;
import org.tiogasolutions.couchace.core.api.CouchSetup;

import java.net.URI;

/**
 * User: harlan
 * Date: 2/3/14
 * Time: 6:09 PM
 */
public interface CouchHttpClient {

    void init(CouchSetup couchSetup);

    CouchHttpResponse createDatabase(String databaseName);

    CouchHttpResponse deleteDatabase(String databaseName);

    CouchHttpResponse head(HttpHeadRequest headRequest);

    CouchHttpResponse get(HttpGetRequest request);

    CouchHttpResponse put(HttpPutRequest putRequest);

    CouchHttpResponse post(HttpPostRequest postRequest);

    CouchHttpResponse delete(HttpDeleteRequest request);

    URI pageUri(URI currentPageUri, CouchDocument document, boolean reverse);

    URI uri(String... paths);

    String getBaseUrl();

}
