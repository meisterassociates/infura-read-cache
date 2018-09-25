package com.meisterassociates.apicache.service.infura.v1;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import com.meisterassociates.apicache.service.infura.v1.models.InfuraGasPrice;
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
    protected static final Logger logger = LogManager.getLogger(InfuraApiService.class);


    @Value("${infura.base.url}")
    private String infuraBaseUrl;

    private final String ethGasPriceEndpoint = "eth_gasPrice";
    private final String getBlockByHashEndpoint = "eth_getBlockByHash";
    private final String parametersKeyword = "params";

    public InfuraApiService() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GasPrice getGasPriceInWei() throws Exception {
        var infuraGasPrice = this.executeRequest(infuraBaseUrl + ethGasPriceEndpoint, null, InfuraGasPrice.class);
        return infuraGasPrice.toGasPrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockByHash(String hash) {
        // TODO: Actually hit Infura
        return new Block(0, "2.0", "0x1231231");
    }

    private <T> T executeRequest(String urlString, Map<String, String> parameters, Class<T> clazz) throws Exception {
        try {
            var httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            var requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            var restTemplate = new RestTemplate(requestFactory);
            if (parameters == null) {
                return restTemplate.getForObject(urlString, clazz);
            } else {
                return restTemplate.getForObject(urlString, clazz, parameters);
            }
        } catch (Exception ex) {
            logger.error(String.format("Error executing request to [%s] with parameters [%s]", urlString, parameters), ex);
            throw ex;
        }
    }
}
