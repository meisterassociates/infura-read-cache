package com.meisterassociates.apicache.model;

public class Transaction {
    private final String blockHash;
    private final String blockNumber;
    private final String from;
    private final String to;
    private final String gas;
    private final String gasPrice;
    private final String hash;
    private final String input;
    private final String nonce;
    private final String r;
    private final String s;
    private final String transactionIndex;
    private final String v;
    private final String value;

    public Transaction(String blockHash, String blockNumber, String from, String to, String gas, String gasPrice,
                       String hash, String input, String nonce, String r, String s, String transactionIndex, String v,
                       String value) {
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.hash = hash;
        this.input = input;
        this.nonce = nonce;
        this.r = r;
        this.s = s;
        this.transactionIndex = transactionIndex;
        this.v = v;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transaction)) {
            return false;
        }
        var other = (Transaction)obj;

        // TODO: Add other checks as necessary.
        return other.hash.equals(this.hash)
                && other.transactionIndex.equals(this.transactionIndex)
                && other.to.equals(this.to);
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public String getHash() {
        return hash;
    }

    public String getInput() {
        return input;
    }

    public String getNonce() {
        return nonce;
    }

    public String getR() {
        return r;
    }

    public String getS() {
        return s;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public String getV() {
        return v;
    }

    public String getValue() {
        return value;
    }
}
