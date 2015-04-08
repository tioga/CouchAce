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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: harlan
 * Date: 7/27/14
 * Time: 4:55 PM
 */
public class CouchHttpQuery implements Iterable<CouchHttpQueryParam> {
    public static CouchHttpQuery EMPTY = new CouchHttpQuery(null);

    public static Builder Builder() {
        return new Builder();
    }

    private List<CouchHttpQueryParam> queryParams = new ArrayList<>();

    private CouchHttpQuery(List<CouchHttpQueryParam> queryParams) {
        if (queryParams == null) {
            this.queryParams = Collections.emptyList();
        } else {
            this.queryParams = Collections.unmodifiableList(new ArrayList<>(queryParams));
        }
    }

    public List<CouchHttpQueryParam> toList() {
        return queryParams;
    }

    @Override
    public Iterator<CouchHttpQueryParam> iterator() {
        return queryParams.iterator();
    }

    public static class Builder {
        private List<CouchHttpQueryParam> queryParams = new ArrayList<>();

        public Builder add(String key, Object value) {
            queryParams.add(new CouchHttpQueryParam(key, value));
            return this;
        }

        public CouchHttpQuery build() {
            return new CouchHttpQuery(queryParams);
        }

    }

}
