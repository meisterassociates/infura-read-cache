package com.meisterassociates.apicache.service.health;

import com.meisterassociates.apicache.model.ApplicationHealth;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckService implements HealthCheckServiceBase {

    private InfuraApiServiceBase infuraApi;

    @Autowired
    public HealthCheckService(InfuraApiServiceBase infuraApi) {
        this.infuraApi = infuraApi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationHealth getApplicationHealth() {
        if (this.infuraApi.isConnectionHealthy()) {
            return ApplicationHealth.healthy();
        } else {
            return ApplicationHealth.unhealthy(List.of("Connection to Infura is unhealthy."));
        }
    }
}
