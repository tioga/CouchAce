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

package org.tiogasolutions.couchace.all.entity.action;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.api.query.CouchViewQuery;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
@Test
public class ActionEntityTest {

    private CouchDatabase couchDatabase = TestSetup.couchDatabase();
    private List<ActionEntity> actions = new ArrayList<>();

    private int nextId = 7000;

    public void actionEntityTest() throws Exception {

        // Perform an initial count, should be zero
        CouchViewQuery couchViewQuery = CouchViewQuery
            .builder("entity", "countEntityType")
            .key("Action")
            .includeDocs(false)
            .build();
        long initialActionCount = couchDatabase
            .get()
            .document(couchViewQuery)
            .execute()
            .getFirstContentAsLong();

        // Add actions
        SpeakActionEntity speakAction = SpeakActionEntity.newSpeak(newId(), "Speak something", "Hello");
        putAction(speakAction);

        WalkActionEntity walkAction = WalkActionEntity.newWalk(newId(), "Take a walk", 10);
        putAction(walkAction);

        SleepActionEntity sleepAction = SleepActionEntity.newSleep(newId(), "Going to nappy house", 10);
        putAction(sleepAction);

        // Check count - should be initial + 3
        long newActionCount = couchDatabase
            .get()
            .document(couchViewQuery)
            .execute()
            .getFirstContentAsLong();

        assertEquals(newActionCount, initialActionCount + 3);

        // Retrieve the speak action.
        SpeakActionEntity foundSpeakAction = couchDatabase
            .get()
            .entity(SpeakActionEntity.class, "Action:" + speakAction.getId())
            .execute()
            .getFirstEntity();

        assertEquals(foundSpeakAction.getId(), speakAction.getId());
        assertEquals(foundSpeakAction.getClass(), SpeakActionEntity.class);
        assertEquals(foundSpeakAction.getActionType(), speakAction.getActionType());
        assertEquals(foundSpeakAction.getSaid(), speakAction.getSaid());
        assertNotNull(foundSpeakAction.getRevision(), "Revision is null");

        // Update speak action.
        foundSpeakAction.setSaid("Goodbye");
        WriteResponse putResponse = couchDatabase
            .put()
            .entity(foundSpeakAction)
            .execute();
        assertEquals(putResponse.getDocumentId(), "Action:" + foundSpeakAction.getId());
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertNotNull(putResponse.getDocumentRevision());

        // Get and ensure said was updated.
        foundSpeakAction = couchDatabase
            .get()
            .entity(SpeakActionEntity.class, "Action:" + foundSpeakAction.getId())
            .execute()
            .getFirstEntity();
        assertEquals(foundSpeakAction.getSaid(), "Goodbye");
        assertEquals(foundSpeakAction.getRevision(), putResponse.getDocumentRevision());

        // Should be the same if searching by ActionEntity
        ActionEntity actionEntity = couchDatabase
            .get()
            .entity(ActionEntity.class, "Action:" + foundSpeakAction.getId())
            .execute()
            .getFirstEntity();
        assertEquals(actionEntity.getClass(), SpeakActionEntity.class);
        assertEquals(actionEntity.getRevision(), putResponse.getDocumentRevision());
        assertEquals(((SpeakActionEntity)actionEntity).getSaid(), "Goodbye");


    }

    private String newId() {
        return String.valueOf(nextId++);
    }

    private WriteResponse putAction(ActionEntity actionEntity) {
        WriteResponse putResponse = couchDatabase
            .put()
            .entity(actionEntity)
            .execute();
        assertEquals(putResponse.getDocumentId(), "Action:" + actionEntity.getId());
        assertEquals(putResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertNotNull(putResponse.getDocumentRevision());

        actions.add(actionEntity);

        return putResponse;
    }
}
