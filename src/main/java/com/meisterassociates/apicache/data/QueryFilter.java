package com.meisterassociates.apicache.data;

import java.time.LocalDateTime;

/**
 * Base class representing a filter for a {@link CacheRepository} query.
 */
public class QueryFilter {
    private static final int defaultPage = 0;
    private static final int defaultPageSize = 25;

    private int page;
    private int pageSize;
    private String uniqueId;
    private LocalDateTime since;

    /**
     * Default QueryFilter constructor, populating the default page and pageSize.
     */
    public QueryFilter() {
        this.page = defaultPage;
        this.pageSize = defaultPageSize;
        this.since = null;
    }

    public QueryFilter(LocalDateTime since) {
        this(defaultPage, defaultPageSize, since);
    }

    /**
     * Constructs a QueryFilter with the provided page and pageSize.
     *
     * @param page the Page to filter by
     * @param pageSize the Page Size to use in filtering
     */
    public QueryFilter(int page, int pageSize) {
        if (page < 0) throw new IllegalArgumentException(String.format("QueryFilter expects a page >= 0. Received %d", page));
        if (pageSize <= 0) throw new IllegalArgumentException(String.format("QueryFilter expects a pageSize > 0. Received %d", pageSize));

        this.page = page;
        this.pageSize = pageSize;
        this.since = null;
    }

    /**
     * Constructs a QueryFilter with the provided page and pageSize.
     *
     * @param page the Page to filter by
     * @param pageSize the Page Size to use in filtering
     * @param since the date before which results will not be included
     */
    public QueryFilter(int page, int pageSize, LocalDateTime since) {
        if (page < 0) throw new IllegalArgumentException(String.format("QueryFilter expects a page >= 0. Received %d", page));
        if (pageSize <= 0) throw new IllegalArgumentException(String.format("QueryFilter expects a pageSize > 0. Received %d", pageSize));

        this.page = page;
        this.pageSize = pageSize;
        this.since = since;
    }


    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public LocalDateTime getSince() {
        return since;
    }
}
