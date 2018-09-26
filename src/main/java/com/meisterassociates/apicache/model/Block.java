package com.meisterassociates.apicache.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Block extends CacheableModel {

    private final long id;
    private final String jsonrpc;

    private final String difficulty;
    private final String extraData;
    private final String gasLimit;
    private final String gasUsed;
    private final String hash;
    private final String logsBloom;
    private final String miner;
    private final String mixHash;
    private final String nonce;
    private final String number;
    private final String parentHash;
    private final String receiptsRoot;
    private final String sha3Uncles;
    private final String size;
    private final String stateRoot;
    private final String timestamp;
    private final String totalDifficulty;
    private final String transactionsRoot;

    private final List<String> uncles;
    private final List transactions;

    public Block(long id, String jsonrpc, String difficulty, String extraData, String gasLimit, String gasUsed,
                 String hash, String logsBloom, String miner, String mixHash, String nonce, String number, String parentHash,
                 String receiptsRoot, String sha3Uncles, String size, String stateRoot, String timestamp,
                 String totalDifficulty, String transactionsRoot, List<String> uncles, List transactions) {

        this(id, jsonrpc, difficulty, extraData, gasLimit, gasUsed, hash, logsBloom, miner, mixHash, nonce, number, parentHash,
                receiptsRoot, sha3Uncles, size, stateRoot, timestamp, totalDifficulty, transactionsRoot, uncles,
                transactions, LocalDateTime.now());
    }

    public Block(long id, String jsonrpc, String difficulty, String extraData, String gasLimit, String gasUsed,
                 String hash, String logsBloom, String miner, String mixHash, String nonce, String number, String parentHash,
                 String receiptsRoot, String sha3Uncles, String size, String stateRoot, String timestamp,
                 String totalDifficulty, String transactionsRoot, List<String> uncles, List transactions,
                 LocalDateTime datetime) {
        super(datetime);

        uncles = uncles == null ? Collections.emptyList() : uncles;
        transactions = transactions == null ? Collections.emptyList() : transactions;

        this.id = id;
        this.jsonrpc = jsonrpc;
        this.difficulty = difficulty;
        this.extraData = extraData;
        this.gasLimit = gasLimit;
        this.gasUsed = gasUsed;
        this.hash = hash;
        this.logsBloom = logsBloom;
        this.miner = miner;
        this.mixHash = mixHash;
        this.nonce = nonce;
        this.number = number;
        this.parentHash = parentHash;
        this.receiptsRoot = receiptsRoot;
        this.sha3Uncles = sha3Uncles;
        this.size = size;
        this.stateRoot = stateRoot;
        this.timestamp = timestamp;
        this.totalDifficulty = totalDifficulty;
        this.transactionsRoot = transactionsRoot;
        this.uncles = Collections.unmodifiableList(uncles);
        this.transactions = Collections.unmodifiableList(transactions);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block)) {
            return false;
        }
        var other = (Block)obj;

        // TODO: Add other checks as necessary.
        return other.id == this.id
                && other.jsonrpc.equals(this.jsonrpc)
                && other.hash.equals(this.hash);
    }

    @Override
    public String toString() {
        // Add more details as necessary.
        return String.format("{jsonrpc: %s, id: %s, hash: %s, datetime: %s}", this.jsonrpc, this.id,
                this.hash, datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public long getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getHash() {
        return hash;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getExtraData() {
        return extraData;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public String getMiner() {
        return miner;
    }

    public String getMixHash() {
        return mixHash;
    }

    public String getNonce() {
        return nonce;
    }

    public String getNumber() {
        return number;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getReceiptsRoot() {
        return receiptsRoot;
    }

    public String getSha3Uncles() {
        return sha3Uncles;
    }

    public String getSize() {
        return size;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTotalDifficulty() {
        return totalDifficulty;
    }

    public String getTransactionsRoot() {
        return transactionsRoot;
    }

    public List<String> getUncles() {
        return uncles;
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<Transaction> getCastedTransactions() {
        return (List<Transaction>)transactions;
    }

    public List getTransactions() {
        return transactions;
    }

}
