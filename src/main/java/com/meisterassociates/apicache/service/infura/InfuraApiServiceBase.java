package com.meisterassociates.apicache.service.infura;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.GasPrice;

public interface InfuraApiServiceBase {

    /**
     * Determines whether or not our connection to Infura is healthy.
     *
     * @return true if it is, false otherwise.
     */
    boolean isConnectionHealthy();

    /**
     * Gets the current {@link GasPrice} in Wei from the Infura API
     *
     * @return The GasPrice object, parsed from the Infura API.
     */
    GasPrice getGasPriceInWei() throws Exception;

    /**
     * Gets the {@link Block} with the provided block hash from the Infura API, if one exists.
     *
     * @param hash the hash for the Block being requested.
     * @return The Block if one exists, else null
     */
    Block getBlockByHash(String hash) throws Exception;
}
