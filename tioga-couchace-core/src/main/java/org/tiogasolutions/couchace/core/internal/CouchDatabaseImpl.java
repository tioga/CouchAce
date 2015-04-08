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

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.request.*;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpResponse;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.http.HttpGetRequest;
import org.tiogasolutions.couchace.core.spi.http.HttpHeadRequest;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.couchace.core.api.CouchDatabaseInfo;
import org.tiogasolutions.couchace.core.api.CouchException;

/**
 * User: harlan
 * Date: 9/17/12
 * Time: 8:09 PM
 */
public class CouchDatabaseImpl implements CouchDatabase {
    private static final Logger log = LoggerFactory.getLogger(CouchDatabaseImpl.class);
    private final String databaseName;
    private final CouchJsonStrategy jsonStrategy;
    private final CouchHttpClient httpClient;
    private final CouchFeatureSet featureSet;

    private final GetRequestFactory getRequestFactory;
    private final PutRequestFactory putRequestFactory;
    private final PostRequestFactory postRequestFactory;
    private final HeadRequestFactory headRequestFactory;
    private final DeleteRequestFactory deleteRequestFactory;
    private final CouchMetaRepository metaRepository;

    public CouchDatabaseImpl(String databaseName, CouchHttpClient httpClient, CouchJsonStrategy jsonStrategy, CouchMetaRepository metaRepository, CouchFeatureSet featureSet) {
        ArgUtil.assertNotNull(databaseName, "databaseName");
        ArgUtil.assertNotNull(httpClient, "httpClient");
        ArgUtil.assertNotNull(jsonStrategy, "jsonStrategy");
        this.databaseName = databaseName;
        this.httpClient = httpClient;
        this.jsonStrategy = jsonStrategy;
        this.metaRepository = metaRepository;
        this.featureSet = featureSet;

        // Create RequestExecutor and request factories
        RequestExecutor requestExecutor = new RequestExecutor(this);
        this.getRequestFactory = new GetRequestFactory(requestExecutor);
        this.putRequestFactory = new PutRequestFactory(requestExecutor);
        this.postRequestFactory = new PostRequestFactory(requestExecutor);
        this.headRequestFactory = new HeadRequestFactory(requestExecutor);
        this.deleteRequestFactory = new DeleteRequestFactory(requestExecutor);
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public boolean exists() {
        HttpHeadRequest httpHeadRequest = new HttpHeadRequest(databaseName, null, null);
        CouchHttpResponse couchHttpResponse = httpClient.head(httpHeadRequest);
        return couchHttpResponse.isOk();
    }

    @Override
    public CouchDatabaseInfo databaseInfo() {
        HttpGetRequest httpGetRequest = new HttpGetRequest(databaseName, null, null, CouchMediaType.APPLICATION_JSON);
        CouchHttpResponse couchHttpResponse = httpClient.get(httpGetRequest);
        if (couchHttpResponse.isOk()) {
            return jsonStrategy.readDatabaseInfo(couchHttpResponse.getStringContent());
        } else if (couchHttpResponse.isNotFound()) {
            throw CouchException.notFound("Database " + databaseName + " does not exist");
        } else {
            throw new CouchException(couchHttpResponse.getHttpStatus(), "Error reading database info for database " + databaseName);
        }
    }

    @Override
    public WriteResponse createDatabase() {
        CouchHttpResponse httpResponse =  httpClient.createDatabase(databaseName);
        return new WriteResponse(httpResponse);
    }

    @Override
    public WriteResponse deleteDatabase() {
        if (featureSet.isTrue(CouchFeature.ALLOW_DB_DELETE)) {
            CouchHttpResponse httpResponse = httpClient.deleteDatabase(databaseName);
            return new WriteResponse(httpResponse);
        } else {
            throw CouchException.forbidden("Deletion of database " + databaseName + " is not allowed. To allow configure with CouchFeature.ALLOW_DB_DELETE.");
        }
    }

    @Override
    public WriteResponse recreateDatabase() {
        // If the database exists, delete it
        if (exists()) {
            // Database already exists, delete it so we can create it.
            deleteDatabase();
        }

        // Now create it
        return createDatabase();
    }

    @Override
    public HeadRequestFactory head() {
        return headRequestFactory;
    }

    @Override
    public GetRequestFactory get() {
        return getRequestFactory;
    }

    @Override
    public PutRequestFactory put() {
        return putRequestFactory;
    }

    @Override
    public PostRequestFactory post() {
        return postRequestFactory;
    }

    @Override
    public DeleteRequestFactory delete() {
        return deleteRequestFactory;
    }

    @Override
    public CouchFeatureSet getFeatureSet() {
        return featureSet;
    }

    @Override
    public CouchJsonStrategy getJsonStrategy() {
        return jsonStrategy;
    }

    @Override
    public CouchHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public CouchMetaRepository getMetaRepository() {
        return metaRepository;
    }
}
