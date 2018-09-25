package com.meisterassociates.apicache.service.gas;

import com.meisterassociates.apicache.data.CacheRepository;
import com.meisterassociates.apicache.data.QueryFilter;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Implementation of {@link GasPriceService} that caches {@link GasPrice}s periodically from Infura to fulfill requests.
 */
@Service
public class InfuraGasPriceCacheService implements GasPriceService{
    private static final Logger logger = LogManager.getLogger(InfuraGasPriceCacheService.class);

    @Value("${gas.price.refresh.seconds}")
    private int secondsToCache;
    @Autowired
    private CacheRepository<GasPrice> gasPriceCache;
    @Autowired
    private InfuraApiServiceBase infuraApi;
    private LocalDateTime lastRefresh;


    public InfuraGasPriceCacheService() {
        this.lastRefresh = LocalDateTime.MIN;
    }

    /**
     * {@inheritDoc}
     */
    public GasPrice getCurrentGasPrice() throws Exception {
        if (this.shouldFetchCurrentGasPrice()) {
            return this.fetchAndCacheCurrentGasPrice();
        }

        var results = this.gasPriceCache.query(new QueryFilter(0, 1));

        if (results.size() > 0) {
            logger.debug("Returning cached Gas Price: {}", results.get(0));
            return results.get(0);
        }
        return this.fetchAndCacheCurrentGasPrice();
    }

    /**
     * {@inheritDoc}
     */
    public BigInteger getAverageGasPriceSince(LocalDateTime since) {
        var gasPrices = this.gasPriceCache.query(new QueryFilter(0, Integer.MAX_VALUE, since));
        if (gasPrices.isEmpty()) {
            logger.debug("getAverageGasPriceSince: GasPrice Cache is empty.");
            return BigInteger.ZERO;
        }

        BigInteger sum = BigInteger.valueOf(0);
        for (GasPrice gasPrice: gasPrices) {
            sum = sum.add(gasPrice.getGasPriceInWei());
        }

        logger.debug("getAverageGasPriceSince: Dividing {} by {}.", sum, gasPrices.size());
        return sum.divide(BigInteger.valueOf(gasPrices.size()));
    }

    /**
     * Fetches the current {@link GasPrice} from Infura, adds it to the cache, and returns it. This is synchronized to
     * prevent multiple requests from hitting Infura at once. Only the first will hit Infura. The subsequent ones will
     * return the cached value.
     *
     * @return the newly-cached GasPrice.
     */
    private synchronized GasPrice fetchAndCacheCurrentGasPrice() throws Exception {
        if (! this.shouldFetchCurrentGasPrice()) {
            // This means that there was a race condition and some other request refreshed the cache first.
            return getCurrentGasPrice();
        }

        logger.debug("Fetching current Gas Price from Infura...");
        var gasPrice = this.infuraApi.getGasPriceInWei();

        this.gasPriceCache.add(gasPrice);
        this.lastRefresh = LocalDateTime.now();

        logger.debug("Gas Price from Infura: {}", gasPrice);
        return gasPrice;
    }

    /**
     * Determines whether or not it's time to fetch an updated {@link GasPrice} from Infura.
     *
     * @return true if the cache is stale, false otherwise.
     */
    private boolean shouldFetchCurrentGasPrice() {
        return LocalDateTime.now().minusSeconds(this.secondsToCache).isAfter(this.lastRefresh);
    }
}
