package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.GetRenderingHtmlRequest;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class GetRenderingHtmlTest extends MockedServerAndroidTestCase {

    @Test
    public void testUrlBuilder() throws Exception {
        ScApiSession session = ScApiSession.getAnonymousSession("http://sample.com");

        RequestFuture<String> future = RequestFuture.newFuture();
        GetRenderingHtmlRequest request = session.getRenderingHtml(
                "{2075CBFF-REND-434D-9E1B-937782E0DE49}",
                "{2075CBFF-ITEM-434D-9E1B-937782E0DE49}", future, future)
                .database("master")
                .setItemVersion(2)
                .setLanguage("en-US")
                .fromSite("/sitecore/shell")
                .addRenderingParameter("key1", "param1")
                .addRenderingParameter("key2", "param2")
                .build();

        final String expected = "http://sample.com/-/item/v1/sitecore/shell/-/actions/GetRenderingHtml?" +
                "renderingId=%7B2075CBFF-REND-434D-9E1B-937782E0DE49%7D" +
                "&sc_itemid=%7B2075CBFF-ITEM-434D-9E1B-937782E0DE49%7D" +
                "&sc_database=master&language=en-US&itemversion=2&key1=param1&key2=param2";

        assertEquals(expected, request.getUrl());
    }

}
