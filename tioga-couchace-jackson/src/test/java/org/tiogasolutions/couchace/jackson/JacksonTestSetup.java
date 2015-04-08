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

package org.tiogasolutions.couchace.jackson;

import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.internal.util.IOUtil;

import java.net.URL;

public class JacksonTestSetup {

    public static JacksonCouchJsonStrategy strategy;
    static {
        strategy = new JacksonCouchJsonStrategy();
        CouchSetup couchSetup = new CouchSetup("http:\\something")
                .setJsonStrategy(strategy);
    }
    public static String databaseInfoJson = readJson("database-info.json");
    public static String locationEntityDocJson = readJson("location-entity-doc.json");
    public static String viewResultsWithDocsJson = readJson("location-entity-view-with-docs.json");
    public static String petDocJson = readJson("pet-doc.json");
    public static String petViewWithDocs = readJson("pet-view-with-docs.json");
    public static String viewWithBigDocs = readJson("view-with-big-docs.json");

    public static String readJson(String filename) {
        URL url = JacksonTestSetup.class.getClassLoader().getResource(filename);
        return IOUtil.readText(url);
    }

}
