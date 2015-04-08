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

package org.tiogasolutions.couchace.all.entity.person;

import org.tiogasolutions.couchace.annotations.CouchEmbeddedAttachment;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:34 PM
 */
@CouchEntity("person")
public class PersonEntity {

    private final InjectedLogger logger;
    private final InjectedEventBus eventBus ;

    private final String id;
    private final String version;
    private String status;
    private String firstName;
    private final String lastName;
    private final String attachmentHtml;
    private final byte[] imageBytes;
    private final AddressEntity address;

    public PersonEntity(PersonEntity p) {
        this.logger = p.getLogger();
        this.eventBus = p.getEventBus();

        this.id = p.id;
        this.version = p.getVersion();
        this.status = p.getStatus();
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.attachmentHtml = p.attachmentHtml;
        this.imageBytes = p.imageBytes;
        this.address = p.address;
    }

    @JsonCreator
    public PersonEntity(@JacksonInject InjectedEventBus eventBus,       // injected without name
                        @JacksonInject("logger") InjectedLogger logger, // injected by name
                        @JsonProperty("id") String id,
                        @JacksonInject("version") String version,
                        @JsonProperty("status") String status,
                        @JsonProperty("firstName") String firstName,
                        @JsonProperty("lastName") String lastName,
                        @JacksonInject("attachmentHtml") String attachmentHtml,
                        @JacksonInject("imageBytes") byte[] imageBytes,
                        @JsonProperty("address") AddressEntity address) {

        this.eventBus = eventBus;
        this.logger = logger;

        this.id = id;
        this.version = version;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attachmentHtml = attachmentHtml;
        this.imageBytes = imageBytes;
        this.address = address;

        logger.trace("Created person.");
        eventBus.personCreated();
    }

    @CouchId
    public String getId() {
        return id;
    }

    @CouchRevision
    public String getVersion() {
        return version;
    }

    @JsonIgnore
    public InjectedLogger getLogger() {
        return logger;
    }

    @JsonIgnore
    public InjectedEventBus getEventBus() {
        return eventBus;
    }

  public String getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @CouchEmbeddedAttachment(contentType = MediaType.TEXT_HTML)
    public String getAttachmentHtml() {
        return attachmentHtml;
    }

    @CouchEmbeddedAttachment(contentType = "image/jpeg")
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEntity that = (PersonEntity) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (attachmentHtml != null ? !attachmentHtml.equals(that.attachmentHtml) : that.attachmentHtml != null)
            return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!Arrays.equals(imageBytes, that.imageBytes)) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (attachmentHtml != null ? attachmentHtml.hashCode() : 0);
        result = 31 * result + (imageBytes != null ? Arrays.hashCode(imageBytes) : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
            "id='" + id + '\'' +
            ", version='" + version + '\'' +
            ", status='" + status + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", address=" + address +
            ", attachmentHtml=" + attachmentHtml +
            '}';
    }
}
