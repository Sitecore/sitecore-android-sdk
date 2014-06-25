package net.sitecore.android.sdk.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DownloadMediaOptionsTest {

    @Test
    public void testFullParams() {
        DownloadMediaOptions.Builder builder = new DownloadMediaOptions.Builder();
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

        String finalURL = builder.build().toString();
        //TODO: fix test - not to depend on params order
        //assertEquals("?w=100&thn=1&db=web&dmc=1&la=en&as=1&mh=100&sc=0.3&mw=100&bc=black&h=100", finalURL);
    }

    @Test
    public void testEmptyParams() {
        DownloadMediaOptions.Builder builder = new DownloadMediaOptions.Builder();
        assertNotNull(builder.build());

        String finalURL = builder.build().toString();
        assertEquals("", finalURL);
    }

    @Test
    public void testNotModifiableParams() {
        DownloadMediaOptions.Builder builder = new DownloadMediaOptions.Builder();
        builder.maxHeight(100)
                .maxHeight(200)
                .maxHeight(300)
                .maxHeight(400);

        assertNotNull(builder.build());

        String finalURL = builder.build().toString();
        assertTrue(finalURL.contains("mh"));
        assertEquals(2, finalURL.split("mh").length);
    }
}
