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

package org.tiogasolutions.couchace.jackson.entity;

import org.tiogasolutions.couchace.annotations.CouchEmbeddedAttachment;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@CouchEntity
public class LocationEntity {

    private String id;
    private String revision;

    private String direction;
    private String city;
    private String notes;

    @JsonCreator
    public LocationEntity(
          @JacksonInject("id") String id,
          @JacksonInject("revision") String revision,
          @JsonProperty("direction") String direction,
          @JsonProperty("city") String city,
          @JacksonInject("notes") String notes) {

        this.id = id;
        this.revision = revision;
        this.direction = direction;
        this.city = city;
        this.notes = notes;
    }

    @CouchId
    public String getId() {
        return id;
    }

    @CouchRevision
    public String getRevision() {
        return revision;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @CouchEmbeddedAttachment
    public String getNotes() {
        return notes;
    }
}
