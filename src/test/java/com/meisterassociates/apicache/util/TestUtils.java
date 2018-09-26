package com.meisterassociates.apicache.util;

import com.meisterassociates.apicache.model.Block;
import com.meisterassociates.apicache.model.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static Block getBlock() {
        return getBlock(Utils.getHexString(new Object().hashCode()), LocalDateTime.now(), null);
    }

    public static Block getBlock(String hash) {
        return getBlock(hash, LocalDateTime.now(), null);
    }

    public static Block getBlock(LocalDateTime datetime) {
        return getBlock(Utils.getHexString(new Object().hashCode()), datetime, null);
    }

    public static Block getBlock(int numberOfTransactions) {
        return getBlock(Utils.getHexString(new Object().hashCode()), LocalDateTime.now(), numberOfTransactions);
    }

    public static Block getBlock(String hash, LocalDateTime datetime) {
        return getBlock(hash, datetime, null);
    }

    public static Block getBlock(String hash, LocalDateTime datetime, int numberOfTransactions) {
        var transactions = new ArrayList<Transaction>();
        for(int i = 0; i < numberOfTransactions; i++) {
            transactions.add(getTransaction(Utils.getHexString(i)));
        }
        return getBlock(hash, datetime, transactions);
    }

    public static Block getBlock(String hash, LocalDateTime datetime, List<Transaction> transactions) {
        return new Block(0, "", "", "", "", "", hash, "",
                "", "", "", "", "", "", "", "",
                "", "", "", "", null, transactions, datetime);
    }

    public static Transaction getTransaction(String index) {
        return getTransaction(index, Utils.getHexString(new Object().hashCode()), Utils.getHexString(new Object().hashCode()));
    }

    public static Transaction getTransaction(String index, String hash, String to) {
        return new Transaction("", "", "",to, "", "", hash, "",
                "","", "", index, "", "");
    }
}
