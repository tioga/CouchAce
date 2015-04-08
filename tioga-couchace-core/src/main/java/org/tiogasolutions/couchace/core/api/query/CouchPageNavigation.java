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

public class CouchPageNavigation {
    private final int pageSize;
    private final String requestedPage;
    private final String previousPage;
    private final String nextPage;
    private final boolean includeDocs;

    public static CouchPageNavigation empty() {
        return new CouchPageNavigation(0, null, null, null, false);
    }

    public CouchPageNavigation(int pageSize, String requestedPage, String previousPage, String nextPage, boolean includeDocs) {
        this.pageSize = pageSize;
        this.requestedPage = requestedPage;
        this.previousPage = previousPage;
        this.nextPage = nextPage;
        this.includeDocs = includeDocs;
    }

    public CouchPageQuery queryNextPage() {
        return new CouchPageQuery(pageSize, nextPage, requestedPage, includeDocs);
    }

    public CouchPageQuery queryPreviousPage() {
        return new CouchPageQuery(pageSize, previousPage, requestedPage, includeDocs);
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getRequestedPage() {
        return requestedPage;
    }

    public boolean hasRequestedPage() {
        return requestedPage != null;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public boolean hasNextPage() {
        return nextPage != null;
    }

    public boolean hasPreviousPage() {
        return previousPage != null;
    }

    public boolean isIncludeDocs() {
        return includeDocs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouchPageNavigation that = (CouchPageNavigation) o;

        if (pageSize != that.pageSize) return false;
        if (nextPage != null ? !nextPage.equals(that.nextPage) : that.nextPage != null) return false;
        if (previousPage != null ? !previousPage.equals(that.previousPage) : that.previousPage != null) return false;
        if (requestedPage != null ? !requestedPage.equals(that.requestedPage) : that.requestedPage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageSize;
        result = 31 * result + (requestedPage != null ? requestedPage.hashCode() : 0);
        result = 31 * result + (previousPage != null ? previousPage.hashCode() : 0);
        result = 31 * result + (nextPage != null ? nextPage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageNavigation{" +
            "pageSize=" + pageSize +
            ", requestedPage='" + requestedPage + '\'' +
            ", previousPage='" + previousPage + '\'' +
            ", nextPage='" + nextPage + '\'' +
            '}';
    }
}
