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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 10:36 PM
 */

public final class CouchAttachmentMetaSerializer extends StdSerializer<CouchAttachmentInfo> {

  public CouchAttachmentMetaSerializer() {
      super(CouchAttachmentInfo.class);
  }

  @Override
  public void serialize(CouchAttachmentInfo attachmentMeta, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeFieldName("content_type");
    gen.writeString(attachmentMeta.getContentType());

    gen.writeFieldName("revpos");
    gen.writeNumber(attachmentMeta.getRevPos());

    gen.writeFieldName("digest");
    gen.writeString(attachmentMeta.getDigest());

    gen.writeFieldName("length");
    gen.writeNumber(attachmentMeta.getLength());

    gen.writeFieldName("stub");
    gen.writeBoolean(attachmentMeta.isStub());

    gen.writeEndObject();
  }
}

