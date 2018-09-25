package com.meisterassociates.apicache.service.block;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;
import com.meisterassociates.apicache.util.QueryFilter;

import java.util.List;

/**
 * Block service, coordinating the retrieval of and business logic related to {@link Block}s.
 */
public interface BlockService {

    /**
     * Gets the {@link Block} with the provided block hash.
     *
     * @param blockHash The hash of the block.
     * @return The block if one exists, else null.
     * @throws Exception if there is an error fetching the block
     */
    Block getBlockByHash(String blockHash) throws Exception;

    /**
     * Gets the {@link Transaction} at the provided index of the {@link Block} with the provided hash, if one exists.
     *
     * @param blockHash The hash of the block.
     * @param index The index of the transaction for which we're looking.
     * @return The transaction, if one exists, else null.
     * @throws Exception if there is an error fetching the block
     */
    Transaction getTransaction(String blockHash, int index) throws Exception;

    /**
     * Gets a {@link List<Transaction>} of the {@link Transaction}s within the provided {@link Block}.
     *
     * @param blockHash The hash of the block in which to search
     * @param filter The filter, paring down the size of the resulting list
     * @return The list of transactions
     * @throws Exception if there is an error fetching the block
     */
    List<Transaction> getTransactionsByBlockHash(String blockHash, QueryFilter filter) throws Exception;

    /**
     * Gets a {@link List<Transaction>} of the {@link Transaction}s within the provided {@link Block} to the provided address.
     *
     * @param blockHash The hash of the block in which to search
     * @param recipientAddress The address of the recipient for which transactions will be returned
     * @param filter The filter, paring down the size of the resulting list
     * @return The list of transactions
     * @throws Exception if there is an error fetching the block
     */
    List<Transaction> getTransactionsTo(String blockHash, String recipientAddress, QueryFilter filter) throws Exception;

    /**
     * Gets the number of {@link Transaction}s in the {@link Block} with the provided hash.
     *
     * @param blockHash The hash of the block in question.
     * @return The number of transactions if the block is found, 0 otherwise.
     * @throws Exception if there is an error fetching the block
     */
    default long getTransactionCountByBlockHash(String blockHash) throws Exception {
        var block = this.getBlockByHash(blockHash);
        if (block == null || block.getTransactions() == null) {
            return 0;
        }

        return block.getTransactions().size();
    }
}
