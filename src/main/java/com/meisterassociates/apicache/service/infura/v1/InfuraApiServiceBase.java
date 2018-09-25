package com.meisterassociates.apicache.service.infura.v1;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.GasPrice;

public interface InfuraApiServiceBase {
    /**
     * Gets the current {@link GasPrice} in Wei from the Infura API
     *
     * @return The GasPrice object, parsed from the Infura API.
     */
    GasPrice getGasPriceInWei();

    /**
     * Gets the {@link Block} with the provided block hash from the Infura API, if one exists.
     *
     * @param hash the hash for the Block being requested.
     * @return The Block if one exists, else null
     */
    Block getBlockByHash(String hash);
}
