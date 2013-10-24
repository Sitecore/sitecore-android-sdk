package net.sitecore.android.sdk.api;

import android.app.Activity;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        mRequestQueue = RequestQueueProvider.getRequestQueue(getContext());
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

    protected ScApiSession getApiSession() throws InterruptedException, ExecutionException, TimeoutException {
        setMockResponse(TestData.RSA_PUBLIC_KEY);

        RequestFuture<ScApiSession> sessionFuture = RequestFuture.newFuture();
        ScApiSession.getSession(getContext(), getBackendUrl(), "name", "pass", sessionFuture);

        return sessionFuture.get(10, TimeUnit.SECONDS);
    }

    protected void setMockResponse(String response) {
        mServer.enqueue(new MockResponse().setBody(response));
    }

}
