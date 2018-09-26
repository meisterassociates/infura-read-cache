package com.meisterassociates.apicache.service.block;

import com.meisterassociates.apicache.data.BlockCacheRepository;
import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import com.meisterassociates.apicache.util.QueryFilter;
import com.meisterassociates.apicache.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfuraBlockCacheService implements BlockService {
    private static final Logger logger = LogManager.getLogger(InfuraBlockCacheService.class);

    private BlockCacheRepository blockCache;
    private InfuraApiServiceBase infuraApi;

    @Autowired
    public InfuraBlockCacheService(BlockCacheRepository blockCache, InfuraApiServiceBase infuraApi) {
        this.blockCache = blockCache;
        this.infuraApi = infuraApi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockByHash(String blockHash) throws Exception {
        if (StringUtils.isEmpty(blockHash)) {
            return null;
        }

        var block = this.blockCache.getByHash(blockHash);
        if (block != null) {
            return block;
        }

        return this.fetchAndCacheBlockByHash(blockHash);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction getTransaction(String blockHash, String hexIndex) throws Exception {
        var block = this.getBlockByHash(blockHash);
        if (block == null || block.getTransactions() == null) {
            return null;
        }

        var transaction = block.getCastedTransactions().stream()
                .filter(trans -> trans.getTransactionIndex().equals(hexIndex))
                .findFirst();

        return transaction.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactionsByBlockHash(String blockHash, QueryFilter filter) throws Exception {
        var block = this.getBlockByHash(blockHash);
        if (block == null || block.getTransactions() == null) {
            return Collections.emptyList();
        }

        if (filter == null) {
            return block.getCastedTransactions();
        }

        return block.getCastedTransactions().stream()
                .skip(filter.getPage() * filter.getPageSize())
                .limit(filter.getPageSize())
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactionsTo(String blockHash, String recipientAddress, QueryFilter filter) throws Exception {
        return this.getTransactionsByBlockHash(blockHash, QueryFilter.ALL_ITEMS_QUERY_FILTER).stream()
                .filter(trans -> trans.getTo().equals(recipientAddress))
                .skip(filter.getPage()*filter.getPageSize())
                .limit(filter.getPageSize())
                .collect(Collectors.toList());
    }


    /**
     * Fetches the {@link Block} from Infura, inserts it into the cache, and returns it. This function is synchronized
     * on the block hash to prevent multiple fetches at once for the same block hash.
     *
     * @param blockHash the hash of the block to fetch
     * @return the block that is found or null
     * @throws Exception if there is an error fetching the block
     */
    private synchronized Block fetchAndCacheBlockByHash(String blockHash) throws Exception {

        // Ensures all strings with this same text share the same memory address so synchronization on the string works.
        var internedHash = blockHash.intern();

        synchronized (internedHash) {
            var cachedBlock = this.blockCache.getByHash(blockHash);
            if (cachedBlock != null) {
                return cachedBlock;
            }

            logger.debug("Fetching Block by hash [{}] from Infura...", blockHash);
            var block = this.infuraApi.getBlockByHash(blockHash);
            if (block == null) {
                return null;
            }

            // TODO: Maybe add Transactions' GasPrices to GasPriceCache with Transactions' date?
            this.blockCache.add(block);
            return block;
        }
    }
}
