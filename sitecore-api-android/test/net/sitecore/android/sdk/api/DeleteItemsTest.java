package net.sitecore.android.sdk.api;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.DeleteItemsResponse;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class DeleteItemsTest extends MockedServerAndroidTestCase {

    @Test
    public void testAnonymousDelete() throws Exception {
        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());
        setMockResponse(TestData._200_delete_5_items);

        RequestFuture<DeleteItemsResponse> future = RequestFuture.newFuture();
        ScRequest request = session.deleteItems(future, future).build();
        assertEquals(Request.Method.DELETE, request.getMethod());

        mRequestQueue.add(request);

        DeleteItemsResponse response = future.get(10, TimeUnit.SECONDS);
        assertEquals(200, response.getStatusCode());
        assertEquals(5, response.getDeletedItemsIds().size());
    }

}
