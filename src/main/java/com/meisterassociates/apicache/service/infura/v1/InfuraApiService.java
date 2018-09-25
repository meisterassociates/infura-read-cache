package com.meisterassociates.apicache.service.infura.v1;

import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.model.Block;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class InfuraApiService implements InfuraApiServiceBase {
    /**
     * {@inheritDoc}
     */
    @Override
    public GasPrice getGasPriceInWei() {
        // TODO: Actually hit Infura
        return new GasPrice(0, "2.0", BigInteger.valueOf((long)(Math.random()*100000)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockByHash(String hash) {
        // TODO: Actually hit Infura
        return new Block(0, "2.0", "0x1231231");
    }
}
