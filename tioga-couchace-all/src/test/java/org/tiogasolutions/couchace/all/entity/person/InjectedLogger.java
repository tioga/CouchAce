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

package org.tiogasolutions.couchace.all.entity.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Harlan
 * Date: 7/24/2014
 * Time: 12:27 AM
 */
public class InjectedLogger {
    private static final Logger log = LoggerFactory.getLogger(InjectedLogger.class);
    public void trace(String message) {
        log.trace("Logger message: " + message);
    }
}
