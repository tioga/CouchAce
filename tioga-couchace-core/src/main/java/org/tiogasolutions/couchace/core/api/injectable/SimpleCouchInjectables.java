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

package org.tiogasolutions.couchace.core.api.injectable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Harlan
 * Date: 7/23/2014
 * Time: 10:31 PM
 */
public class SimpleCouchInjectables implements CouchInjectables {
    private final Map<String, Object> valueMap = new HashMap<>();

    public SimpleCouchInjectables() {
    }

    public SimpleCouchInjectables(Map<String, Object> map) {
        valueMap.putAll(map);
    }

    public SimpleCouchInjectables addValue(String name, Object value) {
        valueMap.put(name, value);
        return this;
    }

    public SimpleCouchInjectables addValue(Class type, Object value) {
        return addValue(type.getName(), value);
    }

    public SimpleCouchInjectables addValue(Object value) {
      return addValue(value.getClass().getName(), value);
    }

    @Override
    public boolean hasValue(String key) {
        return valueMap.containsKey(key);
    }

    @Override
    public Object getValue(String key) {
        return valueMap.get(key);
    }
}
