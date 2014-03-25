package net.sitecore.android.sdk.api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

class UploadResultReceiver extends ResultReceiver {

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
        final String message = resultData.getString(Intent.EXTRA_TEXT);

        switch (resultCode) {
            case UploadMediaService.STATUS_OK:
                mSuccessListener.onResponse(message);
                break;

            case UploadMediaService.STATUS_ERROR:
                mErrorListener.onErrorResponse(new VolleyError(message));
                break;
        }
    }
}
