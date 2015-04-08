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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.json.CouchJsonException;

import java.util.Collections;
import java.util.List;

public class CouchEntityMeta<T> {
    private final String metaName;
    private final Class<T> entityClass;
    private final String entityType;
    private final ValueAccessor idValueAccessor;
    private final String idPattern;
    private final ValueAccessor revisionValueAccessor;
    private final ValueAccessor attachmentInfoMapAccessor;
    private final List<CouchEmbeddedAttachmentMeta> embeddedAttachmentMetaList;

    public CouchEntityMeta(String metaName,
                           Class<T> entityClass,
                           String entityType,
                           ValueAccessor idValueAccessor,
                           String idPattern,
                           ValueAccessor revisionValueAccessor,
                           ValueAccessor attachmentInfoMapAccessor,
                           List<CouchEmbeddedAttachmentMeta> embeddedAttachmentMetaList) {

        this.metaName = metaName;
        this.entityClass = entityClass;
        this.entityType = entityType;
        this.idValueAccessor = idValueAccessor;
        this.idPattern = idPattern;
        this.revisionValueAccessor = revisionValueAccessor;
        this.attachmentInfoMapAccessor = attachmentInfoMapAccessor;
        this.embeddedAttachmentMetaList = (embeddedAttachmentMetaList != null) ? Collections.unmodifiableList(embeddedAttachmentMetaList) : Collections.<CouchEmbeddedAttachmentMeta>emptyList();
    }

    public String getMetaName() {
        return metaName;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getIdName() {
        return (idValueAccessor != null) ? idValueAccessor.getPropertyName() : null;
    }

    public String getRevisionName() {
        return (revisionValueAccessor != null) ? revisionValueAccessor.getPropertyName() : null;
    }

    public String readDocumentId(Object entity) {
        String documentId = null;
        if (idValueAccessor != null) {
            Object value = idValueAccessor.readValue(entity);
            if (value != null) {
                documentId = value.toString();
                if (idPattern != null) {
                    documentId = String.format(idPattern, value);
                }
            }
        }
        return documentId;
    }

    public String readDocumentRevision(Object entity) {
        Object documentRevision = null;
        if (revisionValueAccessor != null) {
            documentRevision = revisionValueAccessor.readValue(entity);
        }
        return (documentRevision != null) ? documentRevision.toString() : null;
    }

    public CouchAttachmentInfoMap readAttachmentInfoMap(Object entity) {
        CouchAttachmentInfoMap attachmentInfoMap = null;
        if (attachmentInfoMapAccessor != null) {
            Object value = attachmentInfoMapAccessor.readValue(entity);
            if (value != null) {
                attachmentInfoMap = (CouchAttachmentInfoMap) value;
            }
        }
        return attachmentInfoMap;
    }

    public void writeDocumentId(T entity, String id) {
        if (idValueAccessor == null || !idValueAccessor.isWritable()) {
            throw CouchJsonException.internalServerError("Cannot assign id to entity " + entityClass);
        }
        idValueAccessor.writeValue(entity, id);
    }

    public void writeDocumentRevision(T entity, String revision) {
        if (revisionValueAccessor == null || !revisionValueAccessor.isWritable()) {
            throw CouchJsonException.internalServerError("Cannot assign revision to entity " + entityClass);
        }
        revisionValueAccessor.writeValue(entity, revision);
    }

    public void writeAttachmentInfoMap(T entity, CouchAttachmentInfoMap attachmentInfoMap) {
        if (attachmentInfoMapAccessor == null || !attachmentInfoMapAccessor.isWritable()) {
            throw CouchJsonException.internalServerError("Cannot assign attachmentInfoMap to entity " + entityClass);
        }
        attachmentInfoMapAccessor.writeValue(entity, attachmentInfoMap);
    }

    public boolean hasId() {
        return idValueAccessor != null;
    }

    public String getIdPattern() {
        return idPattern;
    }

    public boolean isIdSet(Object entity) {
        return idValueAccessor != null && idValueAccessor.readValue(entity) != null;
    }

    public boolean hasRevision() {
        return revisionValueAccessor != null;
    }

    public boolean hasAttachmentsMeta() {
        return attachmentInfoMapAccessor != null;
    }

    public boolean isRevisionSet(Object entity) {
        return revisionValueAccessor != null && revisionValueAccessor.readValue(entity) != null;
    }

    public List<CouchEmbeddedAttachmentMeta> getEmbeddedAttachmentMetaList() {
        return embeddedAttachmentMetaList;
    }

    public boolean hasEmbeddedAttachments() {
        return !embeddedAttachmentMetaList.isEmpty();
    }

    public String getEntityType() {
        return entityType;
    }

}
