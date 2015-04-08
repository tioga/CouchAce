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

package org.tiogasolutions.couchace.all.paging;

import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchEntityId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:34 PM
 */
@CouchEntity("Pet")
public class PetEntity {
    public static int nextId = 1;

    public enum PetType {CAT, DOG, FISH, BIRD}

    private final int id;
    private final String version;
    private final String name;
    private final PetType type;
    private final LocalDateTime createdAt;

    public static PetEntity newPet(String name, PetType type) {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new PetEntity(nextId++, null, name, type, LocalDateTime.now());
    }

    @JsonCreator
    private PetEntity(@JsonProperty("id") int id,
                      @JacksonInject("version") String version,
                      @JsonProperty("name") String name,
                      @JsonProperty("type") PetType type,
                      @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }

    @CouchEntityId("Pet:%s")
    public int getId() {
        return id;
    }

    @CouchRevision
    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public PetType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetEntity petEntity = (PetEntity) o;

        if (id != petEntity.id) return false;
        if (createdAt != null ? !createdAt.equals(petEntity.createdAt) : petEntity.createdAt != null) return false;
        if (name != null ? !name.equals(petEntity.name) : petEntity.name != null) return false;
        if (type != petEntity.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PetEntity{" +
            "id=" + id +
            ", version='" + version + '\'' +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", createdAt=" + createdAt +
            '}';
    }
}
