package com.meisterassociates.apicache.service.gas;

import com.meisterassociates.apicache.data.GasMemoryCacheRepository;
import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfuraGasPriceCacheServiceTest {

    @MockBean
    private InfuraApiServiceBase infuraApiMock;
    private GasMemoryCacheRepository gasPriceCacheRepository;
    private InfuraGasPriceCacheService gasPriceCacheService;

    @Before
    public void setUp() {
        this.gasPriceCacheRepository = new GasMemoryCacheRepository();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        this.gasPriceCacheService = new InfuraGasPriceCacheService(this.gasPriceCacheRepository, this.infuraApiMock);
        this.gasPriceCacheService.setSecondsToCache(200);
    }


    @Test
    public void testGetCurrentGasPriceEmptyCache() throws Exception {
        var now = LocalDateTime.now();
        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);

        given(this.infuraApiMock.getGasPriceInWei()).willReturn(new GasPrice(0, "2.0", BigInteger.TWO, now));

        var gasPrice = this.gasPriceCacheService.getCurrentGasPrice();

        assertThat(gasPrice).isEqualTo(expectedGasPrice);
    }

    @Test
    public void testGetCurrentGasPriceSingleItemCached() throws Exception {
        var now = LocalDateTime.now();
        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(expectedGasPrice);
        this.gasPriceCacheService.setLastRefresh(LocalDateTime.now());

        given(this.infuraApiMock.getGasPriceInWei()).willReturn(new GasPrice(0, "2.0", BigInteger.ONE, now));

        var gasPrice = this.gasPriceCacheService.getCurrentGasPrice();

        assertThat(gasPrice).isEqualTo(expectedGasPrice);
    }

    @Test
    public void testGetCurrentGasPriceMultipleItemsCached() throws Exception {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.TWO, now.minusSeconds(10)));

        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);
        this.gasPriceCacheRepository.add(expectedGasPrice);
        this.gasPriceCacheService.setLastRefresh(LocalDateTime.now());

        given(this.infuraApiMock.getGasPriceInWei()).willReturn(new GasPrice(0, "2.0", BigInteger.ONE, now));

        var gasPrice = this.gasPriceCacheService.getCurrentGasPrice();

        assertThat(gasPrice).isEqualTo(expectedGasPrice);
    }

    @Test
    public void testGetAverageGasPriceSinceEmptyCache() throws Exception {
        var now = LocalDateTime.now();

        given(this.infuraApiMock.getGasPriceInWei()).willReturn(new GasPrice(0, "2.0", BigInteger.TWO, now));

        var gasPrice = this.gasPriceCacheService.getAverageGasPriceSince(now.minusHours(1));

        assertThat(gasPrice).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void testGetAverageGasPriceSinceSingleItemCached() throws Exception {
        var now = LocalDateTime.now();
        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(expectedGasPrice);
        this.gasPriceCacheService.setLastRefresh(LocalDateTime.now());

        var gasPrice = this.gasPriceCacheService.getAverageGasPriceSince(now.minusHours(1));

        assertThat(gasPrice).isEqualTo(expectedGasPrice.getPriceInWei());
    }

    @Test
    public void testGetAverageGasPriceSinceMultipleItemsCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.ONE, now));
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.TWO, now));
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.TEN, now));
        this.gasPriceCacheService.setLastRefresh(LocalDateTime.now());

        var gasPrice = this.gasPriceCacheService.getAverageGasPriceSince(now.minusHours(1));

        assertThat(gasPrice).isEqualTo(BigInteger.valueOf(13).divide(BigInteger.valueOf(3)));
    }

    @Test
    public void testGetAverageGasPriceSinceMultipleItemsCachedOneExcluded() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.ONE, now.minusDays(1)));
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.TWO, now));
        this.gasPriceCacheRepository.add(new GasPrice(0, "2.0", BigInteger.TEN, now));
        this.gasPriceCacheService.setLastRefresh(LocalDateTime.now());

        var gasPrice = this.gasPriceCacheService.getAverageGasPriceSince(now.minusHours(1));

        assertThat(gasPrice).isEqualTo(BigInteger.valueOf(12).divide(BigInteger.valueOf(2)));
    }

}
