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

import org.tiogasolutions.couchace.core.api.query.CouchPageQuery;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 2/20/14
 * Time: 9:52 AM
 */
public class HeadRequestFactory {

    private final RequestExecutor requestExecutor;

    public HeadRequestFactory(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public HeadRequest id(String documentId) {
        return new HeadRequest(requestExecutor, documentId, null);
    }

    public HeadRequest id(String documentId, String revision) {
        return new HeadRequest(requestExecutor, documentId, revision);
    }

    public HeadRequest view(CouchViewQuery viewQuery) {
        return new HeadRequest(requestExecutor, viewQuery);
    }

    public HeadRequest page(CouchPageQuery pageQuery) {
        return new HeadRequest(requestExecutor, pageQuery);
    }

}
