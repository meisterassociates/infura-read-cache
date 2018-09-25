package com.meisterassociates.apicache.service.gas;

import com.meisterassociates.apicache.model.GasPrice;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Gas price service, coordinating the retrieval of and business logic related to {@link GasPrice}s.
 */
public interface GasPriceService {

    /**
     * Fetches the current {@link GasPrice}.
     *
     * @return the gas price.
     */
    GasPrice getCurrentGasPrice() throws Exception;

    /**
     * Gets the average {@link GasPrice} between the provided {@link LocalDateTime} and now.
     *
     * @param since the start of the date range for which we're getting the average.
     * @return The average gas price during the range in question.
     */
    BigInteger getAverageGasPriceSince(LocalDateTime since);
}
