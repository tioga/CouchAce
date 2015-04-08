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

package org.tiogasolutions.couchace.core;

import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.response.CouchDocument;
import org.tiogasolutions.couchace.core.spi.http.*;

import java.net.URI;

/**
 * Created by jacob on 3/26/2014.
 */
public class MockCouchHttpClient implements CouchHttpClient {
    @Override
    public void init(CouchSetup couchSetup) {

    }

    @Override
    public CouchHttpResponse createDatabase(String databaseName) {
        return null;
    }

    @Override
    public CouchHttpResponse deleteDatabase(String databaseName) {
        return null;
    }

    @Override
    public CouchHttpResponse head(HttpHeadRequest headRequest) {
        return null;
    }

    @Override
    public CouchHttpResponse get(HttpGetRequest request) {
        return null;
    }

    @Override
    public CouchHttpResponse put(HttpPutRequest putRequest) {
        return null;
    }

    @Override
    public CouchHttpResponse post(HttpPostRequest postRequest) {
        return null;
    }

    @Override
    public CouchHttpResponse delete(HttpDeleteRequest request) {
        return null;
    }

    @Override
    public URI pageUri(URI currentPageUri, CouchDocument document, boolean reverse) {
        return null;
    }

    @Override
    public URI uri(String... paths) {
        return null;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
