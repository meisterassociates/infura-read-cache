package com.meisterassociates.apicache.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Representation of current application health, conveying a status and any errors.
 */
public class ApplicationHealth {
    public static final String HEALTHY_STATUS = "healthy";
    public static final String UNHEALTHY_STATUS = "unhealthy";

    private static final ApplicationHealth HEALTHY = new ApplicationHealth();


    private final String status;
    private final Collection<String> errorMessages;

    /**
     * Default constructor, creating a healthy ApplicationHealth.
     */
    private ApplicationHealth() {
        this.status = HEALTHY_STATUS;
        this.errorMessages = Collections.unmodifiableList(Collections.emptyList());
    }


    private ApplicationHealth(String status, List<String> errorMessages) {
        this.status = status;
        this.errorMessages = Collections.unmodifiableList(errorMessages);
    }

    /**
     * Factory method for unhealthy {@link ApplicationHealth} objects.
     *
     * @param errorMessages The error messages, if there are any
     * @return The unhealthy application health.
     */
    public static ApplicationHealth unhealthy(List<String> errorMessages) {
        errorMessages = errorMessages == null ? Collections.emptyList() : errorMessages;
        return new ApplicationHealth(UNHEALTHY_STATUS, errorMessages);
    }

    /**
     * Factory method for healthy {@link ApplicationHealth} objects.
     *
     * @return the healthy application health.
     */
    public static ApplicationHealth healthy() {
        return HEALTHY;
    }

    public String getStatus() {
        return status;
    }

    public Collection<String> getErrorMessages() {
        return errorMessages;
    }
}
