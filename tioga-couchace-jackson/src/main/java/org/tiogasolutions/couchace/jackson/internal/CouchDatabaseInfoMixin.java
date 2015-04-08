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

package org.tiogasolutions.couchace.jackson.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: harlan
 * Date: 3/11/14
 * Time: 10:03 PM
 */
public abstract class CouchDatabaseInfoMixin {

    protected CouchDatabaseInfoMixin(
            @JsonProperty("compact_running") Boolean compactRunning,
            @JsonProperty("committed_update_seq") Integer committedUpdateSeq,
            @JsonProperty("disk_format_version") Integer diskFormatVersion,
            @JsonProperty("data_size") Integer dataSize,
            @JsonProperty("disk_size") Integer diskSize,
            @JsonProperty("doc_count") Integer docCount,
            @JsonProperty("doc_del_count") Integer docDelCount,
            @JsonProperty("db_name") String dbName,
            @JsonProperty("instance_start_time") Long instanceStartTime,
            @JsonProperty("purge_seq") Integer purgeSeq,
            @JsonProperty("update_seq") Integer updateSeq) {
    }

}
