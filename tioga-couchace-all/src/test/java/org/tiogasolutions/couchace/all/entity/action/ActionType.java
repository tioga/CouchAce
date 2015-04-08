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

public enum ActionType {
    UNDEFINED(null),
    SPEAK(SpeakActionEntity.class),
    WALK(WalkActionEntity.class),
    SLEEP(SleepActionEntity.class);

    private final Class<? extends ActionEntity> actionClass;

    private ActionType(Class<? extends ActionEntity> actionClass) {
        this.actionClass = actionClass;
    }

    public Class<? extends ActionEntity> getActionClass() {
        return actionClass;
    }

    public boolean isUndefined() {
        return this == UNDEFINED;
    }

    public static ActionType findByName(String name) {
        for (ActionType eventClass : values()) {
            if (eventClass.name().equalsIgnoreCase(name)) {
                return eventClass;
            }
        }
        return UNDEFINED;
    }

}
