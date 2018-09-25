package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.GasPrice;

/**
 * Interface defining the contract for a {@link GasPrice} repository. For now, this is completely covered by
 * {@link CacheRepository<GasPrice>}.
 */
public interface GasCacheRepository extends CacheRepository<GasPrice> {
}
