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

package org.tiogasolutions.couchace.core.internal.util;

import java.util.HashMap;
import java.util.Map;

public class CouchJsonUtil {

    /**
     *
     * @param json -
     * @return Map
     */
    public static Map<String, String> parseJson(String json) {
        String[] result = json.split(",|\\{|\\}");
        Map<String, String> jsonMap = new HashMap<>();
        for (String r : result) {
            String[] keyAndValue = r.split(":");

            String key = null;
            if (keyAndValue.length > 0 && keyAndValue[0] != null) {
                key = keyAndValue[0].replace("\"", "").trim();
                key = (key.isEmpty()) ? null : key;
            }
            String value = null;
            if (key != null && keyAndValue.length > 1 && keyAndValue[1] != null) {
                value = keyAndValue[1].replace("\"", "").trim();
            }
            if (key != null) {
                jsonMap.put(key, value);
            }
        }
        return jsonMap;
    }
}
