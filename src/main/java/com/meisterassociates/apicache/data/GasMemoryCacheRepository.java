package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.GasPrice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Repository
public class GasMemoryCacheRepository implements GasCacheRepository {
    private static final int defaultPurgeAfterSeconds = 700_000;  // A little over a week.

    private ConcurrentLinkedDeque<GasPrice> cache;
    private final int purgeAfterSeconds;

    public GasMemoryCacheRepository() {
        this.cache = new ConcurrentLinkedDeque<>();
        this.purgeAfterSeconds = defaultPurgeAfterSeconds;
    }

    public GasMemoryCacheRepository(int purgeAfterSeconds) {
        this.cache = new ConcurrentLinkedDeque<>();
        this.purgeAfterSeconds = purgeAfterSeconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(GasPrice item) {
        this.cache.addFirst(item);
        this.purge();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<GasPrice> query(QueryFilter filter) {
        this.purge();
        return this.cache.stream()
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
        this.cache.removeIf(item -> item.getDatetime().isBefore(cutoffDateTime));
    }
}
