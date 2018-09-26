package com.meisterassociates.apicache.web.controller.v1;

import com.meisterassociates.apicache.service.health.HealthCheckServiceBase;
import com.meisterassociates.apicache.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController extends BaseController {

    @Autowired
    private HealthCheckServiceBase healthCheckService;

    @GetMapping("/v1/health")
    public ResponseEntity getHealthCheck() {
        return this.getSuccessPayload(this.healthCheckService.getApplicationHealth());
    }
}
