package com.meisterassociates.apicache.service.infura.v1;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import com.meisterassociates.apicache.service.infura.v1.models.InfuraBlockResult;
import com.meisterassociates.apicache.service.infura.v1.models.InfuraGasPriceResult;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class InfuraApiService implements InfuraApiServiceBase {
    private static final Logger logger = LogManager.getLogger(InfuraApiService.class);

    @Value("${infura.base.url}")
    private String infuraBaseUrl;

    private static final String ethGasPriceEndpoint = "eth_gasPrice";
    private static final String getBlockByHashEndpoint = "eth_getBlockByHash";
    private static final String parametersKeyword = "params";

    public InfuraApiService() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GasPrice getGasPriceInWei() throws Exception {
        var infuraGasPrice = this.executeRequest(infuraBaseUrl + ethGasPriceEndpoint, null, InfuraGasPriceResult.class);
        return infuraGasPrice.toGasPrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockByHash(String hash) throws Exception {
        var paramArray = String.format("[\"%s\",true]", hash);
        var parameters = Map.of(parametersKeyword, paramArray);
        var infuraBlock = this.executeRequest(infuraBaseUrl + getBlockByHashEndpoint, parameters, InfuraBlockResult.class);
        logger.debug("Received Block from Infura: {}", infuraBlock);

        return infuraBlock.toBlock();
    }

    private <T> T executeRequest(String urlString, Map<String, String> parameters, Class<T> clazz) throws Exception {
        logger.debug("Executing Infura fetch to url {} with querystring: {}", urlString, parameters);
        try {
            var httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            var requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            var restTemplate = new RestTemplate(requestFactory);

            if (parameters == null || parameters.isEmpty()) {
                return restTemplate.getForObject(urlString, clazz);
            } else {
                var urlBuilder = new StringBuilder(urlString);
                urlBuilder.append("?");
                for (Map.Entry<String, String> keyValue : parameters.entrySet()) {
                    urlBuilder.append(keyValue.getKey())
                              .append("=")
                              .append(keyValue.getValue())
                              .append("&");
                }
                return restTemplate.getForObject(urlBuilder.substring(0, urlBuilder.length() -1), clazz, parameters);
            }

        } catch (Exception ex) {
            logger.error(String.format("Error executing request to [%s] with parameters [%s]", urlString, parameters), ex);
            throw ex;
        }
    }
}
