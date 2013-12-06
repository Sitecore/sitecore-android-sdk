package net.sitecore.android.sdk.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;

import com.google.mockwebserver.Dispatcher;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.RecordedRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CancelRequestsTest extends MockedServerAndroidTestCase {
    private ScApiSession mSession;

    @Before
    public void init() throws Exception {
        super.setUp();

        mSession = ScApiSessionFactory.newAnonymousSession(getBackendUrl());

        mServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                Thread.sleep(2000);
                return new MockResponse().setBody(TestData._200_simple_response);
            }
        });
    }

    @Test
    public void testCancelRequest() {
        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        Request request = mSession.getItems(future, future).build();

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);
        request.cancel();

        try {
            future.get(3, TimeUnit.SECONDS);
            fail("Timeout Exception should be here");
        } catch (Exception e) {
            assertTrue(e instanceof TimeoutException);
        }
    }

    @Test
    public void testCancelOneRequest() throws InterruptedException, ExecutionException, TimeoutException {
        RequestFuture<ItemsResponse> future1 = RequestFuture.newFuture();
        Request request1 = mSession.getItems(future1, future1).build();

        request1.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestFuture<ItemsResponse> future2 = RequestFuture.newFuture();
        Request request2 = mSession.getItems(future2, future2).build();

        request2.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request2);

        request1.cancel();
        try {
            future1.get(3, TimeUnit.SECONDS);
            fail("TimeoutException should be here");
        } catch (Exception e) {
            assertTrue(e instanceof TimeoutException);
        }

        ItemsResponse response = future2.get(3, TimeUnit.SECONDS);
        assertNotNull(response);

        assertEquals(4, response.getItems().size());
    }
}
