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

import org.tiogasolutions.couchace.core.api.http.CouchHttpQuery;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

/**
 * User: harlan
 * Date: 2/8/14
 * Time: 6:46 PM
 */
public abstract class HttpRequest {
    private final String path;
    private final CouchHttpQuery httpQuery;

    public abstract CouchMethodType getMethodType();

    protected HttpRequest(String path, CouchHttpQuery httpQuery) {
        ArgUtil.assertNotEmpty(path, "path");
        this.path = path;
        this.httpQuery = (httpQuery == null) ? CouchHttpQuery.EMPTY : httpQuery;
    }

    protected HttpRequest() {
        this.path = null;
        this.httpQuery = CouchHttpQuery.EMPTY;
    }

    public String getPath() {
        return path;
    }

    public CouchHttpQuery getHttpQuery() {
        return httpQuery;
    }
}
