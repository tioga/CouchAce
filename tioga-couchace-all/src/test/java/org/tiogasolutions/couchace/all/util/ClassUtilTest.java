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

package org.tiogasolutions.couchace.all.util;

import org.tiogasolutions.couchace.all.entity.action.ActionEntity;
import org.tiogasolutions.couchace.all.entity.action.SpeakActionEntity;
import org.tiogasolutions.couchace.all.entity.vehicle.VehicleEntity;
import org.tiogasolutions.couchace.annotations.CouchEntityId;
import org.tiogasolutions.couchace.annotations.CouchId;
import org.tiogasolutions.couchace.annotations.CouchRevision;
import org.tiogasolutions.couchace.core.internal.util.ClassUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class ClassUtilTest {

    public void listGetterMethodActionTest() {

        // ActionEntity
        List<Method> methodList = ClassUtil.listGetterMethods(ActionEntity.class);
        Map<String, Method> methodMap = ClassUtil.toPropertyMethodMap(methodList);
        assertTrue(methodMap.containsKey("id"));
        assertTrue(methodMap.containsKey("revision"));
        assertTrue(methodMap.containsKey("description"));

        // Ensure they contain the annotation
        assertNotNull(methodMap.get("id").getAnnotation(CouchEntityId.class));
        assertNotNull(methodMap.get("revision").getAnnotation(CouchRevision.class));

        // ActionEntity
        methodList = ClassUtil.listGetterMethods(SpeakActionEntity.class);
        methodMap = ClassUtil.toPropertyMethodMap(methodList);
        assertTrue(methodMap.containsKey("id"));
        assertTrue(methodMap.containsKey("revision"));
        assertTrue(methodMap.containsKey("description"));
        assertTrue(methodMap.containsKey("said"));

        // Ensure they contain the annotation -- need to use list in this case since map will contain AbstractActionEntity methods
        Method idMethod = null;
        Method revisionMethod = null;
        for (Method method : methodList) {
            if (method.getName().equals("getId") && method.getAnnotation(CouchEntityId.class) != null) {
                idMethod = method;
            }
            if (method.getName().equals("getRevision") && method.getAnnotation(CouchRevision.class) != null) {
                revisionMethod = method;
            }
        }
        assertNotNull(idMethod, "Did not find idMethod");
        assertNotNull(revisionMethod, "Did not find revisionMethod");
    }

    public void listGetterMethodVehicleTest() {

        // VehicleEntity
        List<Method> methodList = ClassUtil.listGetterMethods(VehicleEntity.class);
        Map<String, Method> methodMap = ClassUtil.toPropertyMethodMap(methodList);
        assertTrue(methodMap.containsKey("id"));
        assertTrue(methodMap.containsKey("revision"));
        assertTrue(methodMap.containsKey("vehicleType"));
        assertTrue(methodMap.containsKey("model"));

        // Ensure they contain the annotation
        assertNotNull(methodMap.get("id").getAnnotation(CouchId.class));
        assertNotNull(methodMap.get("revision").getAnnotation(CouchRevision.class));

        // VehicleEntity
        methodList = ClassUtil.listGetterMethods(SpeakActionEntity.class);
        methodMap = ClassUtil.toPropertyMethodMap(methodList);
        assertTrue(methodMap.containsKey("id"));
        assertTrue(methodMap.containsKey("revision"));

    }
}
