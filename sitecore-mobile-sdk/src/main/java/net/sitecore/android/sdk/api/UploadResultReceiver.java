package net.sitecore.android.sdk.api;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

class UploadResultReceiver extends ResultReceiver {
    static final String EXTRA_ERROR = "upload_error";
    static final String EXTRA_MESSAGE = "upload_message";

    private Response.Listener<String> mSuccessListener;
    private ErrorListener mErrorListener;

    public UploadResultReceiver(Handler handler,
            Response.Listener<String> successListener,
            ErrorListener errorListener) {
        super(handler);
        mSuccessListener = successListener;
        mErrorListener = errorListener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case UploadMediaService.STATUS_OK:
                mSuccessListener.onResponse(resultData.getString(EXTRA_MESSAGE));
                break;

            case UploadMediaService.STATUS_ERROR:
                VolleyError error = new VolleyError((Throwable) resultData.getSerializable(EXTRA_ERROR));
                mErrorListener.onErrorResponse(error);
                break;
        }
    }
}
