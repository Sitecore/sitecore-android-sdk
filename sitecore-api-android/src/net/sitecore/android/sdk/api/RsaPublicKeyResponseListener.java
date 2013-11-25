package net.sitecore.android.sdk.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

class RsaPublicKeyResponseListener implements Response.Listener<String> {

    private String mUrl;
    private String mName;
    private String mPassword;

    private boolean mToReturnRsaKey = false;

    private Response.Listener mOnSuccess;
    private Response.ErrorListener mOnError;

    RsaPublicKeyResponseListener(final String url,
            final String name,
            final String password,
            final Response.Listener<ScApiSession> onSuccess,
            final Response.ErrorListener onError) {
        mUrl = url;
        mName = name;
        mPassword = password;
        mOnSuccess = onSuccess;
        mOnError = onError;
    }

    RsaPublicKeyResponseListener(String url, Response.Listener<RSAPublicKey> success, Response.ErrorListener onError) {
        mUrl = url;
        mOnSuccess = success;
        mOnError = onError;
        mToReturnRsaKey = true;
    }

    @Override
    public void onResponse(String response) {
        try {
            LOGD("RSA public key received:" + response);
            RSAPublicKey pub = CryptoUtils.getPublicKey(response);
            if (mToReturnRsaKey) {
                mOnSuccess.onResponse(pub);
            } else {
                ScApiSession session = new ScApiSessionImpl(mUrl, pub, mName, mPassword);
                mOnSuccess.onResponse(session);
            }
        } catch (InvalidKeySpecException e) {
            sendError(e);
        } catch (NoSuchAlgorithmException e) {
            sendError(e);
        } catch (Exception e) {
            sendError(e);
        }
    }

    private void sendError(Throwable e) {
        if (mOnError != null) mOnError.onErrorResponse(new VolleyError(e));
    }
}
