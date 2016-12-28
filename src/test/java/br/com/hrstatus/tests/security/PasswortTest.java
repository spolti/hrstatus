package br.com.hrstatus.tests.security;

import br.com.hrstatus.security.PasswordUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class PasswortTest {

    private String password = "mySecretPassword";

    @Test
    public void testUserEncryptPassword() {
        Assert.assertEquals("dlSbgn7EbnBf0DgxgT+lIXIzjw38vXEe1EuBqW2sUcY=", PasswordUtils.encryptUserPassword("myPassword"));
    }

    @Test
    public void testEncodePassword() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Assert.assertEquals("108e108ad20f97979df9f1ea230fdbcc15e9f5eb0de8463c", PasswordUtils.encode(password));
    }

    @Test
    public void testDecodePassword() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Assert.assertEquals(password, String.valueOf(PasswordUtils.decode("108e108ad20f97979df9f1ea230fdbcc15e9f5eb0de8463c")));
    }

}