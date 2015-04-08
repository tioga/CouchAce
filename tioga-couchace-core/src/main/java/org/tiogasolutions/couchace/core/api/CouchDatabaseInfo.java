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

package org.tiogasolutions.couchace.core.api;

/**
 * User: harlan
 * Date: 2/2/14
 * Time: 12:10 AM
 */
public class CouchDatabaseInfo {

    private final Boolean compactRunning;
    private final Integer committedUpdateSeq;
    private final Integer diskFormatVersion;
    private final Integer dataSize;
    private final Integer diskSize;
    private final Integer docCount;
    private final Integer docDelCount;
    private final String dbName;
    private final Long instanceStartTime;
    private final Integer purgeSeq;
    private final Integer updateSeq;

    public CouchDatabaseInfo(Boolean compactRunning,
                             Integer committedUpdateSeq,
                             Integer diskFormatVersion,
                             Integer dataSize,
                             Integer diskSize,
                             Integer docCount,
                             Integer docDelCount,
                             String dbName,
                             Long instanceStartTime,
                             Integer purgeSeq,
                             Integer updateSeq) {

        this.compactRunning = compactRunning;
        this.committedUpdateSeq = committedUpdateSeq;
        this.diskFormatVersion = diskFormatVersion;
        this.dataSize = dataSize;
        this.diskSize = diskSize;
        this.docCount = docCount;
        this.docDelCount = docDelCount;
        this.dbName = dbName;
        this.instanceStartTime = instanceStartTime;
        this.purgeSeq = purgeSeq;
        this.updateSeq = updateSeq;
    }

    public Boolean getCompactRunning() {
        return compactRunning;
    }

    public Integer getCommittedUpdateSeq() {
        return committedUpdateSeq;
    }

    public Integer getDiskFormatVersion() {
        return diskFormatVersion;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public Integer getDocDelCount() {
        return docDelCount;
    }

    public String getDbName() {
        return dbName;
    }

    public Long getInstanceStartTime() {
        return instanceStartTime;
    }

    public Integer getPurgeSeq() {
        return purgeSeq;
    }

    public Integer getUpdateSeq() {
        return updateSeq;
    }
}
