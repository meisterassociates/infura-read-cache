package com.meisterassociates.apicache.model;

import java.time.LocalDateTime;

public abstract class CacheableModel {
    protected LocalDateTime datetime;

    public CacheableModel() {
        this.datetime = LocalDateTime.now();
    }

    public CacheableModel(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}
