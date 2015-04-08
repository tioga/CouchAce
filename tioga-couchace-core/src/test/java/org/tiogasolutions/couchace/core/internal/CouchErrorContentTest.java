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

package org.tiogasolutions.couchace.core.internal;

import org.tiogasolutions.couchace.core.api.response.CouchErrorContent;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;

@Test
public class CouchErrorContentTest {

    public void parseValidJsonTest() {
        String json = "{\"error\":\"some error\",\"reason\":\"some reason\"}";
        CouchErrorContent errorContent = CouchErrorContent.parseJson(json);
        assertEquals(errorContent.getError(), "some error");
        assertEquals(errorContent.getReason(), "some reason");
        assertFalse(errorContent.isNoError());

        json = "{\"error\":\"some error\"}";
        errorContent = CouchErrorContent.parseJson(json);
        assertEquals(errorContent.getError(), "some error");
        assertNull(errorContent.getReason());
        assertFalse(errorContent.isNoError());

        json = "{\"reason\":\"some reason\"}";
        errorContent = CouchErrorContent.parseJson(json);
        assertNull(errorContent.getError());
        assertEquals(errorContent.getReason(), "some reason");
        assertFalse(errorContent.isNoError());
    }

    public void parseInvalidJsonTest() {
        CouchErrorContent errorContent = CouchErrorContent.parseJson("junk");
        assertEquals(errorContent.getError(), "junk");
        assertEquals(errorContent.getReason(), "junk");
        assertFalse(errorContent.isNoError());

        errorContent = CouchErrorContent.parseJson("{junk}");
        assertEquals(errorContent.getError(), "{junk}");
        assertEquals(errorContent.getReason(), "{junk}");
        assertFalse(errorContent.isNoError());

        errorContent = CouchErrorContent.parseJson("");
        assertEquals(errorContent, CouchErrorContent.noError);
        assertEquals(errorContent.getError(), "none");
        assertEquals(errorContent.getReason(), "No error");
        assertTrue(errorContent.isNoError());

        errorContent = CouchErrorContent.parseJson(null);
        assertEquals(errorContent, CouchErrorContent.noError);
    }
}
