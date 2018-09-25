package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.Block;

public interface BlockCacheRepository extends CacheRepository<Block> {
    /**
     * Fetches the cached block with the provided hash, if one exists
     *
     * @param hash the hash to filter on.
     * @return The block with the provided Hash if there is one, else null
     */
    Block getByHash(String hash);
}
