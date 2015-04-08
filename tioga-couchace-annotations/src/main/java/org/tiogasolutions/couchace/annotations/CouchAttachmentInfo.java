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

package org.tiogasolutions.couchace.annotations;

/**
 * User: Harlan
 * Date: 2/3/2015
 * Time: 9:24 PM
 */

public class CouchAttachmentInfo {

    private final String contentType;
    private final int revPos;
    private final String digest;
    private final long length;
    private final boolean stub;

    public CouchAttachmentInfo(String contentType,
                               int revPos,
                               String digest,
                               long length,
                               boolean stub) {
        this.contentType = contentType;
        this.revPos = revPos;
        this.digest = digest;
        this.length = length;
        this.stub = stub;
    }

    public String getContentType() {
        return contentType;
    }

    public int getRevPos() {
        return revPos;
    }

    public String getDigest() {
        return digest;
    }

    public long getLength() {
        return length;
    }

    public boolean isStub() {
        return stub;
    }

}
