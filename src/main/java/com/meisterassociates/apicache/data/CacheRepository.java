package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.CacheableModel;

import java.util.List;

/**
 * Interface defining the contract for a CacheRepository, providing CRUD functionality for {@link CacheableModel}s.
 * @param <T> The CacheableModel for which this interface acts as a cache.
 */
public interface CacheRepository<T extends CacheableModel> {
    /**
     * Adds the provided {@link CacheableModel} to the cache.
     *
     * @param item The item to add to the cache.
     */
    void add(T item);

    /**
     * Performs the default query for the {@link CacheableModel} in question. Results are always in descending order of
     * {@link CacheableModel#datetime}.
     *
     * @return The {@link List<T>} of query result objects.
     */
    default List<T> query() {
        return this.query(new QueryFilter());
    }

    /**
     * Queries for all {@link CacheableModel}s matching the provided {@link QueryFilter}. Results are always in
     * descending order of {@link CacheableModel#datetime}.
     *
     * @param filter The filter to be applied to the query.
     * @return The {@link List<T>} of query result objects.
     */
    List<T> query(QueryFilter filter);

}
