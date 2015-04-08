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

import org.tiogasolutions.couchace.core.api.request.PostEntityRequest;
import org.tiogasolutions.couchace.core.api.request.PutEntityRequest;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.JacksonTestSetup;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class WriteEntityTest {

  private final JacksonCouchJsonStrategy strategy = JacksonTestSetup.strategy;

  private final CityEntity city = new CityEntity("4234", "1-jflk3j4odj92p3oem1239jke0329kk345", "Oakhurst", 2829, "1) This is a city\n2)It's really cool");
  private final LocationEntity location = new LocationEntity("1122", "1-03ba856139647f1ed4694ceb1fe21751", "North", "Toronto", "1) I don't know where this is\n2) No one really even cares.");

  public void createLocationForPost() {
    PostEntityRequest request = new PostEntityRequest(null, "location", location);
    String json = strategy.createJsonForPost(request);

    Assert.assertEquals(
        removeLineSeparators(json),
        removeLineSeparators(LOCATION_JSON_POST));
  }

  public void createLocationForPut() {
    PutEntityRequest request = new PutEntityRequest(null, location.getId(), "location", location, location.getRevision());
    String json = strategy.createJsonForPut(request);

    Assert.assertEquals(
        removeLineSeparators(json),
        removeLineSeparators(LOCATION_JSON_PUT));
  }

  public void createCityForPost() {
    PostEntityRequest request = new PostEntityRequest(null, "city", city);
    String json = strategy.createJsonForPost(request);

    Assert.assertEquals(
        removeLineSeparators(json),
        removeLineSeparators(CITY_JSON_POST));
  }

  public void createCityForPut() {
    PutEntityRequest request = new PutEntityRequest(null, city.getId(), "city", city, city.getRevision());
    String json = strategy.createJsonForPut(request);

    Assert.assertEquals(
        removeLineSeparators(json),
        removeLineSeparators(CITY_JSON_PUT));
  }

  public static String removeLineSeparators(String str) {
  return str.replaceAll("(\\r)?\\n", "");
}

  public static final String LOCATION_JSON_POST = "{\n" +
      "\"entityType\":\"location\",\n" +
      "\"entity\" : {\n" +
      "  \"direction\" : \"North\",\n" +
      "  \"city\" : \"Toronto\"}\n" +
      "}";

  public static final String LOCATION_JSON_PUT = "{\n" +
      "\"_id\":\"1122\",\n" +
      "\"_rev\":\"1-03ba856139647f1ed4694ceb1fe21751\",\n" +
      "\"entityType\":\"location\",\n" +
      "\"entity\" : {\n" +
      "  \"direction\" : \"North\",\n" +
      "  \"city\" : \"Toronto\"}\n" +
      "}";

  public static final String CITY_JSON_POST = "{\n" +
      "\"entityType\":\"city\",\n" +
      "\"entity\" : {\n" +
      "  \"city\" : \"Oakhurst\",\n" +
      "  \"population\" : 2829,\n" +
      "  \"id\" : \"4234\"}\n" +
      "}";

  public static final String CITY_JSON_PUT = "{\n" +
      "\"_id\":\"4234\",\n" +
      "\"_rev\":\"1-jflk3j4odj92p3oem1239jke0329kk345\",\n" +
      "\"entityType\":\"city\",\n" +
      "\"entity\" : {\n" +
      "  \"city\" : \"Oakhurst\",\n" +
      "  \"population\" : 2829,\n" +
      "  \"id\" : \"4234\"}\n" +
      "}";
}
