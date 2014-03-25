package net.sitecore.android.sdk.api.model;

import com.android.volley.VolleyError;

public class ScError extends VolleyError {

    private int mStatusCode;

    public ScError(int statusCode, String message) {
        super(message);
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

}
