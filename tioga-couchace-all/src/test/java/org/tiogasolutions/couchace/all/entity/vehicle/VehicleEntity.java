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

import org.tiogasolutions.couchace.annotations.CouchEntity;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;

@CouchEntity("Vehicle")
public class VehicleEntity {
    private String id;
    private String revision;
    private VehicleType vehicleType;
    private String make;
    private String model;
    private String year;
    private String color;

    public static VehicleEntity newVehicle(String id, VehicleType vehicleType, String make, String model, String year, String color) {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.id = id;
        vehicle.revision = null;
        vehicle.vehicleType = vehicleType;
        vehicle.make = make;
        vehicle.model = model;
        vehicle.year = year;
        vehicle.color = color;
        return vehicle;
    }

    /**
     * Used by Jackson / CouchAce
     */
    private VehicleEntity() {
    }

    @CouchId
    public String getId() {
        return id;
    }

    @CouchRevision
    public String getRevision() {
        return revision;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
