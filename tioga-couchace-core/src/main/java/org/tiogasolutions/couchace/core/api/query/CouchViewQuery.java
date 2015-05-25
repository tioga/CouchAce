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

import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

public class CouchViewQuery {
    private final String designName;
    private final String viewName;
    private final CouchJsonKey startKey;
    private final CouchJsonKey endKey;
    private final CouchJsonKey key;
    private final int limit;
    private final int skip;
    private boolean descending = false;
    private boolean includeDocs = true;
    private boolean group = false;

    public static CouchViewQueryBuilder builder(String designName, String viewName) {
        return new CouchViewQueryBuilder(designName, viewName);
    }

    public CouchViewQuery(String designName,
                          String viewName,
                          int limit,
                          int skip,
                          CouchJsonKey startKey,
                          CouchJsonKey endKey,
                          CouchJsonKey key,
                          boolean descending,
                          boolean includeDocs,
                          boolean group) {
        ArgUtil.assertNotNull(designName, "designName");
        ArgUtil.assertNotNull(viewName, "viewName");
        this.designName = designName;
        this.viewName = viewName;
        this.startKey = startKey;
        this.endKey = endKey;
        this.key = key;
        this.limit = limit;
        this.skip = skip;
        this.descending = descending;
        this.includeDocs = includeDocs;
        this.group = group;
    }

    public String getDesignName() {
        return designName;
    }

    public String getViewName() {
        return viewName;
    }

    public boolean hasStartKey() {
        return startKey != null;
    }

    public CouchJsonKey getStartKey() {
        return startKey;
    }

    public String getStartKeyJson() {
        return startKey.getJsonValue();
    }

    public boolean hasEndKey() {
        return endKey != null;
    }

    public CouchJsonKey getEndKey() {
        return endKey;
    }

    public String getEndKeyJson() {
        return endKey.getJsonValue();
    }

    public boolean hasKey() {
        return key != null;
    }

    public CouchJsonKey getKey() {
        return key;
    }

    public String getKeyJson() {
        return key.getJsonValue();
    }

    public int getLimit() {
        return limit;
    }

    public int getSkip() {
        return skip;
    }

    public boolean isDescending() {
        return descending;
    }

    public boolean isGroup() {
        return group;
    }

    // TODO - why is this mutable
    public CouchViewQuery setDescending(boolean descending) {
        this.descending = descending;
        return this;
    }

    public boolean isIncludeDocs() {
        return includeDocs;
    }

    // TODO - why is this mutable
    public CouchViewQuery setIncludeDocs(boolean includeDocs) {
        this.includeDocs = includeDocs;
        return this;
    }

    private static CouchJsonKey toJsonKey(Object... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        } else if (keys.length == 1 && keys[0] instanceof CouchJsonKey) {
            return (CouchJsonKey) keys[0];
        } else {
            return new CouchJsonKey(keys);
        }
    }

    public static class CouchViewQueryBuilder {

        private final String designName;
        private final String viewName;
        private CouchJsonKey startKey;
        private CouchJsonKey endKey;
        private CouchJsonKey key;
        private int limit;
        private int skip;
        private boolean descending = false;
        private boolean includeDocs = true;
        private boolean group = false;

        public CouchViewQueryBuilder(String designName, String viewName) {
            this.designName = designName;
            this.viewName = viewName;
        }

        public CouchViewQuery build() {
            return new CouchViewQuery(designName, viewName, limit, skip, startKey, endKey, key, descending, includeDocs, group);
        }

        public CouchViewQueryBuilder key(Object... key) {
            this.key = toJsonKey(key);
            return this;
        }

        public CouchViewQueryBuilder start(Object... startKey) {
            this.startKey = toJsonKey(startKey);
            return this;
        }

        public CouchViewQueryBuilder end(Object... endKey) {
            this.endKey = toJsonKey(endKey);
            return this;
        }

        public CouchViewQueryBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public CouchViewQueryBuilder skip(int skip) {
            this.skip = skip;
            return this;
        }

        public CouchViewQueryBuilder descending(boolean descending) {
            this.descending = descending;
            return this;
        }

        public CouchViewQueryBuilder includeDocs(boolean includeDocs) {
            this.includeDocs = includeDocs;
            return this;
        }

        public CouchViewQueryBuilder group(boolean group) {
            this.group = group;
            return this;
        }

    }

}
