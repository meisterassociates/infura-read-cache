package com.meisterassociates.apicache.web.controller.v1;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;
import com.meisterassociates.apicache.service.block.BlockService;
import com.meisterassociates.apicache.util.QueryFilter;
import com.meisterassociates.apicache.util.Utils;
import com.meisterassociates.apicache.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BlockController extends BaseController {

    @Autowired
    private BlockService blockService;

    @GetMapping("/v1/jsonrpc/ropsten/eth_getBlockByHash")
    public ResponseEntity getBlockByHash(@RequestParam(parametersQueryStringKey) String params) {
        var paramsArray = this.getParameterArray(params);
        if (paramsArray.length != 2) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getBlockByHash requires a parameter array " +
                    "of length 2: [blockHash, showTransactionDetailsFlag]");
        }
        var blockHash = paramsArray[0];
        if ( ! Utils.isHex(blockHash)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getBlockByHash requires the `blockHash` " +
                    "to be a hash of the format 0x142afc9783de...]");
        }
        var booleanString = paramsArray[1].toLowerCase();
        if ( ! booleanString.equals("true") && ! booleanString.equals("false")) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getBlockByHash requires the `showTransactionDetailsFlag` " +
                    "to be either true or false]");
        }

        var showTransactionDetails = Boolean.parseBoolean(paramsArray[1]);

        try {
            var block = this.blockService.getBlockByHash(blockHash);
            if (block == null) {
                return this.getNullPayload();
            }

            return this.getSuccessPayload(formatBlockTransactions(block, showTransactionDetails));

        } catch (Exception ex) {
            logger.error("Error getting Block by Block Hash", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting Block by Block Hash. Please " +
                    "try back later.");
        }
    }

    @GetMapping("/v1/jsonrpc/ropsten/eth_getBlockTransactionCountByHash")
    public ResponseEntity getBlockTransactionCountByBlockHash(@RequestParam(parametersQueryStringKey) String params) {
        var paramsArray = this.getParameterArray(params);
        if (paramsArray.length != 1) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getBlockTransactionCountByHash requires a " +
                    "parameter array of length 1: [block hash]");
        }
        var blockHash = paramsArray[0];
        if ( ! Utils.isHex(blockHash)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getBlockTransactionCountByHash requires the " +
                    "`blockHash` to be a hash of the format 0x142afc9783de...]");
        }

        try {
            var transactionCount = this.blockService.getTransactionCountByBlockHash(blockHash);

            return this.getSuccessPayload(transactionCount);
        } catch (Exception ex) {
            logger.error("Error getting Transaction Count by Block Hash", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting Transaction count by Block " +
                    "Hash. Please try back later.");
        }
    }

    @GetMapping("/v1/jsonrpc/ropsten/eth_getTransactionByBlockHashAndIndex")
    public ResponseEntity getBlockTransactionByBlockHashAndIndex(@RequestParam(parametersQueryStringKey) String params) {
        var paramsArray = this.getParameterArray(params);
        if (paramsArray.length != 2) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getTransactionByBlockHashAndIndex requires a " +
                    "parameter array of length 2: [blockHash, indexHex]");
        }
        var blockHash = paramsArray[0];
        if ( ! Utils.isHex(blockHash)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getTransactionByBlockHashAndIndex requires the " +
                    "`blockHash` to be a hash of the format 0x142afc9783de...]");
        }
        var indexHex = paramsArray[1];
        if ( ! Utils.isHex(indexHex)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth_getTransactionByBlockHashAndIndex requires the " +
                    "`indexHex` to be a hex number of the format 0x142afc]");
        }

        var index = Utils.getIntFromHexString(indexHex);

        try {
            var transaction = this.blockService.getTransaction(blockHash, index);
            if (transaction == null) {
                return this.getNullPayload();
            }

            return this.getSuccessPayload(transaction);
        } catch (Exception ex) {
            logger.error("Error getting Transaction Count by Block Hash", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting Transaction count by Block " +
                    "Hash. Please try back later.");
        }
    }

    @GetMapping("/v1/cache/ropsten/eth/block/{blockHash}/transactions")
    public ResponseEntity getBlockTransactions(@PathVariable String blockHash,
                                         @RequestParam(value = pageQueryStringParameter, required = false) Integer page,
                                         @RequestParam(value = pageSizeQueryStringParameter, required = false) Integer pageSize) {
        page = page == null ? 0 : page;
        pageSize = pageSize == null ? 0 : pageSize;

        if (page < 0 || pageSize < 0) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "`page` and `pageSize` must be >= 0");
        }
        if ( ! Utils.isHex(blockHash)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth/block/<blockHash>/transactions endpoint requires " +
                    "the `blockHash` to be a hash of the format 0x142afc9783de...]");
        }

        pageSize = pageSize == 0 ? QueryFilter.DEFAULT_PAGE_SIZE : pageSize;
        try {
            var transactions = this.blockService.getTransactionsByBlockHash(blockHash, new QueryFilter(page, pageSize));
            return this.getSuccessPayload(transactions);

        } catch (Exception ex) {
            logger.error("Error getting Block Transactions", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting Block Transactions by Block " +
                    "Hash. Please try back later.");
        }
    }

    @GetMapping("/v1/cache/ropsten/eth/block/{blockHash}/transactions/to/{recipientAddress}")
    public ResponseEntity getBlockTransactions(@PathVariable String blockHash,
                                               @PathVariable String recipientAddress,
                                               @RequestParam(value = pageQueryStringParameter, required = false) Integer page,
                                               @RequestParam(value = pageSizeQueryStringParameter, required = false) Integer pageSize) {
        page = page == null ? 0 : page;
        pageSize = pageSize == null ? 0 : pageSize;

        if (page < 0 || pageSize < 0) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "`page` and `pageSize` must be >= 0");
        }
        if ( ! Utils.isHex(blockHash) || ! Utils.isHex(recipientAddress)) {
            return this.getErrorPayload(HttpStatus.BAD_REQUEST, "eth/block/<blockHash>/transactions/to/<recipientAddress} " +
                    "endpoint requires both the `blockHash` and `recipientAddress` to be a hash of the format 0x142afc9783de...]");
        }

        pageSize = pageSize == 0 ? QueryFilter.DEFAULT_PAGE_SIZE : pageSize;
        try {
            var transactions = this.blockService.getTransactionsTo(blockHash, recipientAddress, new QueryFilter(page, pageSize));
            return this.getSuccessPayload(transactions);

        } catch (Exception ex) {
            logger.error("Error getting Block Transactions to Recipient", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting Block Transactions to Recipient. Please try back later.");
        }
    }

    @GetMapping("/v1/jsonrpc/{network}/eth_getBlockByHash")
    public ResponseEntity getBlockByHashInvalid(@PathVariable String network) {
        var message = Map.of("error", String.format("Network [%s] is not supported for eth_getBlockByHash. Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    @GetMapping("/v1/jsonrpc/{network}/eth_getBlockTransactionCountByHash")
    public ResponseEntity getBlockTransactionCountByBlockHashInvalid(@PathVariable String network) {
        var message = Map.of("error", String.format("Network [%s] is not supported for eth_getBlockTransactionCountByHash. Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    @GetMapping("/v1/jsonrpc/{network}/eth_getTransactionByBlockHashAndIndex")
    public ResponseEntity getBlockTransactionByBlockHashAndIndexInvalid(@PathVariable String network) {
        var message = Map.of("error", String.format("Network [%s] is not supported for eth_getTransactionByBlockHashAndIndex. Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    @GetMapping("/v1/cache/{network}/eth/block/{blockHash}/transactions")
    public ResponseEntity getBlockTransactions(@PathVariable String network, @PathVariable String blockHash) {
        var message = Map.of("error", String.format("Network [%s] is not supported for /v1/cache/<network>/eth/block/<blockHash>/transactions. " +
                "Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    @GetMapping("/v1/cache/{network}/eth/block/{blockHash}/transactions/to/{recipientAddress}")
    public ResponseEntity getBlockTransactionsToRecipient(@PathVariable String network, @PathVariable String blockHash,
                                                          @PathVariable String recipientAddress) {
        var message = Map.of("error", String.format("Network [%s] is not supported for /v1/cache/<network>/eth/block/<blockHash>/transactions/to/<recipientAddress>. " +
                "Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    private static Block formatBlockTransactions(Block block, boolean showDetails) {
        if (showDetails) {
            return block;
        }

        @SuppressWarnings("unchecked")
        var transactions = ((List<Transaction>) block.getTransactions()).stream()
                .map(Transaction::getHash)
                .collect(Collectors.toList());

        return new Block(block.getId(), block.getJsonrpc(), block.getDifficulty(), block.getExtraData(), block.getGasLimit(),
                block.getGasUsed(), block.getHash(), block.getLogsBloom(), block.getMiner(), block.getMixHash(), block.getNonce(),
                block.getNumber(), block.getParentHash(), block.getReceiptsRoot(), block.getSha3Uncles(), block.getSize(),
                block.getStateRoot(), block.getTimestamp(), block.getTotalDifficulty(), block.getTransactionsRoot(),
                block.getUncles(), transactions);
    }
}
