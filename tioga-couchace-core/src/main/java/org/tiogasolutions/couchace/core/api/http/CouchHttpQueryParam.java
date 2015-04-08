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

import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

import java.util.Arrays;

/**
 * User: harlan
 * Date: 2/8/14
 * Time: 11:57 AM
 */
public class CouchHttpQueryParam {
    private final String name;
    private final Object[] value;

    public CouchHttpQueryParam(String name, Object value) {
        ArgUtil.assertNotNull(name, "name");
        this.name = name;
        if (value == null) {
            this.value = new Object[0];
        } else {
            this.value = new Object[]{value};
        }
    }

    public CouchHttpQueryParam(String name, Object[] value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouchHttpQueryParam that = (CouchHttpQueryParam) o;

        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(value, that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }
}
