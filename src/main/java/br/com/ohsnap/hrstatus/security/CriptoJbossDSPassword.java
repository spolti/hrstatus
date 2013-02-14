package br.com.ohsnap.hrstatus.security;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CriptoJbossDSPassword {

	public static String encode(String secret) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException {
		byte[] kbytes = "jaas is the way".getBytes();
		SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encoding = cipher.doFinal(secret.getBytes());
		BigInteger n = new BigInteger(encoding);
		return n.toString(16);
	}
}