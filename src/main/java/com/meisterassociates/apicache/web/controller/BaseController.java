package com.meisterassociates.apicache.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    protected static final String parametersQueryStringKey = "params";
    protected static final String pageQueryStringParameter = "page";
    protected static final String pageSizeQueryStringParameter = "pageSize";

    private static final String defaultJsonrpcString = "2.0";
    private static final long defaultId = 0;

    protected final Logger logger = LogManager.getLogger(getClass().getName());

    protected String[] getParameterArray(String parameters) {
        logger.debug("Parsing parameters: {}", parameters);
        if (StringUtils.isEmpty(parameters)) {
            return new String[]{};
        }

        parameters = URLDecoder.decode(parameters, StandardCharsets.UTF_8);

        return parameters.replace("[", "").replace("]", "").replace("\"", "").replace(" ", "").split(",");
    }

    @SuppressWarnings("unchecked")
    protected ResponseEntity getNullPayload() {
        var map = this.getBaseResponseMap();
        map.put("result", null);

        return ResponseEntity.ok(map);
    }

    protected <T> ResponseEntity getSuccessPayloadRaw(T payloadItem) {
        return ResponseEntity.status(HttpStatus.OK).body(payloadItem);
    }

    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity getSuccessPayload(T payloadItem) {
        var map = this.getBaseResponseMap();
        map.put("result", payloadItem);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @SuppressWarnings("unchecked")
    protected ResponseEntity getErrorPayload(HttpStatus status, String message) {
        var map = this.getBaseResponseMap();
        map.put("error", Map.of("code", -1, "message", message));
        return ResponseEntity.status(status).body(map);
    }

    @SuppressWarnings("unchecked")
    private Map getBaseResponseMap() {
        var map = new HashMap();
        map.put("id", defaultId);
        map.put("jsonrpc", defaultJsonrpcString);
        return map;
    }
}
