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

package org.tiogasolutions.couchace.all.entity.vehicle;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.tiogasolutions.couchace.core.api.response.GetDocumentResponse;
import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
public class VehicleEntityTest {

    CouchDatabase couchDatabase;

    @BeforeClass()
    public void setup() {
        couchDatabase = TestSetup.couchDatabase();
    }

    @Test
    public void vehicleEntityTest() throws IOException {

        // Perform an initial count, should be zero
        CouchViewQuery couchViewQuery = CouchViewQuery
            .builder("entity", "countEntityType")
            .key("Vehicle")
            .includeDocs(false)
            .build();
        long initialVehicleCount = couchDatabase
            .get()
            .document(couchViewQuery)
            .execute()
            .getFirstContentAsLong();

        // Add a vehicle
        String firebirdId = "99";
        VehicleEntity firebird = VehicleEntity.newVehicle(firebirdId, VehicleType.CAR, "Pontiac", "Firebird", "1969", "Black");
        WriteResponse putResponse = couchDatabase
            .put()
            .entity(firebird)
            .execute();
        assertEquals(putResponse.getDocumentId(), firebirdId);
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertNotNull(putResponse.getDocumentRevision());

        // Check count
        long newVehicleCount = couchDatabase
            .get()
            .document(couchViewQuery)
            .execute()
            .getFirstContentAsLong();
        assertEquals(newVehicleCount, initialVehicleCount + 1);

        // Retrieve the vehicle we just added.
        VehicleEntity foundFirebird = couchDatabase
            .get()
            .entity(VehicleEntity.class, firebirdId)
            .execute()
            .getFirstEntity();

        assertEquals(foundFirebird.getId(), firebird.getId());
        assertEquals(foundFirebird.getRevision(), putResponse.getDocumentRevision());
        assertEquals(foundFirebird.getVehicleType(), firebird.getVehicleType());
        assertEquals(foundFirebird.getMake(), firebird.getMake());
        assertEquals(foundFirebird.getModel(), firebird.getModel());
        assertEquals(foundFirebird.getYear(), firebird.getYear());
        assertEquals(foundFirebird.getColor(), firebird.getColor());

        // Retrieve the raw document and ensure the id is not part of the entity body.
        GetDocumentResponse response = couchDatabase
                .get()
                .document(firebirdId)
                .execute();
        TextDocument textDocument = response.getFirstDocument();
        String content = textDocument.getContent();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(content, Map.class);
        Map entity = (Map) map.get("entity");
        assertFalse(entity.containsKey("id"), "The entity body stored in couch contains the id.");

        // Change color to blue and update
        foundFirebird.setColor("Green");
        putResponse = couchDatabase
            .put()
            .entity(foundFirebird)
            .execute();
        assertEquals(putResponse.getDocumentId(), firebirdId);
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertNotNull(putResponse.getDocumentRevision());

        // Get and ensure color was updated.
        foundFirebird = couchDatabase
            .get()
            .entity(VehicleEntity.class, firebirdId)
            .execute()
            .getFirstEntity();
        assertEquals(foundFirebird.getColor(), "Green");
        assertEquals(foundFirebird.getRevision(), putResponse.getDocumentRevision());
    }

}
