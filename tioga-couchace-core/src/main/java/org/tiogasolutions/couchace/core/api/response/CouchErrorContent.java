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

package org.tiogasolutions.couchace.core.api.response;

import org.tiogasolutions.couchace.core.internal.util.CouchJsonUtil;
import org.tiogasolutions.couchace.core.internal.util.StringUtil;

import java.util.Map;

public final class CouchErrorContent {
    private final String error;
    private final String reason;

    public static final CouchErrorContent noError = new CouchErrorContent("none", "No error");
    public static final CouchErrorContent undefined = new CouchErrorContent("undefined", "No reason specified");

    public static CouchErrorContent parseJson(String json) {
        try {
            if (StringUtil.isBlank(json)) {
                return noError;
            } else if (!json.startsWith("{") || !json.endsWith("}")) {
                return new CouchErrorContent(json, json);
            } else {
                Map<String, String> jsonMap = CouchJsonUtil.parseJson(json);
                String error = jsonMap.get("error");
                String reason = jsonMap.get("reason");
                if (error == null && reason == null) {
                    error = json;
                    reason = json;
                }
                return new CouchErrorContent(error, reason);
            }
        } catch (Throwable e) {
            return new CouchErrorContent("undefined", "Error parsing error content: " + e.getMessage());
        }
    }

    public CouchErrorContent(String error, String reason) {
        this.error = error;
        this.reason = reason;
    }

    public String toJson() {
        return String.format("{\"error\" : \"%s\",\"reason\" : \"%s\"}", error, reason);
    }

    public boolean isNoError() {
        return this.equals(noError);
    }

    public String getError() {
        return error;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouchErrorContent that = (CouchErrorContent) o;

        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = error != null ? error.hashCode() : 0;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CouchErrorContent{" +
            "error='" + error + '\'' +
            ", reason='" + reason + '\'' +
            '}';
    }
}
