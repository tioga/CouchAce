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

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: harlan
 * Date: 12/21/13
 * Time: 12:48 PM
 */
public class FileNameFilter implements FileFilter {
    private final Pattern pattern;
    private final boolean exclude;

    public FileNameFilter(Pattern pattern) {
        this(pattern, false);
    }

    public FileNameFilter(Pattern pattern, boolean exclude) {
        this.pattern = pattern;
        this.exclude = exclude;
    }

    public FileNameFilter(String nameRegEx) {
        this(Pattern.compile(nameRegEx), false);
    }

    public FileNameFilter(String nameRegEx, boolean exclude) {
        this(Pattern.compile(nameRegEx), exclude);
    }

    @Override
    public boolean accept(File file) {
        Matcher matcher = pattern.matcher(file.getName());
        if (isExclude()) {
            return !matcher.matches();
        } else {
            return matcher.matches();
        }
    }

    public Pattern getPattern() {
        return pattern;
    }

    public boolean isExclude() {
        return exclude;
    }
}
