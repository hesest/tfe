package be.hesest.tfe.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptionUtil {

    public static String encrypt(String value) throws NoSuchAlgorithmException {
        // Algorithme de cryptage en SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return String.format("%064x", new BigInteger(1, hash));
    }

}