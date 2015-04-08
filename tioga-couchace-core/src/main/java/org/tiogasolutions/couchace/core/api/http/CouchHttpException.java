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

package org.tiogasolutions.couchace.core.api.http;

import org.tiogasolutions.couchace.core.api.CouchException;

/**
 * User: harlan
 * Date: 2/1/14
 * Time: 4:26 PM
 */
public class CouchHttpException extends CouchException {

    public static CouchHttpException internalServerError(Throwable ex) {
        return new CouchHttpException(CouchHttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    public static CouchHttpException internalServerError(String message) {
        return new CouchHttpException(CouchHttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static CouchHttpException internalServerError(String message, Throwable ex) {
        return new CouchHttpException(CouchHttpStatus.INTERNAL_SERVER_ERROR, message, ex);
    }

    public static CouchHttpException serviceUnavailable(String message, Throwable ex) {
        return new CouchHttpException(CouchHttpStatus.SERVICE_UNAVAILABLE, message, ex);
    }

    public static CouchHttpException serviceUnavailable(String message) {
        return new CouchHttpException(CouchHttpStatus.SERVICE_UNAVAILABLE, message);
    }

    public static CouchHttpException serviceUnavailable(Throwable ex) {
        return new CouchHttpException(CouchHttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    public CouchHttpException(CouchHttpStatus statusCode, String message) {
        super(statusCode, message);
    }

    public CouchHttpException(CouchHttpStatus statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public CouchHttpException(CouchHttpStatus statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
    }

}
