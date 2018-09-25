package com.meisterassociates.apicache.util;

import org.apache.tomcat.util.buf.HexUtils;

import java.math.BigInteger;

public class Utils {
    public static String getHexString(BigInteger bigInt) {
        return String.format("0x%s",HexUtils.toHexString(bigInt.toByteArray()));
    }

    public static BigInteger getBigIntegerFromHexString(String hexString) {
        return hexString.startsWith("0x") ? new BigInteger(hexString.substring(2), 16)
                                          : new BigInteger(hexString, 16);
    }
}
