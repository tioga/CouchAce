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

/**
 * User: harlan
 * Date: 3/3/12
 * Time: 11:26 AM
 */
public enum CouchHttpStatus {

    /**
     * {@code 100 Continue}.
     */
    UNDEFINED(-1, "Undefined"),

    /**
     * {@code 100 Continue}.
     */
    CONTINUE(100, "Continue"),
    /**
     * {@code 101 Switching Protocols}.
     */
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    /**
     * {@code 102 Processing}.
     */
    PROCESSING(102, "Processing"),

    /**
     * {@code 200 OK}.
     */
    OK(200, "OK"),
    /**
     * {@code 201 Created}.
     */
    CREATED(201, "Created"),
    /**
     * {@code 202 Accepted}.
     */
    ACCEPTED(202, "Accepted"),
    /**
     * {@code 203 Non-Authoritative Information}.
     */
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    /**
     * {@code 204 No Content}.
     */
    NO_CONTENT(204, "No Content"),
    /**
     * {@code 205 Reset Content}.
     */
    RESET_CONTENT(205, "Reset Content"),
    /**
     * {@code 206 Partial Content}.
     */
    PARTIAL_CONTENT(206, "Partial Content"),
    /**
     * {@code 207 Multi-Status}.
     */
    MULTI_STATUS(207, "Multi-Status"),
    /**
     * {@code 208 Already Reported}.
     */
    ALREADY_REPORTED(208, "Already Reported"),
    /**
     * {@code 226 IM Used}.
     */
    IM_USED(226, "IM Used"),

    // 3xx Redirection
    /**
     * {@code 300 Multiple Choices}.
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    /**
     * {@code 301 Moved Permanently}.
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    /**
     * {@code 302 Found}.
     */
    FOUND(302, "Found"),
    /**
     * {@code 303 See Other}.
     */
    SEE_OTHER(303, "See Other"),
    /**
     * {@code 304 Not Modified}.
     */
    NOT_MODIFIED(304, "Not Modified"),
    /**
     * {@code 305 Use Proxy}.
     */
    USE_PROXY(305, "Use Proxy"),
    /**
     * {@code 307 Temporary Redirect}.
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * {@code 400 Bad Request}.
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * {@code 401 Unauthorized}.
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * {@code 402 Payment Required}.
     */
    PAYMENT_REQUIRED(402, "Payment Required"),
    /**
     * {@code 403 Forbidden}.
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * {@code 404 Not Found}.
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * {@code 405 Method Not Allowed}.
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    /**
     * {@code 406 Not Acceptable}.
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    /**
     * {@code 407 Proxy Authentication Required}.
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    /**
     * {@code 408 Request Timeout}.
     */
    REQUEST_TIMEOUT(408, "Request Time-out"),
    /**
     * {@code 409 Conflict}.
     */
    CONFLICT(409, "Conflict"),
    /**
     * {@code 410 Gone}.
     */
    GONE(410, "Gone"),
    /**
     * {@code 411 Length Required}.
     */
    LENGTH_REQUIRED(411, "Length Required"),
    /**
     * {@code 412 Precondition failed}.
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),
    /**
     * {@code 413 Request Entity Too Large}.
     */
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    /**
     * {@code 414 Request-URI Too Long}.
     */
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Large"),
    /**
     * {@code 415 Unsupported Media Type}.
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    /**
     * {@code 416 Requested Range Not Satisfiable}.
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
    /**
     * {@code 417 Expectation Failed}.
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),
    /**
     * {@code 419 Insufficient Space on Resource}.
     */
    INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
    /**
     * {@code 420 Method Failure}.
     */
    METHOD_FAILURE(420, "Method Failure"),
    /**
     * {@code 421 Destination Locked}.
     */
    DESTINATION_LOCKED(421, "Destination Locked"),
    /**
     * {@code 422 Unprocessable Entity}.
     */
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    /**
     * {@code 423 Locked}.
     */
    LOCKED(423, "Locked"),
    /**
     * {@code 424 Failed Dependency}.
     */
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    /**
     * {@code 426 Upgrade Required}.
     */
    UPGRADE_REQUIRED(426, "Upgrade Required"),

    // 5xx Server Error
    /**
     * {@code 500 Internal Server Error}.
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * {@code 501 Not Implemented}.
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),
    /**
     * {@code 502 Bad Gateway}.
     */
    BAD_GATEWAY(502, "Bad Gateway"),
    /**
     * {@code 503 Service Unavailable}.
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    /**
     * {@code 504 Gateway Timeout}.
     */
    GATEWAY_TIMEOUT(504, "Gateway Time-out"),
    /**
     * {@code 505 HTTP Version Not Supported}.
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
    /**
     * {@code 506 Variant Also Negotiates}
     */
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    /**
     * {@code 507 Insufficient Storage}
     */
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    /**
     * {@code 508 Loop Detected}
     */
    LOOP_DETECTED(508, "Loop Detected"),
    /**
     * {@code 510 Not Extended}
     */
    NOT_EXTENDED(510, "Not Extended");

    private final int code;
    private final String reason;

    private CouchHttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public boolean isOk() {
        return this == OK;
    }

    public boolean isCreated() {
        return this == CREATED;
    }

    public boolean isNotFound() {
        return this == NOT_FOUND;
    }

    public boolean isConflict() {
        return this == CONFLICT;
    }

    public boolean isInformational() {
        return code / 100 == 1;
    }

    public boolean isSuccess() {
        return code / 100 == 2;
    }

    public boolean isRedirect() {
        return code / 100 == 3;
    }

    public boolean isError() {
        return code / 100 > 3;
    }

    public boolean isClientError() {
        return code / 100 == 4;
    }

    public boolean isServerError() {
        return code / 100 == 5;
    }

    public boolean isOther() {
        return code / 100 == 6;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public boolean isUndefined() {
        return this == UNDEFINED;
    }

    public boolean isOkOrCreated() {
        return this == OK || this == CREATED;
    }

    public boolean isUnauthorized() {
        return this == UNAUTHORIZED;
    }

    public static CouchHttpStatus findByCode(int code) {
        for (CouchHttpStatus statusCode : values()) {
            if (statusCode.code == code) {
                return statusCode;
            }
        }
        return UNDEFINED;
    }

}
