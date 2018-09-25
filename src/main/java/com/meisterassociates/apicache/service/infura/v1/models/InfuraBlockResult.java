package com.meisterassociates.apicache.service.infura.v1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meisterassociates.apicache.model.Block;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfuraBlockResult extends InfuraApiResult<InfuraBlock>{

    public InfuraBlockResult() {

    }

    @Override
    public String toString() {
        return this.result == null ? null : this.result.toString();
    }

    public Block toBlock() {
        if (this.result == null) {
            return null;
        }
        return this.result.toBlock(this.id, this.jsonrpc);
    }

}
