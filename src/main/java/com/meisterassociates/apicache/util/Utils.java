package com.meisterassociates.apicache.util;

import org.apache.tomcat.util.buf.HexUtils;

import java.math.BigInteger;

public class Utils {
    public static String getHexString(BigInteger bigInt) {
        return String.format("0x%s",HexUtils.toHexString(bigInt.toByteArray()));
    }
}
