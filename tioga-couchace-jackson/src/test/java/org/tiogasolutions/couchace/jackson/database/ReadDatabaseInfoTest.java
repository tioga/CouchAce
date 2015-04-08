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

package org.tiogasolutions.couchace.jackson.database;

import org.tiogasolutions.couchace.core.api.CouchDatabaseInfo;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.JacksonTestSetup;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static org.testng.Assert.assertEquals;

@Test
public class ReadDatabaseInfoTest {

    private JacksonCouchJsonStrategy strategy = JacksonTestSetup.strategy;

    public void viewResultsWithDocsTest() throws URISyntaxException {
        String json = JacksonTestSetup.databaseInfoJson;
        CouchDatabaseInfo info = strategy.readDatabaseInfo(json);
        assertEquals(info.getDbName(), "couchace-test");
        assertEquals(info.getDocCount(), new Integer(20));
        assertEquals(info.getDocDelCount(), new Integer(1));
        assertEquals(info.getUpdateSeq(), new Integer(29));
        assertEquals(info.getPurgeSeq(), new Integer(0));
        assertEquals(info.getCompactRunning(), Boolean.FALSE);
        assertEquals(info.getDiskSize(), new Integer(36968));
        assertEquals(info.getDataSize(), new Integer(7534));
        assertEquals(info.getInstanceStartTime(), new Long(1393733215845300L));
        assertEquals(info.getDiskFormatVersion(), new Integer(6));
        assertEquals(info.getCommittedUpdateSeq(), new Integer(29));

    }

}
