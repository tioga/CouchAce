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

package org.tiogasolutions.couchace.all.entity.notification;

import org.tiogasolutions.couchace.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;

import java.util.UUID;

/**
 * User: Harlan
 * Date: 2/3/2015
 * Time: 8:59 PM
 */
@CouchEntity("Notification")
public class Notification {

    public static Notification newNotification(String message) {
        return new Notification(UUID.randomUUID().toString(), null, message, false);
    }

    private final String id;
    private final String revision;
    private final String message;
    private boolean sent;
    private CouchAttachmentInfoMap attachmentInfoMap;

    public Notification(@JsonProperty("id") String id,
                        @JsonProperty("revision") String revision,
                        @JsonProperty("message") String message,
                        @JsonProperty("sent") boolean sent) {
        this.id = id;
        this.revision = revision;
        this.message = message;
        this.sent = sent;
    }

    @CouchId
    public String getId() {
        return id;
    }

    @CouchRevision
    public String getRevision() {
        return revision;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
