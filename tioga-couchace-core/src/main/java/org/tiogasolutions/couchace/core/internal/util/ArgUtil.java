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

package org.tiogasolutions.couchace.core.internal.util;

public class ArgUtil {

    public static void assertNotNull(Object argValue) {
        assertNotNull(argValue, null);
    }

    public static void assertNotNull(Object argValue, String argName) {
        if (argValue == null) {
            if (argName != null) {
                throw new IllegalArgumentException("Argument \"" + argName + "\" is null.");
            } else {
                throw new IllegalArgumentException("Argument is null.");
            }
        }
    }

    public static void assertNotEmpty(String argValue, String argName) {
        if (argValue == null || argValue.trim().isEmpty()) {
            if (argName != null) {
                throw new IllegalArgumentException("Argument \"" + argName + "\" is empty.");
            } else {
                throw new IllegalArgumentException("Argument is empty.");
            }
        }
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}
