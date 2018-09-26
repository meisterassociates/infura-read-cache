package com.meisterassociates.apicache.service.health;

import com.meisterassociates.apicache.model.ApplicationHealth;

/**
 * Contract for all Health Check Service implementations to determine the health of the application.
 */
public interface HealthCheckServiceBase {

    /**
     * Gets the {@link ApplicationHealth} object conveying the health of the application.
     *
     * @return The application health
     */
    ApplicationHealth getApplicationHealth();
}
