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

import org.tiogasolutions.couchace.annotations.*;
import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.internal.util.ClassUtil;
import org.tiogasolutions.couchace.core.internal.util.StringUtil;
import org.tiogasolutions.couchace.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Harlan
 * Date: 3/14/2014
 * Time: 9:25 AM
 */
public class AnnotationCouchMetaBuilder implements CouchMetaBuilder {

    public <T> CouchEntityMeta<T> buildEntityMeta(Class<T> entityClass) {
        List<CouchEmbeddedAttachmentMeta> localEmbeddedAttachmentList = new ArrayList<>();

        // Look for class level annotations.
        CouchEntity couchEntity = ClassUtil.findClassAnnotation(entityClass, CouchEntity.class);
        if (couchEntity == null) {
            throw CouchException.internalServerError("Class " + entityClass.getName() + " does not have required @CouchEntity annotation.");
        }
        String entityType;
        if (StringUtil.isNotBlank(couchEntity.value())) {
            entityType = couchEntity.value();
        } else {
            entityType = entityClass.getSimpleName();
        }

        // Look for method level annotations.
        ValueAccessor localIdAccessor = null;
        ValueAccessor localRevisionAccessor = null;
        ValueAccessor localAttachmentsMetaMapAccessor = null;
        String localIdPattern = null;
        for (Method readMethod : ClassUtil.listGetterMethods(entityClass)) {
            if (readMethod != null) {
                CouchEmbeddedAttachment couchAttachment = readMethod.getAnnotation(CouchEmbeddedAttachment.class);
                if (couchAttachment != null) {
                    ValueAccessor attachmentAccessor = new ValueAccessor(entityClass, readMethod);
                    localEmbeddedAttachmentList.add(new CouchEmbeddedAttachmentMeta(couchAttachment, attachmentAccessor));
                }
                if (localRevisionAccessor == null) {
                    CouchRevision couchRevision = readMethod.getAnnotation(CouchRevision.class);
                    if (couchRevision != null) {
                        localRevisionAccessor = new ValueAccessor(entityClass, readMethod);
                    }
                }
                if (localIdAccessor == null) {
                    CouchId couchId = readMethod.getAnnotation(CouchId.class);
                    if (couchId != null) {
                        localIdAccessor = new ValueAccessor(entityClass, readMethod);
                    }
                }
                if (localIdAccessor == null) {
                    CouchEntityId couchId = readMethod.getAnnotation(CouchEntityId.class);
                    if (couchId != null) {
                        localIdAccessor = new ValueAccessor(entityClass, readMethod);
                        localIdPattern = (StringUtil.isNotBlank(couchId.value())) ? couchId.value() : null;
                    }
                }
            }
        }

        // Look for field level annotations
        for (Field field : ClassUtil.listAllFields(entityClass)) {
            if (CouchAttachmentInfoMap.class.isAssignableFrom(field.getType())) {
                localAttachmentsMetaMapAccessor = new ValueAccessor(field);
            }
        }

        // Id accessor / annotation is mandatory
        if (localIdAccessor == null) {
            throw CouchException.internalServerError("Class " + entityClass.getName() + " does not have required @CouchId annotation.");
        }

        return new CouchEntityMeta<>(entityClass.getName(),
                entityClass,
                entityType,
                localIdAccessor,
                localIdPattern,
                localRevisionAccessor,
                localAttachmentsMetaMapAccessor,
                localEmbeddedAttachmentList);
    }
}
