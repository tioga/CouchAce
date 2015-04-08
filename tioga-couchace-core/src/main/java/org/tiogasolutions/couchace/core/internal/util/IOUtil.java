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

import org.tiogasolutions.couchace.core.api.CouchException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * User: harlan
 * Date: 12/21/13
 * Time: 11:57 AM
 */
public class IOUtil {

    public static File currentDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static File findDirNear(File startDir, String dirName) {

        if (dirName.equalsIgnoreCase(startDir.getName())) {
            return startDir;
        }

        // Is it a sub directory
        List<File> dirs = IOUtil.listDirs(startDir, new FileNameFilter(dirName));
        if (!dirs.isEmpty()) {
            return dirs.get(0);
        }

        // Is it a subdirectory of parent
        dirs = IOUtil.listDirs(startDir.getParentFile(), new FileNameFilter(dirName));
        if (!dirs.isEmpty()) {
            return dirs.get(0);
        }

        return null;
    }

    public static List<File> listDirs(File parentDir, FileFilter... fileFilters) {
        List<File> dirList = new ArrayList<>();

        File[] listFiles = null;
        if (parentDir != null && parentDir.exists()) {
            listFiles = parentDir.listFiles();
        }
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    boolean accepted = true;
                    for (FileFilter filter : fileFilters) {
                        if (!filter.accept(file)) {
                            // Rejected by filter so no need to look at others.
                            accepted = false;
                            break;
                        }
                    }
                    if (accepted) {
                        dirList.add(file);
                    }
                }
            }
        }
        return dirList;
    }

    public static String readText(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            throw CouchException.internalServerError(e);
        }
    }

    public static String readText(URI uri) {
        return readText(Paths.get(uri));
    }

    public static String readText(URL url) {
        if (url == null) {
            throw CouchException.badRequest("Null url argument given to readText");
        }
        try {
            return readText(Paths.get(url.toURI()));
        } catch (Exception e) {
            throw CouchException.internalServerError(e);
        }
    }

}
