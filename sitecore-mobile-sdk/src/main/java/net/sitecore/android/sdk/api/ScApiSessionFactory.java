package net.sitecore.android.sdk.api;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Factory class for creation of {@link ScApiSession}.
 */
public class ScApiSessionFactory {

    /**
     * Asynchronously creates anonymous session to specified backend url.
     *
     * @param url       Host url with port
     * @param onSuccess is called immediately.
     *
     * @see #newAnonymousSession(String)
     */
    public static void getAnonymousSession(String url, final Response.Listener<ScApiSession> onSuccess) {
        onSuccess.onResponse(new ScApiSessionImpl(url));
    }

    /**
     * Asynchronously retrieves {@link ScPublicKey} and creates authenticated session.
     *
     * @param url       Sitecore instance URL / server URL.
     * @param name      User login name.
     * @param password  User password
     * @param onSuccess Success result callback.
     */
    public static void getSession(RequestQueue queue, String url,
            final String name,
            final String password,
            final Response.Listener<ScApiSession> onSuccess) {
        getSession(queue, url, name, password, onSuccess, null);
    }

    /**
     * Asynchronously retrieves {@link ScPublicKey} and creates authenticated session.
     *
     * @param url       Sitecore instance URL / server URL.
     * @param name      User login name.
     * @param password  User password
     * @param onSuccess Success result callback.
     * @param onError   Error result callback
     */
    public static void getSession(RequestQueue queue, final String url,
            final String name,
            final String password,
            final Response.Listener<ScApiSession> onSuccess,
            final Response.ErrorListener onError) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("Url can't be empty");

        Response.Listener<ScPublicKey> onKeyRetrieve = new Response.Listener<ScPublicKey>() {
            @Override
            public void onResponse(ScPublicKey response) {
                ScApiSession session = new ScApiSessionImpl(url, response.getKey(), name, password);
                onSuccess.onResponse(session);
            }
        };

        Request keyRequest = buildPublicKeyRequest(url, onKeyRetrieve, onError);
        queue.add(keyRequest);
    }

    /**
     * Creates anonymous session to specified backend url.
     *
     * @param url Host url with port
     *
     * @return new session.
     */
    public static ScApiSession newAnonymousSession(String url) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("Url can't be empty");
        return new ScApiSessionImpl(url);
    }

    /**
     * Creates authenticated session based on existing key.
     *
     * @param url      Sitecore instance URL / server URL.
     * @param key      {@link ScPublicKey} key for authenticated requests.
     * @param name     User login name.
     * @param password User password
     */
    public static ScApiSession newSession(String url, ScPublicKey key,
            final String name,
            final String password) {
        if (key == null) throw new IllegalArgumentException("Key can't be null");
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("Url can't be empty");

        return new ScApiSessionImpl(url, key.getKey(), name, password);
    }

    /**
     * Creates {@link com.android.volley.Request} for retrieving {@link ScPublicKey}.
     *
     * @param instanceUrl Sitecore instance URL / server URL.
     * @param onSuccess   Success result callback.
     * @param onError     Error result callback
     *
     * @return {@link com.android.volley.Request}.
     */
    public static Request buildPublicKeyRequest(String instanceUrl,
            final Response.Listener<ScPublicKey> onSuccess,
            final Response.ErrorListener onError) {
        if (TextUtils.isEmpty(instanceUrl)) throw new IllegalArgumentException("Url can't be empty");
        if (onSuccess == null) throw new IllegalArgumentException("onSuccess listener can't be null");

        final PublicKeyResponseListener responseHandler = new PublicKeyResponseListener(onSuccess, onError);

        return new PublicKeyRequest(instanceUrl, responseHandler, onError);
    }
}
