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

import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

@CouchEntity
public class LocationInjectedEntity {
    private final String id;
    private final String direction;
    private final String city;
    private final String injected;

    public LocationInjectedEntity(
            @JacksonInject("id") String id,
            @JsonProperty("direction") String direction,
            @JsonProperty("city") String city,
            @JacksonInject("injected") String injected) {
        this.id = id;
        this.direction = direction;
        this.city = city;
        this.injected = injected;
    }

    @CouchId
    public String getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public String getCity() {
        return city;
    }

    public String getInjected() {
        return injected;
    }
}
