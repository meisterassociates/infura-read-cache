package com.meisterassociates.apicache.service.block;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;

/**
 * Block service, coordinating the retrieval of and business logic related to {@link Block}s.
 */
public interface BlockService {

    /**
     * Gets the {@link Block} with the provided block hash.
     *
     * @param blockHash the hash of the block.
     * @return The block if one exists, else null.
     */
    Block getBlockByHash(String blockHash);

    /**
     * Gets the {@link Transaction} at the provided index of the {@link Block} with the provided hash, if one exists.
     *
     * @param blockHash the hash of the block.
     * @param index the index of the transaction for which we're looking.
     * @return The transaction, if one exists, else null.
     */
    Transaction getTransaction(String blockHash, int index);
}
