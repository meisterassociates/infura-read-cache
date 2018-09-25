package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.CacheableModel;
import com.meisterassociates.apicache.model.Block;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory {@link CacheRepository<Block>} implementation for {@link Block} caching.
 */
public class BlockMemoryCacheRepository implements BlockCacheRepository {

    private static final int defaultPurgeAfterSeconds = 700_000;  // A little over a week.

    private ConcurrentHashMap<String, Block> cache;
    private final int purgeAfterSeconds;

    public BlockMemoryCacheRepository() {
        this.cache = new ConcurrentHashMap<>();
        this.purgeAfterSeconds = defaultPurgeAfterSeconds;
    }

    public BlockMemoryCacheRepository(int purgeAfterSeconds) {
        this.cache = new ConcurrentHashMap<>();
        this.purgeAfterSeconds = purgeAfterSeconds;
    }

    /**
     * {@inheritDoc}
     */
    public Block getByHash(String hash) {
        return this.cache.get(hash);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Block item) {
        this.cache.put(item.getHash(), item);
        this.purge();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Block> query(QueryFilter filter) {
        this.purge();
        return this.cache.values().stream()
                .sorted(Comparator.comparing(CacheableModel::getDatetime))
                .filter(item -> filter.getSince() == null || item.getDatetime().isAfter(filter.getSince()))
                .skip(filter.getPage() * filter.getPageSize())
                .limit(filter.getPageSize())
                .collect(Collectors.toList());

    }

    /**
     * Purges items from the cache that are too old or cause the cache to be larger than its max size.
     */
    private void purge() {
        var cutoffDateTime = LocalDateTime.now().minusSeconds(this.purgeAfterSeconds);

        this.cache.values().stream()
                .filter(item -> item.getDatetime().isBefore(cutoffDateTime))
                .forEach(item -> this.cache.remove(item.getHash()));
    }
}
