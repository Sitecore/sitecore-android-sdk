package net.sitecore.android.sdk.api;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class ScPublicKey {
    private final String mXmlDsig;
    private final RSAPublicKey mKey;

    public ScPublicKey(String xmlDsig) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.mXmlDsig = xmlDsig;
        mKey = CryptoUtils.getPublicKey(xmlDsig);
    }

    public String toString() {
        return mXmlDsig;
    }

    public RSAPublicKey getKey() {
        return mKey;
    }
}
