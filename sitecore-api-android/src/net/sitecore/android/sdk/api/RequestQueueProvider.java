package net.sitecore.android.sdk.api;

import android.content.ContentResolver;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

import net.sitecore.android.sdk.api.provider.ScItemsProvider;

/**
 * Holds singletone instance of {@code RequestQueue} for network operations.
 *
 * @see RequestQueue
 */
public class RequestQueueProvider {

    private static RequestQueue mRequestQueue;

    /**
     * Constructs new {@code RequestQueue} if not exists or returns existing.
     *
     * @param context application context.
     *
     * @return {@code queue}
     */
    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = newSitecoreRequestQueue(context.getContentResolver());
        }
        return mRequestQueue;
    }

    /**
     * Returns {@link RequestQueue} without Volley cache, but which stores each successful {@link ScResponse}
     * in {@link ScItemsProvider}.
     *
     * @param resolver application {@code ContentResolver}.
     *
     * @return {@code queue}.
     */
    static RequestQueue newSitecoreRequestQueue(ContentResolver resolver) {
        final String userAgent = "Sitecore/1.0 Mobile SDK for Android";

        HttpStack stack = null;
        if (Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
        }

        final Network network = new BasicNetwork(stack);
        final ResponseDelivery delivery = new ContentProviderExecutorDelivery(resolver);
        final RequestQueue queue = new RequestQueue(new NoCache(), network, 4, delivery);
        queue.start();

        return queue;
    }

}
