package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraApiResult<T> {
    protected String jsonrpc;
    protected long id;
    protected T result;

    public void setResult(T result) {
        this.result = result;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
}
