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

package org.tiogasolutions.couchace.core.api.json;

import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;

/**
 * User: harlan
 * Date: 2/1/14
 * Time: 4:26 PM
 */
public class CouchJsonException extends CouchException {

    public CouchJsonException(String message) {
        super(CouchHttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public CouchJsonException(CouchHttpStatus statusCode, String message) {
        super(statusCode, message);
    }

    public CouchJsonException(Throwable cause) {
        super(CouchHttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public CouchJsonException(CouchHttpStatus statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public CouchJsonException(String message, Throwable cause) {
        super(CouchHttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public CouchJsonException(CouchHttpStatus statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
    }

}
