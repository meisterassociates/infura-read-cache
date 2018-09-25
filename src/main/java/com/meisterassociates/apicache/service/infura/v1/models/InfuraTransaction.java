package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meisterassociates.apicache.model.Transaction;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraTransaction {
    private String blockHash;
    private String blockNumber;
    private String from;
    private String to;
    private String gas;
    private String gasPrice;
    private String hash;
    private String input;
    private String nonce;
    private String r;
    private String s;
    private String transactionIndex;
    private String v;
    private String value;

    public InfuraTransaction() {

    }

    public Transaction toTransaction() {
        return new Transaction(this.blockHash, this.blockNumber, this.from, this.to, this.gas, this.gasPrice, this.hash,
                this.input, this.nonce, this.r, this.s, this.transactionIndex, this.v, this.value);
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setR(String r) {
        this.r = r;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public void setV(String v) {
        this.v = v;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
