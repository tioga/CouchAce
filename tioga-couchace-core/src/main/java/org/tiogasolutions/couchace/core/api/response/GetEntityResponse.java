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

import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.query.CouchPageNavigation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 7:41 PM
 */
public class GetEntityResponse<T> extends GetResponse implements Iterable<T> {
    private final Class<?> entityClass;
    private final List<T> entityList;
    private final List<EntityDocument<T>> entityDocumentList;
    private final CouchPageNavigation couchPageNavigation;

    public static <T> GetEntityResponse<T> withError(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, CouchErrorContent errorContent) {
        return new GetEntityResponse<>(uri, statusCode, contentType, entityClass, errorContent);
    }

    public static <T> GetEntityResponse<T> withEntity(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, EntityDocument<T> entity) {
        return new GetEntityResponse<>(uri, statusCode, contentType, entityClass, null, Collections.singletonList(entity));
    }

    public static <T> GetEntityResponse<T> withEntities(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, List<EntityDocument<T>> entities) {
        return new GetEntityResponse<>(uri, statusCode, contentType, entityClass, null, entities);
    }

    public static <T> GetEntityResponse<T> withEntities(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, CouchPageNavigation couchPageNavigation, List<EntityDocument<T>> entities) {
        return new GetEntityResponse<>(uri, statusCode, contentType, entityClass, couchPageNavigation, entities);
    }

    /**
     * Constructor used in case with null entity.
     *
     * @param statusCode
     * @param contentType
     * @param entityClass
     * @param entityDocuments
     */
    private GetEntityResponse(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, CouchPageNavigation couchPageNavigation, List<EntityDocument<T>> entityDocuments) {
        super(uri, statusCode, contentType, null);
        this.entityClass = (entityClass != null) ? entityClass : Object.class;
        if (entityDocuments == null) {
            this.entityDocumentList = Collections.emptyList();
            this.entityList = Collections.emptyList();
        } else {
            this.entityDocumentList = Collections.unmodifiableList(entityDocuments);
            List<T> localEntityList = new ArrayList<>();
            for (EntityDocument<T> entityDocument : entityDocumentList) {
                localEntityList.add(entityDocument.getEntity());
            }
            this.entityList = Collections.unmodifiableList(localEntityList);
        }
        this.couchPageNavigation = couchPageNavigation;
    }

    private GetEntityResponse(URI uri, CouchHttpStatus statusCode, CouchMediaType contentType, Class<T> entityClass, CouchErrorContent errorContent) {
        super(uri, statusCode, contentType, errorContent);
        this.entityClass = (entityClass != null) ? entityClass : Object.class;
        this.entityDocumentList = Collections.emptyList();
        this.entityList = Collections.emptyList();
        this.couchPageNavigation = CouchPageNavigation.empty();
    }

    public List<EntityDocument<T>> getDocumentList() {
        return entityDocumentList;
    }

    @Override
    public Iterator<T> iterator() {
        return entityList.iterator();
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public T getFirstEntity() {
        if (entityList.size() == 0) {
            throw new IllegalStateException("Attempting to get firstEntity from empty GetEntityResponse.");
        }
        return entityList.get(0);
    }

    public List<T> getEntityList() {
        return entityList;
    }
    public boolean isEmpty() {
        return getDocumentList().isEmpty();
    }

    public boolean isNotEmpty() {
        return !getDocumentList().isEmpty();
    }

    public int getSize() {
        return getDocumentList().size();
    }

    public EntityDocument<T> getFirstDocument() {
        return getDocumentList().get(0);
    }

    public CouchPageNavigation getCouchPageNavigation() {
        return couchPageNavigation;
    }

}
