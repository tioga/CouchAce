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

package org.tiogasolutions.couchace.jackson.text;

import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.JacksonTestSetup;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@Test
public class ReadDocumentTest {

    private JacksonCouchJsonStrategy strategy = JacksonTestSetup.strategy;

    public void petTextDocumentTest() {
        TextDocument couchDocument = strategy.readTextDocument(JacksonTestSetup.petDocJson);
        assertEquals(couchDocument.getDocumentId(), "7889");
        assertEquals(couchDocument.getDocumentRevision(), "1-8765");
        String content = couchDocument.getContent();
        assertNotNull(content);
        assertTrue(content.contains("\"_id\":\"7889\","), content);
        assertTrue(content.contains("\"type\":\"DOG\","), content);
        assertEquals(couchDocument.getContent(), JacksonTestSetup.petDocJson);
    }

    public void petViewWithDocsTest() {

        List<TextDocument> couchDocuments = strategy.readTextDocuments(JacksonTestSetup.petViewWithDocs);
        assertEquals(couchDocuments.size(), 2);

        TextDocument couchDocument = couchDocuments.get(0);
        assertEquals(couchDocument.getDocumentId(), "7889");
        assertEquals(couchDocument.getDocumentRevision(), "1-8765");
        String content = couchDocument.getContent();
        assertNotNull(content);
        assertTrue(content.contains("\"_id\":\"7889\","), content);
        assertTrue(content.contains("\"type\":\"DOG\","), content);
        assertEquals(couchDocument.getContent(), JacksonTestSetup.petDocJson);

        couchDocument = couchDocuments.get(1);
        assertEquals(couchDocument.getDocumentRevision(), "1-99222");
        assertEquals(couchDocument.getDocumentId(), "9999");
        content = couchDocument.getContent();
        assertNotNull(content);
        assertTrue(content.contains("\"_id\":\"9999\","), content);
        assertTrue(content.contains("\"type\":\"DOG\","), content);
        assertTrue(content.contains("\"name\":\"Rocky\","), content);
    }

}
