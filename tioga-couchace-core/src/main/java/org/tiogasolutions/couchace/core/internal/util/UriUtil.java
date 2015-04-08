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

import java.net.URI;

/**
 * User: harlan
 * Date: 2/8/14
 * Time: 9:18 PM
 */
public class UriUtil {

    public static String lastPathElement(URI uri) {
        if (uri == null) {
            return null;
        }
        return lastPathElement(uri.getPath());
    }

    public static String lastPathElement(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf('/');
        if (index >= 0) {
            return path.substring(index + 1);
        } else {
            return null;
        }
    }

    public static String buildPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (path == null) {
                throw new IllegalArgumentException("Attempting to build path with null element, path so far is: " + sb.toString());
            }
            sb.append("/");
            sb.append(path);
        }
        return sb.toString();
    }

    public static String buildPathIgnoreNull(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (path != null) {
                sb.append("/");
                sb.append(path);
            }
        }
        return sb.toString();
    }

}
