package com.meisterassociates.apicache.service.block;

import com.meisterassociates.apicache.data.BlockMemoryCacheRepository;
import com.meisterassociates.apicache.service.infura.InfuraApiServiceBase;
import com.meisterassociates.apicache.util.QueryFilter;
import com.meisterassociates.apicache.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfuraBlockCacheServiceTest {

    @MockBean
    private InfuraApiServiceBase infuraApiMock;
    private BlockMemoryCacheRepository blockCache;
    private InfuraBlockCacheService blockCacheService;

    @Before
    public void setUp() {
        this.blockCache = new BlockMemoryCacheRepository();
        this.blockCache.setPurgeAfterSeconds(300);
        this.blockCacheService = new InfuraBlockCacheService(this.blockCache, this.infuraApiMock);
    }


    @Test
    public void testGetBlockByHashEmptyCache() throws Exception {
        var expectedBlock = TestUtils.getBlock();

        given(this.infuraApiMock.getBlockByHash(expectedBlock.getHash())).willReturn(expectedBlock);

        var block = this.blockCacheService.getBlockByHash(expectedBlock.getHash());

        assertThat(block).isEqualTo(expectedBlock);
    }

    @Test
    public void testGetBlockByHashOneBlockCached() throws Exception {
        var expectedBlock = TestUtils.getBlock();
        this.blockCache.add(expectedBlock);

        given(this.infuraApiMock.getBlockByHash(expectedBlock.getHash())).willReturn(TestUtils.getBlock());

        var block = this.blockCacheService.getBlockByHash(expectedBlock.getHash());

        assertThat(block).isEqualTo(expectedBlock);
    }

    @Test
    public void testGetBlockByHashTwoBlocksCached() throws Exception {
        var block1 = TestUtils.getBlock();
        var block2 = TestUtils.getBlock();
        var block3 = TestUtils.getBlock();
        this.blockCache.add(block1);
        this.blockCache.add(block2);

        given(this.infuraApiMock.getBlockByHash(block1.getHash())).willReturn(TestUtils.getBlock());
        given(this.infuraApiMock.getBlockByHash(block2.getHash())).willReturn(TestUtils.getBlock());
        given(this.infuraApiMock.getBlockByHash(block3.getHash())).willReturn(block3);

        // Block 1
        var block = this.blockCacheService.getBlockByHash(block1.getHash());
        assertThat(block).isEqualTo(block1);

        // Block 2
        block = this.blockCacheService.getBlockByHash(block2.getHash());
        assertThat(block).isEqualTo(block2);

        // Block 3
        block = this.blockCacheService.getBlockByHash(block3.getHash());
        assertThat(block).isEqualTo(block3);
    }

    @Test
    public void testGetBlockByHashTwoBlocksCachedOnePurged() throws Exception {
        this.blockCache.setPurgeAfterSeconds(10);
        var block1 = TestUtils.getBlock(LocalDateTime.now().minusSeconds(11));
        var block2 = TestUtils.getBlock();
        this.blockCache.add(block1);
        this.blockCache.add(block2);

        given(this.infuraApiMock.getBlockByHash(block1.getHash())).willReturn(TestUtils.getBlock());
        given(this.infuraApiMock.getBlockByHash(block2.getHash())).willReturn(TestUtils.getBlock());

        // Block 1
        var block = this.blockCacheService.getBlockByHash(block1.getHash());
        assertThat(block).isNotEqualTo(block1);

        // Block 2
        block = this.blockCacheService.getBlockByHash(block2.getHash());
        assertThat(block).isEqualTo(block2);
    }


    @Test
    public void testGetTransactionByBlockHashAndIndexEmptyCache() throws Exception {
        var block = TestUtils.getBlock(1);
        var expectedTransacton = block.getCastedTransactions().get(0);
        var transactionIndex = block.getCastedTransactions().get(0).getTransactionIndex();

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transaction = this.blockCacheService.getTransaction(block.getHash(), transactionIndex);

        assertThat(transaction).isEqualTo(expectedTransacton);
    }

    @Test
    public void testGetTransactionByBlockHashAndIndexBlockCached() throws Exception {
        var block = TestUtils.getBlock(1);
        this.blockCache.add(block);

        var expectedTransacton = block.getCastedTransactions().get(0);
        var transactionIndex = block.getCastedTransactions().get(0).getTransactionIndex();

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(TestUtils.getBlock(1));

        var transaction = this.blockCacheService.getTransaction(block.getHash(), transactionIndex);

        assertThat(transaction).isEqualTo(expectedTransacton);
    }

    @Test
    public void testGetTransactionsByBlockHashEmptyCacheDefaultQuery() throws Exception {
        var block = TestUtils.getBlock(3);
        var expectedTransactions = block.getCastedTransactions();

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsByBlockHash(block.getHash());

        assertThat(transactions.size()).isEqualTo(3);
        assertThat(transactions.get(0)).isEqualTo(expectedTransactions.get(0));
        assertThat(transactions.get(1)).isEqualTo(expectedTransactions.get(1));
        assertThat(transactions.get(2)).isEqualTo(expectedTransactions.get(2));
    }

    @Test
    public void testGetTransactionsByBlockHashEmptyCachePageFiltered() throws Exception {
        var block = TestUtils.getBlock(3);
        var expectedTransactions = block.getCastedTransactions();

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsByBlockHash(block.getHash(), new QueryFilter(0, 1));

        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0)).isEqualTo(expectedTransactions.get(0));
    }

    @Test
    public void testGetTransactionsByBlockHashEmptyCachePageFilteredOutOfRange() throws Exception {
        var block = TestUtils.getBlock(3);

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsByBlockHash(block.getHash(), new QueryFilter(4, 1));

        assertThat(transactions.size()).isEqualTo(0);
    }

    @Test
    public void testGetTransactionsToRecipientByBlockHashEmptyCacheDefaultFilter() throws Exception {
        var block = TestUtils.getBlock(3);
        var expectedTransaction = block.getCastedTransactions().get(0);

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsTo(block.getHash(), expectedTransaction.getTo());

        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0)).isEqualTo(expectedTransaction);
    }

    @Test
    public void testGetTransactionsToRecipientByBlockHashEmptyCachePageFilter() throws Exception {
        var block = TestUtils.getBlock(3);
        var expectedTransaction = block.getCastedTransactions().get(0);

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsTo(block.getHash(), expectedTransaction.getTo(), new QueryFilter(0, 1));

        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0)).isEqualTo(expectedTransaction);
    }

    @Test
    public void testGetTransactionsToRecipientByBlockHashEmptyCachePageFilterOutOfRange() throws Exception {
        var block = TestUtils.getBlock(3);
        var expectedTransaction = block.getCastedTransactions().get(0);

        given(this.infuraApiMock.getBlockByHash(block.getHash())).willReturn(block);

        var transactions = this.blockCacheService.getTransactionsTo(block.getHash(), expectedTransaction.getTo(), new QueryFilter(2, 1));

        assertThat(transactions.size()).isEqualTo(0);
    }

}
