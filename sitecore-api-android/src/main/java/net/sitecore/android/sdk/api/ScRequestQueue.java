package net.sitecore.android.sdk.api;

import android.content.ContentResolver;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

import net.sitecore.android.sdk.api.internal.ContentProviderExecutorDelivery;

public class ScRequestQueue extends RequestQueue {

    static {
        final String userAgent = "Sitecore/1.3 Mobile SDK for Android";

        HttpStack stack = null;
        if (Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
        }

        sNetwork = new BasicNetwork(stack);
    }

    private static Network sNetwork;

    public ScRequestQueue(ContentResolver resolver) {
        this(resolver, new MainLooperExecutor());
    }

    public ScRequestQueue(ContentResolver resolver, Executor executor) {
        super(new NoCache(), sNetwork, 4, new ContentProviderExecutorDelivery(resolver, executor));
        start();
    }

    private static class MainLooperExecutor implements Executor {

        private final Handler mHandler;

        private MainLooperExecutor() {
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(Runnable command) {
            mHandler.post(command);
        }
    }
}
