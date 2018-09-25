package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraGasPriceResult extends InfuraApiResult<String> {

    public InfuraGasPriceResult() {}

    public GasPrice toGasPrice() {
        return new GasPrice(this.id, this.jsonrpc, Utils.getBigIntegerFromHexString(this.result));
    }
}
