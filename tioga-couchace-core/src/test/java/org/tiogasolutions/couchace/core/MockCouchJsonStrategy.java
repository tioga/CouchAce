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

import org.tiogasolutions.couchace.core.api.CouchDatabaseInfo;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.json.CouchJsonException;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.tiogasolutions.couchace.core.api.request.GetRequestFactory;
import org.tiogasolutions.couchace.core.api.request.PostEntityRequest;
import org.tiogasolutions.couchace.core.api.request.PutEntityRequest;
import org.tiogasolutions.couchace.core.api.response.EntityDocument;
import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;

import java.util.List;

public class MockCouchJsonStrategy implements CouchJsonStrategy {

    @Override
    public void init(CouchSetup couchSetup) {

    }

    @Override
    public CouchDatabaseInfo readDatabaseInfo(String json) {
        return null;
    }

    @Override
    public String createJsonForPut(PutEntityRequest request) {
        return null;
    }

    @Override
    public String createJsonForPost(PostEntityRequest request) {
        return null;
    }

    @Override
    public TextDocument readTextDocument(String json) throws CouchJsonException {
        return null;
    }

    @Override
    public List<TextDocument> readTextDocuments(String json) throws CouchJsonException {
        return null;
    }

    @Override
    public <T> EntityDocument<T> readEntityDocument(GetRequestFactory getRequestFactory, CouchEntityMeta<T> entityMeta, String json) throws CouchJsonException {
        return null;
    }

    @Override
    public <T> List<EntityDocument<T>> readEntityDocuments(GetRequestFactory getRequestFactory, CouchEntityMeta<T> entityMeta, String json) throws CouchJsonException {
        return null;
    }
}
