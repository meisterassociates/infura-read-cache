package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.CacheableModel;
import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.util.QueryFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory {@link CacheRepository<Block>} implementation for {@link Block} caching.
 */
@Repository
public class BlockMemoryCacheRepository implements BlockCacheRepository {

    @Value("${block.cache.purge.after.seconds}")
    private int purgeAfterSeconds;
    private ConcurrentHashMap<String, Block> cache;

    public BlockMemoryCacheRepository() {
        this.cache = new ConcurrentHashMap<>();
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
    public void add(Block block) {
        if (LocalDateTime.now().minusSeconds(this.purgeAfterSeconds).isAfter(block.getDatetime())) {
            return;
        }
        this.cache.put(block.getHash(), block);
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
                .filter(block -> block.getDatetime().isBefore(cutoffDateTime))
                .forEach(block -> this.cache.remove(block.getHash()));
    }
}
