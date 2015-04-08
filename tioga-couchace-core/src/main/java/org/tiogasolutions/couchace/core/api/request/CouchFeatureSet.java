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

package org.tiogasolutions.couchace.core.api.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CouchFeatureSet {

    public static class CouchFeatureSetBuilder {
        private final Map<CouchFeature, Object> featureMap = new HashMap<>();

        public CouchFeatureSetBuilder add(CouchFeature feature, Object value) {
            featureMap.put(feature, value);
            return this;
        }

        public CouchFeatureSet build() {
            return new CouchFeatureSet(featureMap);
        }
    }

    public static CouchFeatureSetBuilder builder() {
        return new CouchFeatureSetBuilder();
    }

    private static final CouchFeatureSet EMPTY = new CouchFeatureSet(null);

    public static CouchFeatureSet empty() {
        return EMPTY;
    }

    private final Map<CouchFeature, Object> featureMap;

    public CouchFeatureSet(Map<CouchFeature, ?> featureMap) {
        if (featureMap != null) {
            this.featureMap = Collections.unmodifiableMap(featureMap);
        } else {
            this.featureMap = Collections.emptyMap();
        }
    }

    public boolean containsFeature(CouchFeature feature) {
        return featureMap.containsKey(feature);
    }

    public Object getValue(CouchFeature feature) {
        return featureMap.get(feature);
    }

    public String getString(CouchFeature feature) {
        Object value = featureMap.get(feature);
        return (value != null) ? value.toString() : null;
    }

    public Boolean getBoolean(CouchFeature feature) {
        Object value = featureMap.get(feature);
        return (value != null && value instanceof Boolean) ? (Boolean) value : null;
    }

    public boolean isTrue(CouchFeature feature) {
        Boolean val = getBoolean(feature);
        return (val != null && val);
    }

    public boolean isValue(CouchFeature feature, Object value) {
        Object val = getValue(feature);
        if (value == null) {
            return val == null;
        } else {
            return value.equals(val);
        }
    }

    public Map<CouchFeature, Object> getFeatureMap() {
        return featureMap;
    }
}
