package com.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Helper {

    /**
     * encrypt the string
     *
     * @param str some string need to be encode
     * @return MD5 string
     * @throws NoSuchAlgorithmException on security api call error
     */
    public static String encodeToMD5(String str) {
        String hashtext = "";
        try {
            String msg = str;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(msg.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            hashtext = number.toString(16);

            StringBuilder bld = new StringBuilder();
            for (int count = hashtext.length(); count < 32; count++) {
                bld.append("0");
            }
            hashtext = bld.toString() + hashtext;
        } catch (NoSuchAlgorithmException e) {
            // LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            // LOGGER.error(e.getMessage());
        }

        return hashtext;
    }

}
