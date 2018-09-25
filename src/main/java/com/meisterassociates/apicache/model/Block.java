package com.meisterassociates.apicache.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Block extends CacheableModel {

    private final int id;
    private final String jsonrpc;
    private final String hash;

    public Block(int id, String jsonrpc, String hash) {
        this(id, jsonrpc, hash, LocalDateTime.now());
    }

    public Block(int id, String jsonrpc, String hash, LocalDateTime datetime) {
        super(datetime);

        this.id = id;
        this.jsonrpc = jsonrpc;
        this.hash = hash;
    }

    @Override
    public String toString() {
        return String.format("{jsonrpc: %s, id: %s, hash: %s, datetime: %s}", this.jsonrpc, this.id,
                this.hash, datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public String getHash() {
        return hash;
    }
}
