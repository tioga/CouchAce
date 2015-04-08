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

/**
 * User: harlan
 * Date: 4/14/12
 * Time: 8:48 PM
 */
public class StringUtil {

    public static String appendNullSafe(String appendTo, String appendValue) {
        if (appendValue == null) {
            return appendTo;
        } else if (appendTo == null) {
            return appendValue;
        } else {
            return appendTo + appendValue;
        }
    }

    public static String nullIfEmpty(String value) {
        return (value != null && value.length() > 0) ? value : null;
    }

    public static String nullIfBlank(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : null;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String arrayToString(Object[] array) {
        if (array == null || array.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder("[");
            for (Object object : array) {
                if (object != null) {
                    sb.append(object.toString());
                }
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     *
     * @param str -
     * @param delimiters -
     * @return String
     */
    public static String label(String str, char... delimiters) {
        if (delimiters == null || delimiters.length == 0) {
            delimiters = new char[]{' ', '_'};
        }
        if (isBlank(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                buffer[i] = ' ';
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            } else {
                buffer[i] = Character.toLowerCase(ch);
            }
        }
        return new String(buffer);
    }

    /**
     *
     * @param str -
     * @param delimiters -
     * @return String
     */
    public static String labelCapitalizeFirst(String str, char... delimiters) {
        if (delimiters == null || delimiters.length == 0) {
            delimiters = new char[]{' ', '_'};
        }
        if (isBlank(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                buffer[i] = ' ';
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            } else {
                buffer[i] = Character.toLowerCase(ch);
            }
        }
        return new String(buffer);
    }

    public static String capitalize(String str, char... delimiters) {
        if (delimiters == null || delimiters.length == 0) {
            delimiters = new char[]{' ', '_'};
        }
        if (isBlank(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }
}
