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

import java.util.*;

/**
 * User: Harlan
 * Date: 2/3/2015
 * Time: 9:24 PM
 */

public class CouchAttachmentInfoMap {
    private final Map<String, CouchAttachmentInfo> attachmentMap;

    public CouchAttachmentInfoMap(Map<String, CouchAttachmentInfo> attachmentMap) {
        if (attachmentMap == null) {
            this.attachmentMap = Collections.emptyMap();
        } else {
            this.attachmentMap = Collections.unmodifiableMap(attachmentMap);
        }
    }

    public Set<Map.Entry<String, CouchAttachmentInfo>> entrySet() {
        return attachmentMap.entrySet();
    }

    public boolean containsAttachment(String attachmentName) {
        return attachmentMap.containsKey(attachmentName);
    }

    public int size() {
        return attachmentMap.size();
    }

    public CouchAttachmentInfo get(String attachmentName) {
        return attachmentMap.get(attachmentName);
    }

}
