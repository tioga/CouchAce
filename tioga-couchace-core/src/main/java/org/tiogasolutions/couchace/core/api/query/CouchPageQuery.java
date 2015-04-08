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

package org.tiogasolutions.couchace.core.api.query;

import org.tiogasolutions.couchace.core.internal.util.ArgUtil;

public class CouchPageQuery {
    private final int pageSize;
    private final String requestedPage;
    private final String currentPage;
    private final boolean includeDocs;

    public CouchPageQuery(int pageSize, String requestedPage, String currentPage, boolean includeDocs) {
        ArgUtil.assertNotNull(requestedPage, "requestedPage");
        this.pageSize = pageSize;
        this.requestedPage = requestedPage;
        this.currentPage = currentPage;
        this.includeDocs = includeDocs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getRequestedPage() {
        return requestedPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public boolean isForward() {
        return !isReverse();
    }

    public boolean isReverse() {
        return (requestedPage.contains("descending=true"));
    }

    public boolean isIncludeDocs() {
        return includeDocs;
    }

}
