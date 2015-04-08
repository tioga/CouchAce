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

import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.request.*;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.spi.http.CouchHttpClient;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;

/**
 * User: harlan
 * Date: 2/1/14
 * Time: 11:14 PM
 */
public interface CouchDatabase {

    String getDatabaseName();

    boolean exists();

    CouchDatabaseInfo databaseInfo();

    WriteResponse createDatabase();

    WriteResponse deleteDatabase();

    WriteResponse recreateDatabase();

    HeadRequestFactory head();

    GetRequestFactory get();

    PutRequestFactory put();

    PostRequestFactory post();

    DeleteRequestFactory delete();

    CouchFeatureSet getFeatureSet();

    CouchJsonStrategy getJsonStrategy();

    CouchHttpClient getHttpClient();

    CouchMetaRepository getMetaRepository();

}
