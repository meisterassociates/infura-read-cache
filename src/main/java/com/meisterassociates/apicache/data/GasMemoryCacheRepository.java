package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.util.QueryFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * In-memory {@link CacheRepository<GasPrice>} implementation for {@link GasPrice} caching.
 */
@Repository
public class GasMemoryCacheRepository implements GasCacheRepository {

    @Value("${gas.cache.purge.after.seconds}")
    private int purgeAfterSeconds;
    private ConcurrentLinkedDeque<GasPrice> cache;

    public GasMemoryCacheRepository() {
        this.cache = new ConcurrentLinkedDeque<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(GasPrice gasPrice) {
        if (LocalDateTime.now().minusSeconds(this.purgeAfterSeconds).isAfter(gasPrice.getDatetime())) {
            return;
        }
        this.cache.addFirst(gasPrice);
        this.purge();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<GasPrice> query(QueryFilter filter) {
        this.purge();
        return this.cache.stream()
                .filter(gasPrice -> (filter.getSince() == null || gasPrice.getDatetime().isAfter(filter.getSince())))
                .skip(filter.getPage() * filter.getPageSize())
                .limit(filter.getPageSize())
                .collect(Collectors.toList());
    }

    /**
     * Purges items from the cache that are too old or cause the cache to be larger than its max size.
     */
    private void purge() {
        var cutoffDateTime = LocalDateTime.now().minusSeconds(this.purgeAfterSeconds);
        this.cache.removeIf(gasPrice -> gasPrice.getDatetime().isBefore(cutoffDateTime));
    }

    public void setPurgeAfterSeconds(int seconds) {
        this.purgeAfterSeconds = seconds;
    }
}
