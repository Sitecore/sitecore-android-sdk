package net.sitecore.android.sdk.api.model;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class represents request for retrieving rendering html.
 *
 * @see StringRequest
 */
public class GetRenderingHtmlRequest extends StringRequest {

    private final Map<String, String> mHeaders;

    /**
     * Creates a new request with the given URL, error listener and success listener.
     *
     * @param url           endpoint url
     * @param listener      success listener for request
     * @param errorListener error listener for request
     */
    public GetRenderingHtmlRequest(String url, Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        mHeaders = new HashMap<String, String>();
    }

    /**
     * Adds header for request.
     *
     * @param key   header name
     * @param value header value
     *
     * @see GetRenderingHtmlRequest#getHeaders()
     */
    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Returns request headers.
     *
     * @return {@code Map} of headers
     * @see GetRenderingHtmlRequest#addHeader(String, String)
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }
}
