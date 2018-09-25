package com.meisterassociates.apicache.util;

import com.meisterassociates.apicache.data.CacheRepository;

import java.time.LocalDateTime;

/**
 * Base class representing a filter for a {@link CacheRepository} query.
 */
public class QueryFilter {
    public static final QueryFilter ALL_ITEMS_QUERY_FILTER = new QueryFilter(0, Integer.MAX_VALUE);

    public static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE = 0;

    private int page;
    private int pageSize;
    private LocalDateTime since;

    /**
     * Default QueryFilter constructor, populating the default page and pageSize.
     */
    public QueryFilter() {
        this.page = DEFAULT_PAGE;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.since = null;
    }

    public QueryFilter(LocalDateTime since) {
        this(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, since);
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
