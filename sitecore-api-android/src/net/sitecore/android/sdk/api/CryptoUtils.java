package net.sitecore.android.sdk.api;

import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

class CryptoUtils {

    private static final String RSA_TRANSFORMATION_TYPE = "RSA/None/PKCS1Padding";

    static RSAPublicKey getPublicKey(String xmlDsig) throws InvalidKeySpecException, NoSuchAlgorithmException {
        boolean hasMod = xmlDsig.contains(Tags.MOD_START) && xmlDsig.contains(Tags.MOD_END);
        if (!hasMod) throw new IllegalArgumentException("RSA modulus not found in response");

        boolean hasExp = xmlDsig.contains(Tags.EXP_START) && xmlDsig.contains(Tags.EXP_END);
        if (!hasExp) throw new IllegalArgumentException("RSA exponent not found in response");

        String mod = xmlDsig.substring(xmlDsig.indexOf(Tags.MOD_START) + Tags.MOD_START.length(), xmlDsig.indexOf(Tags.MOD_END));
        String exp= xmlDsig.substring(xmlDsig.indexOf(Tags.EXP_START) + Tags.EXP_START.length(), xmlDsig.indexOf(Tags.EXP_END));

        BigInteger modulus = new BigInteger(1, Base64.decode(mod, Base64.DEFAULT));
        BigInteger exponent = new BigInteger(1, Base64.decode(exp, Base64.DEFAULT));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);

        return pub;
    }

    private interface Tags {
        String MOD_START = "<Modulus>";
        String MOD_END = "</Modulus>";

        String EXP_START = "<Exponent>";
        String EXP_END = "</Exponent>";
    }

    static String encodeRsaMessage(PublicKey publicKey, String message) throws InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        final Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal(message.getBytes());
        String encodedMessage = Base64.encodeToString(cipherData, Base64.DEFAULT).replace("\n", "").replace("\r", "");

        return encodedMessage;
    }
}
