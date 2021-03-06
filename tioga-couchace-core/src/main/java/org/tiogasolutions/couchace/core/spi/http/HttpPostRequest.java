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

import org.tiogasolutions.couchace.core.api.http.CouchMethodType;

/**
 * User: harlan
 * Date: 2/8/14
 * Time: 11:04 AM
 */
public class HttpPostRequest extends HttpRequest {
    private final Object content;

    public HttpPostRequest(String path,
                           Object content) {

        super(path, null);
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public CouchMethodType getMethodType() {
        return CouchMethodType.POST;
    }
}
