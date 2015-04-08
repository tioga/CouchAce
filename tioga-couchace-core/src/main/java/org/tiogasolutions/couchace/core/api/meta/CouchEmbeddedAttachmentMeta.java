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

import org.tiogasolutions.couchace.annotations.CouchEmbeddedAttachment;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.internal.util.StringUtil;

public class CouchEmbeddedAttachmentMeta {
    private final ValueAccessor valueAccessor;
    private final String attachmentName;
    private final CouchMediaType contentType;

    public CouchEmbeddedAttachmentMeta(CouchEmbeddedAttachment couchAttachment, ValueAccessor valueAccessor) {
        this.valueAccessor = valueAccessor;
        this.attachmentName = (StringUtil.isBlank(couchAttachment.name())) ? valueAccessor.getPropertyName() : couchAttachment.name();
        this.contentType = CouchMediaType.fromString(couchAttachment.contentType());
    }

    public ValueAccessor getValueAccessor() {
        return valueAccessor;
    }

    public Object readValue(Object object) {
        return valueAccessor.readValue(object);
    }

    public void writeValue(Object object, Object value) {
        valueAccessor.writeValue(object, value);
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public CouchMediaType getContentType() {
        return contentType;
    }

}
