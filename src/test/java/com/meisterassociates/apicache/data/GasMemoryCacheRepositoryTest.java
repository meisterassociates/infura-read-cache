package com.meisterassociates.apicache.data;

import com.meisterassociates.apicache.model.GasPrice;
import com.meisterassociates.apicache.util.QueryFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GasMemoryCacheRepositoryTest {

    private GasMemoryCacheRepository gasPriceCacheRepository;

    @Before
    public void setUp() {
        this.gasPriceCacheRepository = new GasMemoryCacheRepository();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
    }


    @Test
    public void testEmptyDefaultQuery() {
        var gasPrice = this.gasPriceCacheRepository.query();
        assertThat(gasPrice.size()).isEqualTo(0);
    }

    @Test
    public void testDefaultQuerySingleItemCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(expectedGasPrice);

        var gasPrice = this.gasPriceCacheRepository.query();

        assertThat(gasPrice.size()).isEqualTo(1);
        assertThat(gasPrice.get(0)).isEqualTo(expectedGasPrice);
    }

    @Test
    public void testDefaultQueryMultipleItemsCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(30));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query();

        assertThat(gasPrices.size()).isEqualTo(2);
        assertThat(gasPrices.get(0)).isEqualTo(gasPrice2);
        assertThat(gasPrices.get(1)).isEqualTo(gasPrice1);
    }

    @Test
    public void testValidQuerySingleItemCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var expectedGasPrice = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(expectedGasPrice);

        var gasPrices = this.gasPriceCacheRepository.query(new QueryFilter(0, 50, now.minusMinutes(1)));

        assertThat(gasPrices.size()).isEqualTo(1);
        assertThat(gasPrices.get(0)).isEqualTo(expectedGasPrice);
    }

    @Test
    public void testValidQueryMultipleItemsCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(30));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query(new QueryFilter(0, 50, now.minusMinutes(1)));

        assertThat(gasPrices.size()).isEqualTo(2);
        assertThat(gasPrices.get(0)).isEqualTo(gasPrice2);
        assertThat(gasPrices.get(1)).isEqualTo(gasPrice1);
    }

    @Test
    public void testSinceQueryMultipleItemsCachedOneFilteredOut() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(30));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query(new QueryFilter(now.minusSeconds(10)));

        assertThat(gasPrices.size()).isEqualTo(1);
        assertThat(gasPrices.get(0)).isEqualTo(gasPrice2);
    }

    @Test
    public void testPageQueryMultipleItemsCachedOneFilteredOut() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(30));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query(new QueryFilter(0, 1));

        assertThat(gasPrices.size()).isEqualTo(1);
        assertThat(gasPrices.get(0)).isEqualTo(gasPrice2);
    }

    @Test
    public void testPageQueryOutOfRangeMultipleItemsCached() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(30));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query(new QueryFilter(4, 1));

        assertThat(gasPrices.size()).isEqualTo(0);
    }

    @Test
    public void testDefaultQueryMultipleItemsCachedOnePurged() {
        var now = LocalDateTime.now();
        this.gasPriceCacheRepository.setPurgeAfterSeconds(300);
        var gasPrice1 = new GasPrice(0, "2.0", BigInteger.ONE, now.minusSeconds(301));
        var gasPrice2 = new GasPrice(0, "2.0", BigInteger.TWO, now);

        this.gasPriceCacheRepository.add(gasPrice1);
        this.gasPriceCacheRepository.add(gasPrice2);

        var gasPrices = this.gasPriceCacheRepository.query();

        assertThat(gasPrices.size()).isEqualTo(1);
        assertThat(gasPrices.get(0)).isEqualTo(gasPrice2);
    }
}
