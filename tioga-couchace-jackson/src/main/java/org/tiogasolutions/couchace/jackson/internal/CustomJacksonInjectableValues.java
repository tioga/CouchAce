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

package org.tiogasolutions.couchace.jackson.internal;

import org.tiogasolutions.couchace.core.api.injectable.CouchInjectables;
import org.tiogasolutions.couchace.core.api.injectable.MissingInjectableResponse;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;

import java.util.HashMap;
import java.util.Map;

public class CustomJacksonInjectableValues extends InjectableValues {
    private final MissingInjectableResponse missingInjectableResponse;

    private final Map<String, Object> values = new HashMap<>();

    private CouchInjectables injectables;

    public CustomJacksonInjectableValues(MissingInjectableResponse missingInjectableResponse) {
        this.missingInjectableResponse = missingInjectableResponse;
    }

    public CustomJacksonInjectableValues(MissingInjectableResponse missingInjectableResponse, CouchInjectables injectables) {
        this.missingInjectableResponse = missingInjectableResponse;
        this.injectables = injectables;
    }

    @Override
    public Object findInjectableValue(Object valueId, DeserializationContext context, BeanProperty forProperty, Object beanInstance) {
        if (!(valueId instanceof String)) {
            String type = (valueId == null) ? "[null]" : valueId.getClass().getName();
            throw new IllegalArgumentException("Unrecognized inject value id type (" + type + "), expecting String");
        }
        String key = (String) valueId;

        if (values.containsKey(key)) {
            return values.get(key);
        } else if (injectables != null && injectables.hasValue(key)) {
            return injectables.getValue(key);
        } else if (missingInjectableResponse == MissingInjectableResponse.THROW_EXCEPTION) {
            throw new IllegalArgumentException("No injectable value found for key '" + key +  forProperty.getName() + "'");
        } else {
            return null;
        }
    }

    public CustomJacksonInjectableValues addValue(String key, Object value) {
        values.put(key, value);
        return this;
    }

}
