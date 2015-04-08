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

import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;

/**
 * User: harlan
 * Date: 3/3/12
 * Time: 1:34 AM
 */
public class CouchException extends RuntimeException {
    private final CouchHttpStatus statusCode;

    public static CouchException conflict(String message) {
        return new CouchException(CouchHttpStatus.CONFLICT, message);
    }

    public static CouchException conflict(Throwable ex) {
        return new CouchException(CouchHttpStatus.CONFLICT, ex);
    }

    public static CouchException conflict(String message, Throwable ex) {
        return new CouchException(CouchHttpStatus.CONFLICT, message, ex);
    }

    public static CouchException notFound(String message) {
        return new CouchException(CouchHttpStatus.NOT_FOUND, message);
    }

    public static CouchException notFound(Throwable ex) {
        return new CouchException(CouchHttpStatus.NOT_FOUND, ex);
    }

    public static CouchException notFound(String message, Throwable ex) {
        return new CouchException(CouchHttpStatus.NOT_FOUND, message, ex);
    }

    public static CouchException badRequest(String message) {
        return new CouchException(CouchHttpStatus.BAD_REQUEST, message);
    }

    public static CouchException internalServerError(Throwable ex) {
        return new CouchException(CouchHttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    public static CouchException internalServerError(String message) {
        return new CouchException(CouchHttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static CouchException internalServerError(String message, Throwable ex) {
        return new CouchException(CouchHttpStatus.INTERNAL_SERVER_ERROR, message, ex);
    }

    public static CouchException serviceUnavailable(String message, Throwable ex) {
        return new CouchException(CouchHttpStatus.SERVICE_UNAVAILABLE, message, ex);
    }

    public static CouchException serviceUnavailable(String message) {
        return new CouchException(CouchHttpStatus.SERVICE_UNAVAILABLE, message);
    }

    public static CouchException serviceUnavailable(Throwable ex) {
        return new CouchException(CouchHttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    public static CouchException forbidden(String message, Throwable ex) {
        return new CouchException(CouchHttpStatus.FORBIDDEN, message, ex);
    }

    public static CouchException forbidden(String message) {
        return new CouchException(CouchHttpStatus.FORBIDDEN, message);
    }

    public static CouchException forbidden(Throwable ex) {
        return new CouchException(CouchHttpStatus.FORBIDDEN, ex);
    }

    public CouchException(CouchHttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public CouchException(CouchHttpStatus statusCode) {
        this(statusCode, statusCode.getReason());
    }

    public CouchException(CouchHttpStatus statusCode, String message, Throwable throwable) {
        super(message, throwable);
        this.statusCode = statusCode;
    }

    public CouchException(CouchHttpStatus statusCode, Throwable throwable) {
        super(throwable);
        this.statusCode = statusCode;
    }

    public CouchException(int statusCode) {
        this(CouchHttpStatus.findByCode(statusCode));
    }

    public CouchException(int statusCode, String message) {
        this(CouchHttpStatus.findByCode(statusCode), message);
    }

    public CouchException(int statusCode, String message, Throwable throwable) {
        this(CouchHttpStatus.findByCode(statusCode), message, throwable);
    }

    public CouchException(int statusCode, Throwable throwable) {
        this(CouchHttpStatus.findByCode(statusCode), throwable);
    }

    public CouchHttpStatus getHttpStatus() {
        return statusCode;
    }

    public int getHttpStatusCode() {
        return statusCode.getCode();
    }

}
