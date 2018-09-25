package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraBlock {

    private String difficulty;
    private String extraData;
    private String gasLimit;
    private String gasUsed;
    private String hash;
    private String logsBloom;
    private String miner;
    private String mixHash;
    private String nonce;
    private String number;
    private String parentHash;
    private String receiptsRoot;
    private String sha3Uncles;
    private String size;
    private String stateRoot;
    private String timestamp;
    private String totalDifficulty;
    private String transactionsRoot;
    private List<String> uncles;

    private List<InfuraTransaction> transactions;

    public InfuraBlock() {

    }

    @Override
    public String toString() {
        return String.format(", difficulty: %s, extraData: %s, gasLimit: %s, gasUsed: %s, hash: %s, logsBloom: %s, " +
                "miner: %s, mixHash: %s, nonce: %s, number: %s, receiptsRoot: %s, sha3Uncles: %s, size: %s, stateRoot: %s, " +
                "timestamp: %s, totalDifficulty: %s, transactionsRoot: %s, uncles: [%s], transactions: [%s]",
                difficulty, extraData, gasLimit, gasUsed, hash, logsBloom, miner, mixHash, nonce, number, receiptsRoot,
                sha3Uncles, size, stateRoot, timestamp, totalDifficulty, transactionsRoot, uncles, transactions);
    }

    public Block toBlock(long id, String jsonrpc) {
        if (StringUtils.isEmpty(this.hash)) {
            return null;
        }

        var transactions = new ArrayList<Transaction>();
        for (InfuraTransaction transaction : this.transactions) {
            transactions.add(transaction.toTransaction());
        }

        return new Block(id, jsonrpc, this.difficulty, this.extraData, this.gasLimit, this.gasUsed, this.hash,
                this.logsBloom, this.miner, this.mixHash, this.nonce, this.number, this.parentHash, this.receiptsRoot, this.sha3Uncles,
                this.size, this.stateRoot, this.timestamp, this.totalDifficulty, this.transactionsRoot, this.uncles, transactions);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public void setReceiptsRoot(String receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public void setSha3Uncles(String sha3Uncles) {
        this.sha3Uncles = sha3Uncles;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalDifficulty(String totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public void setTransactionsRoot(String transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public void setUncles(List<String> uncles) {
        this.uncles = uncles;
    }

    public void setTransactions(List<InfuraTransaction> transactions) {
        this.transactions = transactions;
    }

    public void setMixHash(String mixHash) {
        this.mixHash = mixHash;
    }
}
