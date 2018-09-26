package com.meisterassociates.apicache.service.health;

import com.meisterassociates.apicache.model.ApplicationHealth;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthCheckServiceTest {

    @MockBean
    private InfuraApiServiceBase infuraApiMock;
    private HealthCheckService healthCheckService;

    @Before
    public void setUp() {
        this.healthCheckService = new HealthCheckService(this.infuraApiMock);
    }


    @Test
    public void testHealthy() {
        given(this.infuraApiMock.isConnectionHealthy()).willReturn(true);

        var applicationHealth = this.healthCheckService.getApplicationHealth();

        assertThat(applicationHealth.getStatus()).isEqualTo(ApplicationHealth.HEALTHY_STATUS);
        assertThat(applicationHealth.getErrorMessages().size()).isEqualTo(0);
    }

    @Test
    public void testUnealthy() {
        given(this.infuraApiMock.isConnectionHealthy()).willReturn(false);

        var applicationHealth = this.healthCheckService.getApplicationHealth();

        assertThat(applicationHealth.getStatus()).isEqualTo(ApplicationHealth.UNHEALTHY_STATUS);
        assertThat(applicationHealth.getErrorMessages().size()).isGreaterThan(0);
    }
}
