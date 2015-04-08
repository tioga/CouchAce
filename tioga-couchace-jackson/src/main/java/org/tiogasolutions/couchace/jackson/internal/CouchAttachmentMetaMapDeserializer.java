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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 10:36 PM
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
public final class CouchAttachmentMetaMapDeserializer extends StdDeserializer<CouchAttachmentInfoMap> {

  public CouchAttachmentMetaMapDeserializer() {
    super(CouchAttachmentInfoMap.class);
  }

  @Override
  public Object deserializeWithType(JsonParser parser, DeserializationContext context, TypeDeserializer deserializer)
          throws IOException
  {
    return deserializer.deserializeTypedFromAny(parser, context);
  }


  @Override
  public CouchAttachmentInfoMap deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    Map<String, CouchAttachmentInfo> attachmentMap = new HashMap<>();

    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String attachmentName = parser.getCurrentName();
      parser.nextToken();
      CouchAttachmentInfo attachmentMeta = parser.readValueAs(CouchAttachmentInfo.class);
      attachmentMap.put(attachmentName, attachmentMeta);
    }

    return new CouchAttachmentInfoMap(attachmentMap);
  }
}

