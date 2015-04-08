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

package org.tiogasolutions.couchace.core.api.query;

public class CouchJsonKey {
    private final String jsonValue;

    public CouchJsonKey(Object... values) {
        this.jsonValue = valuesAsJson(values);
    }

    public String getJsonValue() {
        return jsonValue;
    }

    private static String valuesAsJson(Object... values) {
        if (values == null || values.length == 0) {
            return null;
        } else if (values.length == 1) {
            return singleValueAsJson(values[0]);
        } else {
            StringBuilder sb = new StringBuilder("[");
            for (Object value : values) {
                sb.append(singleValueAsJson(value));
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        }
    }

    private static String singleValueAsJson(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof CouchJsonKey) {
            return ((CouchJsonKey) value).getJsonValue();
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return value.toString();
        } else {
            return String.format("\"%s\"", value.toString());
        }
    }

}
