package net.sitecore.android.sdk.api;

import android.app.Activity;
import android.content.Context;

import com.android.volley.RequestQueue;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;

public class MockedServerAndroidTestCase {

    MockWebServer mServer;
    RequestQueue mRequestQueue;

    @Before
    public void setUp() throws Exception {
        //ShadowLog.stream = System.out;
        mServer = new MockWebServer();
        mServer.play();
        mRequestQueue = RequestQueueProvider.newSitecoreRequestQueue(getContext().getContentResolver(),
                Executors.newSingleThreadExecutor());
    }

    @After
    public void tearDown() throws IOException {
        mServer.shutdown();
    }

    protected String getBackendUrl() {
        //mocked local server
        return mServer.getUrl("/").toExternalForm();
    }

    public Context getContext() {
        return Robolectric.buildActivity(Activity.class).create().get();
    }

    protected void setMockResponse(String response) {
        mServer.enqueue(new MockResponse().setBody(response));
    }

}
