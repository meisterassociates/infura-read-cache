package com.meisterassociates.apicache.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class BaseController {
    protected final Logger logger = LogManager.getLogger(getClass().getName());

    protected  <T> ResponseEntity getSuccessPayload(HttpStatus status, T payloadItem) {
        return ResponseEntity.status(status).body(payloadItem);
    }

    protected <T> ResponseEntity getSuccessPayloadSingleItem(HttpStatus status, T payloadItem) {
        var map = Map.of("result", payloadItem);
        return ResponseEntity.status(status).body(map);
    }

    protected ResponseEntity getErrorPayload(HttpStatus status, String message) {
        var map = Map.of("error", message);
        return ResponseEntity.status(status).body(map);
    }
}
