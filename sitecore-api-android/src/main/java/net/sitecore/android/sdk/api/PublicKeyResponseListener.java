package net.sitecore.android.sdk.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

class PublicKeyResponseListener implements Response.Listener<String> {

    private Response.Listener<ScPublicKey> mOnSuccess;
    private Response.ErrorListener mOnError;

    PublicKeyResponseListener(Response.Listener<ScPublicKey> success, Response.ErrorListener onError) {
        mOnSuccess = success;
        mOnError = onError;
    }

    @Override
    public void onResponse(String response) {
        try {
            LOGD("RSA public key received:" + response);
            ScPublicKey key = new ScPublicKey(response);
            mOnSuccess.onResponse(key);
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
