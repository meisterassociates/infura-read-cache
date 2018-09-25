package com.meisterassociates.apicache.model;

import com.meisterassociates.apicache.util.Utils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GasPrice extends CacheableModel {

    private final long id;
    private final String jsonrpc;
    private final BigInteger priceInWei;

    public GasPrice(long id, String jsonrpc, BigInteger priceInWei) {
        this(id, jsonrpc, priceInWei, LocalDateTime.now());
    }

    public GasPrice(long id, String jsonrpc, BigInteger priceInWei, LocalDateTime datetime) {
        super(datetime);

        this.id = id;
        this.jsonrpc = jsonrpc;
        this.priceInWei = priceInWei;
    }

    @Override
    public String toString() {
        return String.format("{jsonrpc: %s, id: %s, priceInWei: %s, datetime: %s}", this.jsonrpc, this.id,
                this.priceInWei, datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public Map toMap() {
        return Map.of("jsonrpc", this.jsonrpc, "id", this.id, "result", Utils.getHexString(this.priceInWei));
    }

    public BigInteger getGasPriceInWei() {
        return this.priceInWei;
    }
}
