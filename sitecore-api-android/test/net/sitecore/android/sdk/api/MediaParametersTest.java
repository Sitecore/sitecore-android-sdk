package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MediaParametersTest extends MockedServerAndroidTestCase {
    private ScItem mScItem;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        setMockResponse(TestData._200_1_media_item);

        ScApiSession mSession = ScApiSessionFactory.newAnonymousSession(getBackendUrl());
        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        mRequestQueue.add(mSession.getItems(future, future).byItemId(TestData.Content.MediaLibrary.TestImage.ID)
                .build());

        ItemsResponse response = future.get(5, TimeUnit.SECONDS);
        assertNotNull(response);

        assertEquals(1, response.getItems().size());
        mScItem = response.getItems().get(0);
    }

    @Test
    public void testFullParams() {
        MediaParameters.Builder builder = new MediaParameters.Builder();
        builder.allowStretch(true)
                .backgroundColor("black")
                .database("web")
                .disableMediaCaching(true)
                .height(100)
                .width(100)
                .language("en")
                .maxHeight(100)
                .maxWidth(100)
                .scale(0.3f)
                .thumbnail(true);

        assertNotNull(builder.build());

        String finalURL = mScItem.getMediaDownloadUrl(builder.build());
        assertEquals(TestData.media_url_with_all_params, finalURL);
    }

    @Test
    public void testEmptyParams() {
        MediaParameters.Builder builder = new MediaParameters.Builder();
        assertNotNull(builder.build());

        String finalURL = mScItem.getMediaDownloadUrl(builder.build());
        assertEquals(TestData.media_url_with_no_params, finalURL);
    }

    @Test
    public void testNotModifiableParams() {
        MediaParameters.Builder builder = new MediaParameters.Builder();
        builder.maxHeight(100)
                .maxHeight(200)
                .maxHeight(300)
                .maxHeight(400);

        assertNotNull(builder.build());

        String finalURL = mScItem.getMediaDownloadUrl(builder.build());
        assertTrue(finalURL.contains("mh"));
        assertEquals(2, finalURL.split("mh").length);
    }
}
