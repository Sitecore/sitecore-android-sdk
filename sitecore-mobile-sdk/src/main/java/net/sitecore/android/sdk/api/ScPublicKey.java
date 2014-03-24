package net.sitecore.android.sdk.api;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import net.sitecore.android.sdk.api.internal.CryptoUtils;

public class ScPublicKey {

    private final String mRawValue;
    private final RSAPublicKey mKey;

    public ScPublicKey(String rawValue) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.mRawValue = rawValue;
        mKey = CryptoUtils.getPublicKey(rawValue);
    }

    public String getRawValue() {
        return mRawValue;
    }

    public RSAPublicKey getKey() {
        return mKey;
    }
}
