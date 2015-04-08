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

import org.tiogasolutions.couchace.core.internal.RequestExecutor;

/**
 * User: harlan
 * Date: 2/20/14
 * Time: 9:52 AM
 */
public class PostRequestFactory {

    private final RequestExecutor requestExecutor;

    public PostRequestFactory(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public PostDocumentRequest document(String content) {
        return new PostDocumentRequest(requestExecutor, content);
    }

    public PostEntityRequest entity(String entityType, Object entity) {
        return new PostEntityRequest(requestExecutor, entityType, entity);
    }

    public PostDatabaseRequest database(String path, String content) {
        return new PostDatabaseRequest(requestExecutor, path, content);
    }

    public PostDatabaseRequest database(String path) {
        return new PostDatabaseRequest(requestExecutor, path, null);
    }

}
