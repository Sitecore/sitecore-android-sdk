package net.sitecore.android.sdk.api;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

class PublicKeyRequest extends StringRequest {

    private static final String RSA_SUFFIX = "/-/item/v1/-/actions/getpublickey";

    public PublicKeyRequest(String instanceUrl, Listener<String> listener, ErrorListener errorListener) {
        super(instanceUrl + RSA_SUFFIX, listener, errorListener);
    }
}
