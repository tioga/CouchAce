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

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WalkActionEntity extends AbstractActionEntity {

    private final int miles;

    public static WalkActionEntity newWalk(String id, String description, int miles) {
        return new WalkActionEntity(id, null, description, miles);
    }

    @JsonCreator
    protected WalkActionEntity(@JsonProperty("id") String id,
                               @JacksonInject("revision") String revision,
                               @JsonProperty("description") String description,
                               @JsonProperty("miles") int miles) {
        super(id, revision, description);
        this.miles = miles;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.WALK;
    }

    public int getMiles() {
        return miles;
    }
}
