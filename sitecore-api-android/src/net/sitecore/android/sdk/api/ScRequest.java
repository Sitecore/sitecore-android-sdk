package net.sitecore.android.sdk.api;

import android.content.ContentProviderOperation;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import net.sitecore.android.sdk.api.model.ScError;
import net.sitecore.android.sdk.api.model.ScErrorResponse;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

/**
 * Base class for all sdk network requests.
 *
 * @param <T> response type.
 */
public abstract class ScRequest<T extends ScResponse> extends Request<T> {

    private final Listener<T> mListener;
    private final Map<String, String> mHeaders;

    private final ArrayList<ContentProviderOperation> mBeforeSaveContentProviderOperations;

    protected Map<String, String> mBodyFields;

    /**
     * Creates a new request with the given URL, error listener, success listener and method type.
     *
     * @param method          method type, one of the {@link Method}
     * @param url             endpoint url
     * @param successListener success listener for request
     * @param errorListener   error listener for request
     */
    protected ScRequest(int method, String url, Listener<T> successListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        setShouldCache(false);
        mListener = successListener;

        mHeaders = new HashMap<String, String>();
        mBeforeSaveContentProviderOperations = new ArrayList<ContentProviderOperation>();
    }

    protected abstract ScResponse parseResponse(String response) throws JSONException;

    public void addBeforeSaveContentProviderOperation(ContentProviderOperation operation) {
        mBeforeSaveContentProviderOperations.add(operation);
    }

    public ArrayList<ContentProviderOperation> getBeforeSaveContentProviderOperations() {
        return mBeforeSaveContentProviderOperations;
    }

    /**
     * Adds header for request.
     *
     * @param key   header name
     * @param value header value
     *
     * @see ScRequest#getHeaders()
     */
    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Returns request headers.
     *
     * @return {@code Map} of headers
     * @see ScRequest#addHeader(String, String)
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    /**
     * Returns content type.
     *
     * @return {@code String} content type.
     */
    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LOGD(json);
            ScResponse scResponse = parseResponse(json);
            if (scResponse.isSuccess()) {
                return Response.success((T)scResponse, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new ScError(scResponse.getStatusCode(),
                        ((ScErrorResponse) scResponse).getErrorMessage()));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mBodyFields;
    }

    /**
     * Encodes body to UTF-8 and converts it to bytes.
     *
     * @return {@code byte[]} bytes or {@code null} if {@code UnsupportedEncodingException} happened.
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mBodyFields == null || mBodyFields.isEmpty()) {
            return "".getBytes();
        }

        final StringBuilder builder = new StringBuilder();
        boolean firstAdded = false;
        for (Map.Entry<String, String> entry : mBodyFields.entrySet()) {
            if (firstAdded) {
                builder.append('&');
            } else {
                firstAdded = true;
            }
            builder.append(Uri.encode(entry.getKey()));
            builder.append('=');
            builder.append(Uri.encode(entry.getValue()));
        }

        try {
            return builder.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns string representation of the {@code ScRequest}.
     *
     * @return the string representation of this {@code ScRequest}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        switch (getMethod()) {
            case Method.GET:
                builder.append("GET ");
                break;

            case Method.POST:
                builder.append("POST ");
                break;

            case Method.PUT:
                builder.append("PUT ");
                break;

            case Method.DELETE:
                builder.append("DELETE ");
                break;

        }

        builder.append(getUrl());
        if (mBodyFields != null && !mBodyFields.isEmpty()) {
            try {
                builder.append("\n").append(new String(getBody(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            } catch (AuthFailureError authFailureError) {
            }
        }

        return builder.toString();
    }
}