package com.meisterassociates.apicache.web.controller;

import com.meisterassociates.apicache.service.gas.GasPriceService;
import com.meisterassociates.apicache.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class GasController extends BaseController {

    @Autowired
    private GasPriceService gasPriceService;

    @GetMapping("/v1/jsonrpc/ropsten/eth_gasPrice")
    public ResponseEntity getGasPrice() {
        try {
            return this.getSuccessPayload(HttpStatus.OK, this.gasPriceService.getCurrentGasPrice().toMap());
        } catch (Exception ex) {
            logger.error("Error getting Current Gas Price", ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting current Gas Price. Please try back later.");
        }
    }

    @GetMapping("/v1/cache/ropsten/eth/gas/price/average/{hours}h")
    public ResponseEntity getGasPriceAverageLastDay(@PathVariable int hours) {
        try {
            var averagePrice = this.gasPriceService.getAverageGasPriceSince(LocalDateTime.now().minusHours(hours));
            return this.getSuccessPayloadSingleItem(HttpStatus.OK, Utils.getHexString(averagePrice));
        } catch (Exception ex) {
            var message = String.format("Error getting average Gas Price for the past %s hours", hours);
            logger.error(message, ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    @GetMapping("/v1/jsonrpc/{network}/eth_gasPrice")
    public ResponseEntity getGasPrice(@PathVariable String network) {
        var message = Map.of("error", String.format("Network [%s] is not supported for eth_gasPrice. Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }

    @GetMapping("/v1/cache/{network}/eth/gas/price/average/{hours}h")
    public ResponseEntity getGasPriceAverageLastDay(@PathVariable String network, @PathVariable int hours) {
        var message = Map.of("error", String.format("Network [%s] is not supported for average Gas Price. Please use ropsten", network));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
    }


}
