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

package org.tiogasolutions.couchace.all.test;

import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.CouchServer;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.request.CouchFeature;
import org.tiogasolutions.couchace.core.api.request.CouchFeatureSet;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.internal.util.IOUtil;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jersey.JerseyCouchHttpClient;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertTrue;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
public class TestSetup {

    private static TestSetup singleton = null;

    public static final String databaseName = "test-couchace";
    public static final String userName = "couchace";
    public static final String password = "unittest";

    public static final String storePass = "ZWYyazhISHFt";

    //  private static final String couchUrlSsl = "https://localhost:6984"
    public static final String couchUrl = "http://localhost:5984";

    private byte[] imageBytes;

    public static TestSetup singleton() {
        if (singleton == null) {
            singleton = new TestSetup();
        }
        return singleton;
    }

    public static CouchDatabase couchDatabase() {
        return singleton().couchDatabase;
    }

    private CouchSetup couchSetup;
    private CouchDatabase couchDatabase;

    public TestSetup() {

        // REVIEW - Now using SSL Setup
        JacksonCouchJsonStrategy jsonStrategy = new JacksonCouchJsonStrategy(new JSR310Module());
        couchSetup = new CouchSetup(couchUrl)
                .setUserName(userName)
                .setPassword(password)
                .setHttpClient(JerseyCouchHttpClient.class)
                .setJsonStrategy(jsonStrategy);
        if (couchUrl.startsWith("https")) {
            File moduleDir = IOUtil.findDirNear(IOUtil.currentDir(), "couchace-all");
            File keystoreFile = new File(moduleDir, "src/test/resources/couch-test.jks");
            couchSetup.ssl(keystoreFile.getAbsolutePath(), storePass);
        }

        CouchServer couchServer = new CouchServer(couchSetup);

        CouchFeatureSet featureSet = CouchFeatureSet.builder()
            .add(CouchFeature.ALLOW_DB_DELETE, true)
            .build();
        couchDatabase = couchServer.database(databaseName, featureSet);
        couchDatabase.recreateDatabase();
        assertTrue(couchDatabase.exists());

        // Add the Entity design doc
        URL designUrl = getClass().getClassLoader().getResource("design/entity-design.json");
        WriteResponse response = couchDatabase.put().design("entity", designUrl).execute();
        assertTrue(response.isCreated());

        // Add the Person design doc
        designUrl = getClass().getClassLoader().getResource("design/person-design.json");
        response = couchDatabase.put().design("person", designUrl).execute();
        assertTrue(response.isCreated());

        // Add Pet design doc
        designUrl = getClass().getClassLoader().getResource("design/pet-design.json");
        response = couchDatabase.put().design("pet", designUrl).execute();
        assertTrue(response.isCreated());

        // Add City design doc
        designUrl = getClass().getClassLoader().getResource("design/city-design.json");
        response = couchDatabase.put().design("city", designUrl).execute();
        assertTrue(response.isCreated());

        // Read the image bytes
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("AceOfSpades.jpg").toURI());
            imageBytes = Files.readAllBytes(path);
        } catch (Exception ex) {
            throw new RuntimeException("Error reading image.", ex);
        }
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public CouchSetup getCouchSetup() {
        return couchSetup;
    }

    public CouchDatabase getCouchDatabase() {
        return couchDatabase;
    }
}
