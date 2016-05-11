package br.com.hrstatus.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CriptoJbossDSPassword {

    public static String encode(String secret) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        final byte[] kbytes = "jaas is the way".getBytes();
        final SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

        final Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] encoding = cipher.doFinal(secret.getBytes());
        final BigInteger n = new BigInteger(encoding);
        return n.toString(16);
    }
}