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
import org.tiogasolutions.couchace.core.api.json.CouchJsonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 10:36 PM
 */
public final class CouchAttachmentMetaDeserializer extends StdDeserializer<CouchAttachmentInfo> {

  public CouchAttachmentMetaDeserializer() {
    super(CouchAttachmentInfo.class);
  }

  @Override
  public CouchAttachmentInfo deserialize(JsonParser parser, DeserializationContext context) throws IOException {

    String contentType = null;
    Integer revPos = null;
    String digest = null;
    Long length = null;
    Boolean stub = null;
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      String name = parser.getCurrentName();
      parser.nextToken();
      if ("content_type".equalsIgnoreCase(name)) {
        contentType = parser.getValueAsString();

      } else if ("revpos".equalsIgnoreCase(name)) {
        revPos = parser.getIntValue();

      } else if ("digest".equalsIgnoreCase(name)) {
        digest = parser.getValueAsString();

      } else if ("length".equalsIgnoreCase(name)) {
        length = parser.getLongValue();

      } else if ("stub".equalsIgnoreCase(name)) {
        stub = parser.getBooleanValue();
      }

    }

    // Verify all key values were found.
    if (contentType == null) {
      throw new CouchJsonException("CouchAttachmentMeta JSON does not specify containsType");
    }
    if (revPos == null) {
      throw new CouchJsonException("CouchAttachmentMeta JSON does not specify revPos");
    }
    if (digest == null) {
      throw new CouchJsonException("CouchAttachmentMeta JSON does not specify digest");
    }
    if (length == null) {
      throw new CouchJsonException("CouchAttachmentMeta JSON does not specify length");
    }
    if (stub == null) {
      throw new CouchJsonException("CouchAttachmentMeta JSON does not specify stub");
    }

    return new CouchAttachmentInfo(contentType,
            revPos,
            digest,
            length,
            stub);
  }
}

