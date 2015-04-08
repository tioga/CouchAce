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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 10:36 PM
 * Parse attachments meta
 *  "_attachments": {
 *      "someHtml": {
 *          "content_type": "text/html",
 *          "revpos": 2,
 *          "digest": "md5-MmhuLxPUBCd6/uMxKGRAnw==",
 *          "length": 36,
 *          "stub": true
 *       }
 *    }
 */

public final class CouchAttachmentMetaMapSerializer extends StdSerializer<CouchAttachmentInfoMap> {

  public CouchAttachmentMetaMapSerializer() {
      super(CouchAttachmentInfoMap.class);
  }

  @Override
  public void serialize(CouchAttachmentInfoMap attachmentInfoMap, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    for(Map.Entry<String, CouchAttachmentInfo> entry : attachmentInfoMap.entrySet()) {
      gen.writeFieldName(entry.getKey());
      gen.writeObject(entry.getValue());
    }
    gen.writeEndObject();
  }
}

