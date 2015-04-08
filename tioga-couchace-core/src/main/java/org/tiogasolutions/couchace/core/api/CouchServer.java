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

package org.tiogasolutions.couchace.core.api;

import org.tiogasolutions.couchace.core.api.http.CouchHttpException;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpResponse;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.couchace.core.internal.CouchDatabaseImpl;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.http.HttpGetRequest;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;

/**
 * User: harlan
 * Date: 2/10/14
 * Time: 1:47 PM
 */
public class CouchServer {
    private final CouchHttpClient httpClient;
    private final CouchJsonStrategy jsonStrategy;
    private final CouchMetaRepository metaRepository;

    public CouchServer(CouchSetup couchSetup) {
        this.httpClient = couchSetup.getHttpClient();
        this.jsonStrategy = couchSetup.getJsonStrategy();
        this.metaRepository = couchSetup.getMetaRepository();
    }

    public void assertConnection() throws CouchHttpException {
        CouchHttpResponse response;
        try {
            response = httpClient.get(new HttpGetRequest());
        } catch (Throwable e) {
            throw CouchHttpException.internalServerError(e);
        }
        if (response.isOk()) {
            String content = response.getStringContent();
            if (!content.contains("couchdb")) {
                String msg = String.format("The server at " + httpClient.getBaseUrl() + " is not a couch database.");
                throw new CouchHttpException(CouchHttpStatus.CONFLICT, msg);
            }
        } else {
            throw new CouchHttpException(response.getHttpStatus(), response.getErrorReason());
        }

    }

    public CouchDatabase database(String databaseName) {
        return new CouchDatabaseImpl(databaseName, httpClient, jsonStrategy, metaRepository, CouchFeatureSet.empty());
    }

    public CouchDatabase database(String databaseName, CouchFeatureSet couchFeatureSet) {
        return new CouchDatabaseImpl(databaseName, httpClient, jsonStrategy, metaRepository, couchFeatureSet);
    }

}
