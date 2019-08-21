package com.goexp.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String encode(byte[] data) {

        if (data == null) {
            return null;
        }

        try {

            // Static getInstance method is called with hashing MD5
            var md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            var messageDigest = md.digest(data);

            // Convert byte array into signum representation
            var no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            var hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
