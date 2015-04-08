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

import org.tiogasolutions.couchace.core.api.injectable.CouchInjectables;
import org.tiogasolutions.couchace.core.api.injectable.MissingInjectableResponse;
import org.tiogasolutions.couchace.core.api.injectable.SimpleCouchInjectables;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.http.SslSetup;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;

import java.lang.reflect.Constructor;

/**
 * User: harlan
 * Date: 2/17/14
 * Time: 9:52 AM
 */
public class CouchSetup {

    private Class<? extends CouchHttpClient> httpClientClass;
    private Class<? extends CouchJsonStrategy> jsonStrategyClass;
    private CouchHttpClient httpClient;
    private CouchJsonStrategy jsonStrategy;
    private CouchMetaRepository metaRepository = new CouchMetaRepository();
    private CouchInjectables injectables = new SimpleCouchInjectables();
    private MissingInjectableResponse missingInjectableResponse = MissingInjectableResponse.THROW_EXCEPTION;

    private final String url;
    private String userName;
    private String password;
    private SslSetup sslSetup;

    public CouchSetup(String url) {
        this.url = url;
    }

    public CouchSetup ssl(String keyStoreUrl, String storePass) {
        sslSetup = new SslSetup(keyStoreUrl, storePass);
        return this;
    }

    public CouchSetup(CouchSetup couchSetup) {
        this.url = couchSetup.getUrl();
        this.userName = couchSetup.getUserName();
        this.password = couchSetup.getPassword();
        this.sslSetup = couchSetup.getSslSetup();
        this.httpClientClass = couchSetup.getHttpClientClass();
        this.jsonStrategyClass = couchSetup.getJsonStrategyClass();
        this.httpClient = couchSetup.getHttpClient();
        this.jsonStrategy = couchSetup.getJsonStrategy();
        this.metaRepository = couchSetup.getMetaRepository();
        this.injectables = couchSetup.getInjectables();
        this.missingInjectableResponse = couchSetup.getMissingInjectableResponse();
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public SslSetup getSslSetup() {
        return sslSetup;
    }

    public CouchSetup setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public CouchSetup setPassword(String password) {
        this.password = password;
        return this;
    }

    public CouchSetup setSslSetup(SslSetup sslSetup) {
        this.sslSetup = sslSetup;
        return this;
    }

    public CouchSetup setHttpClient(CouchHttpClient httpClient) {
        this.httpClient = httpClient;
        httpClient.init(this);
        return this;
    }

    public CouchSetup setJsonStrategy(CouchJsonStrategy jsonStrategy) {
        this.jsonStrategy = jsonStrategy;
        jsonStrategy.init(this);
        return this;
    }

    public CouchHttpClient getHttpClient() {
        if (httpClient == null) {
            if (httpClientClass != null) {
                try {
                    Constructor<? extends CouchHttpClient> constructor = httpClientClass.getConstructor();
                    httpClient = constructor.newInstance();
                    httpClient.init(this);
                } catch (Exception e) {
                    throw CouchException.internalServerError("Error creating CouchHttpClient.", e);
                }
            } else {
                throw CouchException.internalServerError("No CouchHttpClient instance or class specified.");
            }
        }
        return httpClient;
    }

    public CouchJsonStrategy getJsonStrategy() {
        if (jsonStrategy == null) {
            if (httpClientClass != null) {
                try {
                    Constructor<? extends CouchJsonStrategy> constructor = jsonStrategyClass.getConstructor();
                    jsonStrategy = constructor.newInstance();
                    jsonStrategy.init(this);
                } catch (Exception e) {
                    throw CouchException.internalServerError("Error creating CouchJsonStrategy.", e);
                }
            } else {
                throw CouchException.internalServerError("No CouchJsonStrategy instance or class specified.");
            }
        }
        return jsonStrategy;
    }

    public CouchSetup setHttpClient(Class<? extends CouchHttpClient> httpClientClass) {
        this.httpClientClass = httpClientClass;
        return this;
    }

    public CouchSetup setJsonStrategy(Class<? extends CouchJsonStrategy> jsonStrategyClass) {
        this.jsonStrategyClass = jsonStrategyClass;
        return this;
    }

    public CouchMetaRepository getMetaRepository() {
        return metaRepository;
    }

    public CouchSetup setMetaRepository(CouchMetaRepository metaRepository) {
        this.metaRepository = metaRepository;
        return this;
    }

    public CouchInjectables getInjectables() {
        return injectables;
    }

    public CouchSetup setInjectables(CouchInjectables injectables) {
        this.injectables = injectables;
        return this;
    }

    public MissingInjectableResponse getMissingInjectableResponse() {
        return missingInjectableResponse;
    }

    public CouchSetup setMissingInjectableResponse(MissingInjectableResponse missingInjectableResponse) {
        this.missingInjectableResponse = missingInjectableResponse;
        return this;
    }

    public Class<? extends CouchHttpClient> getHttpClientClass() {
        return httpClientClass;
    }

    public Class<? extends CouchJsonStrategy> getJsonStrategyClass() {
        return jsonStrategyClass;
    }

}
