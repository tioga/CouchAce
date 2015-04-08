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

package org.tiogasolutions.couchace.core.api.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Harlan
 * Date: 3/14/2014
 * Time: 9:52 AM
 */
public class CouchMetaRepository {

    private final Map<String, CouchEntityMeta<?>> entityMetaMap = new HashMap<>();
    private final CouchMetaBuilder metaBuilder;

    public CouchMetaRepository(CouchMetaBuilder metaBuilder) {
        this.metaBuilder = metaBuilder;
    }

    public CouchMetaRepository() {
        this.metaBuilder = new AnnotationCouchMetaBuilder();
    }

    public <T> CouchEntityMeta<T> getEntityMeta(Class<T> type) {
        String name = type.getName();
        //noinspection unchecked
        return (CouchEntityMeta<T>) entityMetaMap.get(name);
    }

    public <T> CouchEntityMeta<T> getOrCreateEntityMeta(Class<T> type) {
        CouchEntityMeta<T> entityMeta = getEntityMeta(type);
        if (entityMeta == null) {
            entityMeta = createEntityMeta(type);
            putEntityMeta(entityMeta);
        }
        return entityMeta;
    }

    public void putEntityMeta(CouchEntityMeta entityMeta) {
        entityMetaMap.put(entityMeta.getMetaName(), entityMeta);
    }

    public CouchEntityMeta removeEntityMeta(String metaName) {
        return entityMetaMap.remove(metaName);
    }

    protected <T> CouchEntityMeta<T> createEntityMeta(Class<T> type) {
        return metaBuilder.buildEntityMeta(type);
    }

    protected Map<String, CouchEntityMeta<?>> getEntityMetaMap() {
        return entityMetaMap;
    }
}
