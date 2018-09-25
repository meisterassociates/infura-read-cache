package com.meisterassociates.apicache.web.controller.v1;

import com.meisterassociates.apicache.web.controller.BaseController;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InfuraProxyController extends BaseController {
    @Value("${infura.server.url}")
    private String infuraServerUrl;

    @RequestMapping("/**")
    public ResponseEntity mirrorRest(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request) {

        // Create HTTPS Request Factory
        var httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        // Build new URI
        var uri = UriComponentsBuilder.fromUriString(this.infuraServerUrl)
                                      .path(request.getRequestURI())
                                      .query(request.getQueryString())
                                      .build(true)
                                      .toUri();
        logger.info("Proxying request for URI: {}", uri);

        // Add all request headers
        var headers = new HttpHeaders();
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (! headerName.equals("host")) {
                headers.set(headerName, request.getHeader(headerName));
            }
        }

        var httpEntity = new HttpEntity<>(body, headers);

        // Execute the request
        try {
            return new RestTemplate(requestFactory).exchange(uri, method, httpEntity, String.class);
        } catch (Exception ex) {
            logger.error(String.format("Error proxying request for %s", request.getRequestURI()), ex);
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error fulfilling your request.");
        }
    }
}
