package ru.endoskop.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PassEncode {
    public static String encode(String st) {
        MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PassEncode.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
 
        return md5Hex;
    }
}
