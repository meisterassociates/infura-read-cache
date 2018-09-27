package com.meisterassociates.apicache.web.controller.v1;

import com.meisterassociates.apicache.model.ApplicationHealth;
import com.meisterassociates.apicache.service.health.HealthCheckServiceBase;
import com.meisterassociates.apicache.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController extends BaseController {

    @Autowired
    private HealthCheckServiceBase healthCheckService;

    @GetMapping("/v1/health")
    public ResponseEntity getHealthCheck() {
        var applicationHealth = this.healthCheckService.getApplicationHealth();

        if (applicationHealth.getStatus().equals(ApplicationHealth.HEALTHY_STATUS)) {
            return this.getSuccessPayload(applicationHealth);
        } else {
            return this.getErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, applicationHealth);
        }

    }
}
