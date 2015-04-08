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

import org.tiogasolutions.couchace.core.api.query.CouchJsonKey;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:32 PM
 */
public final class EntityDocument<T> implements CouchDocument<T> {
    private final String documentId;
    private final String documentRevision;
    private final CouchJsonKey key;
    private final String entityType;
    private final Class<?> entityClass;
    private final T entity;

    public EntityDocument(String documentId,
                          String documentRevision,
                          CouchJsonKey key,
                          String entityType,
                          T entity) {
        this.documentId = documentId;
        this.documentRevision = documentRevision;
        this.key = key;
        this.entityType = entityType;
        this.entity = entity;
        this.entityClass = (entity != null) ? entity.getClass() : Object.class;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public String getDocumentRevision() {
        return documentRevision;
    }

    @Override
    public CouchJsonKey getKey() {
        return key;
    }

    public String getEntityType() {
        return entityType;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public T getContent() {
        return entity;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityDocument that = (EntityDocument) o;

        if (documentId != null ? !documentId.equals(that.documentId) : that.documentId != null) return false;
        if (documentRevision != null ? !documentRevision.equals(that.documentRevision) : that.documentRevision != null)
            return false;
        if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;
        if (!entityClass.equals(that.entityClass)) return false;
        if (entityType != null ? !entityType.equals(that.entityType) : that.entityType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = documentId != null ? documentId.hashCode() : 0;
        result = 31 * result + (documentRevision != null ? documentRevision.hashCode() : 0);
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0);
        result = 31 * result + entityClass.hashCode();
        result = 31 * result + (entity != null ? entity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EntityDocument{" +
            "documentId='" + documentId + '\'' +
            ", documentRevision='" + documentRevision + '\'' +
            ", key=" + key +
            ", entityType='" + entityType + '\'' +
            ", entityClass=" + entityClass +
            ", entity=" + entity +
            '}';
    }
}
