package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraGasPrice {
    private String jsonrpc;
    private int id;
    private String result;

    public InfuraGasPrice() {}

    public GasPrice toGasPrice() {
        return new GasPrice(this.id, this.jsonrpc, Utils.getBigIntegerFromHexString(this.result));
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
