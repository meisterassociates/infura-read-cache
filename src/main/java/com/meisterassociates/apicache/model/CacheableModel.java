package com.meisterassociates.apicache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public abstract class CacheableModel {
    protected LocalDateTime datetime;

    public CacheableModel() {
        this.datetime = LocalDateTime.now();
    }

    public CacheableModel(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    @JsonIgnore
    public LocalDateTime getDatetime() {
        return datetime;
    }
}
