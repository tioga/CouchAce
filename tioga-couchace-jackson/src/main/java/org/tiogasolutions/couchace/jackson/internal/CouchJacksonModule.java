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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfo;
import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.CouchDatabaseInfo;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * User: harlan
 * Date: 3/11/14
 * Time: 10:31 PM
 */
public class CouchJacksonModule extends SimpleModule {

    public CouchJacksonModule() {
        addSerializer(CouchAttachmentInfo.class, new CouchAttachmentMetaSerializer());
        addDeserializer(CouchAttachmentInfo.class, new CouchAttachmentMetaDeserializer());
        addSerializer(CouchAttachmentInfoMap.class, new CouchAttachmentMetaMapSerializer());
        addDeserializer(CouchAttachmentInfoMap.class, new CouchAttachmentMetaMapDeserializer());
        setMixInAnnotation(CouchDatabaseInfo.class, CouchDatabaseInfoMixin.class);
    }

}


