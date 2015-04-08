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

import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.core.api.meta.CouchEmbeddedAttachmentMeta;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.tiogasolutions.couchace.core.internal.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CouchJacksonSerializerIntrospector extends JacksonAnnotationIntrospector {
    private static final Logger log = LoggerFactory.getLogger(CouchJacksonSerializerIntrospector.class);

    private final CouchMetaRepository couchMetaRepository;

    public CouchJacksonSerializerIntrospector(CouchMetaRepository couchMetaRepository) {
        this.couchMetaRepository = couchMetaRepository;
    }

    @Override
    public String[] findPropertiesToIgnore(Annotated ac) {
        if (log.isTraceEnabled()) {
            log.trace("Finding properties to ignore on: " + ac);
        }

        // Let super determine base
        String[] propertiesToIgnore = super.findPropertiesToIgnore(ac);

        if (ac != null && ac.getRawType() != null) {
            Class entityClass = ac.getRawType();
            CouchEntity couchEntity = ClassUtil.findClassAnnotation(entityClass, CouchEntity.class);
            if (couchEntity != null) {
                // This is a CouchEntity look for others properties to ignore.
                Set<String> ignorePropertySet = new HashSet<>();

                CouchEntityMeta entityMeta = couchMetaRepository.getOrCreateEntityMeta(ac.getRawType());
                if (entityMeta != null) {
                    // Ignore id property, if it does not have a pattern.
                    if (entityMeta.hasId() && entityMeta.getIdPattern() == null) {
                        ignorePropertySet.add(entityMeta.getIdName());
                    }
                    // Ignore revision property.
                    if (entityMeta.hasRevision()) {
                        ignorePropertySet.add(entityMeta.getRevisionName());
                    }
                    // Ignore revision property.
                    if (entityMeta.hasEmbeddedAttachments()) {
                        // Not sure why entityMeta.getEmbeddedAttachmentMetaList() is not being seen as a List<CouchEmbeddedAttachmentMeta>
                        List<CouchEmbeddedAttachmentMeta> attachmentMetaList = entityMeta.getEmbeddedAttachmentMetaList();
                        for (CouchEmbeddedAttachmentMeta embeddedAttachmentMeta : attachmentMetaList) {
                            ignorePropertySet.add(embeddedAttachmentMeta.getValueAccessor().getPropertyName());
                        }
                    }
                }

                propertiesToIgnore = ignorePropertySet.toArray(new String[ignorePropertySet.size()]);
            }
        }

        return propertiesToIgnore;

    }

}
