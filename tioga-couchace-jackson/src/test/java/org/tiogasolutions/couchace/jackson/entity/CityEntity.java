package org.tiogasolutions.couchace.jackson.entity;

import org.tiogasolutions.couchace.annotations.CouchEmbeddedAttachment;
import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchEntityId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@CouchEntity
public class CityEntity {

  private String id;
  private String revision;

  private String city;
  private int population;
  private String notes;

  @JsonCreator
  public CityEntity(@JacksonInject("id") String id,
                    @JacksonInject("revision") String revision,
                    @JsonProperty("city") String city,
                    @JsonProperty("population") int population,
                    @JacksonInject("notes") String notes) {
    this.id = id;
    this.revision = revision;
    this.city = city;
    this.population = population;
    this.notes = notes;
  }

  @CouchEntityId("%s:city")
  public String getId() {
    return id;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public String getCity() {
    return city;
  }

  public int getPopulation() {
    return population;
  }

  @CouchEmbeddedAttachment
  public String getNotes() {
    return notes;
  }
}
