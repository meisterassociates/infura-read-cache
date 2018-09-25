package com.meisterassociates.apicache.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern hexPattern = Pattern.compile("^(0x)?[0-9a-fA-F]+$");
    private static final String hexPrefix = "0x";

    public static String getHexString(BigInteger bigInt) {
        return String.format("%s%s", hexPrefix, HexUtils.toHexString(bigInt.toByteArray()));
    }

    public static String getHexString(int integer) {
        return String.format("%s%s",hexPrefix, Integer.toHexString(integer));
    }

    public static BigInteger getBigIntegerFromHexString(String hexString) {
        return hexString.startsWith(hexPrefix) ? new BigInteger(hexString.substring(hexPrefix.length()), 16)
                                               : new BigInteger(hexString, 16);
    }

    public static int getIntFromHexString(String hexString) {
        return getBigIntegerFromHexString(hexString).intValue();
    }

    public static boolean isHex(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return false;
        }

        return hexPattern.matcher(hexString).matches();
    }
}
