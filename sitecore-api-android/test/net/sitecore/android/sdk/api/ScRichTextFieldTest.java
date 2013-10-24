package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScItem;
import net.sitecore.android.sdk.api.model.ScRichtextField;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ScRichTextFieldTest extends MockedServerAndroidTestCase {

    private ScItem mItem;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setMockResponse(TestData._200_1_item_rich_text_test);

        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());

        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        mRequestQueue.add(session.getItems(future, future).build());

        ItemsResponse mItemsResponse = future.get(5, TimeUnit.SECONDS);

        assertEquals(200, mItemsResponse.getStatusCode());
        assertEquals(1, mItemsResponse.getItems().size());

        mItem = mItemsResponse.getItems().get(0);
        assertEquals(5, mItem.getFields().size());
    }

    @Test
    public void testRichTextNotChanged() throws Exception {
        ScRichtextField richtextField = (ScRichtextField) mItem.findFieldByName("Unchanged text");
        assertEquals(ScField.Type.RICHTEXT, richtextField.getType());

        String expectedHtml = TestData.rich_text_not_changed;
        String actualHtml = richtextField.getHtmlText(ScApiSession.getAnonymousSession("http://test/"));
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testRichTextNotChangedSingleQoutes() throws Exception {
        ScRichtextField richtextField = (ScRichtextField) mItem.findFieldByName("Unchanged text Single qoutes");
        assertEquals(ScField.Type.RICHTEXT, richtextField.getType());

        String expectedHtml = TestData.rich_text_test_notchanged_single_qoutes;
        String actualHtml = richtextField.getHtmlText(ScApiSession.getAnonymousSession("http://test/"));
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testRichTextConverted() throws Exception {
        ScRichtextField richtextField = (ScRichtextField) mItem.findFieldByName("Normal rich text");
        assertEquals(ScField.Type.RICHTEXT, richtextField.getType());

        String expectedHtml = TestData.rich_text_test_changed;
        String actualHtml = richtextField.getHtmlText(ScApiSession.getAnonymousSession("http://test/"));
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testRichTextConvertedSingleQoutes() throws Exception {
        ScRichtextField richtextField = (ScRichtextField) mItem.findFieldByName("Text with single qoutes");
        assertEquals(ScField.Type.RICHTEXT, richtextField.getType());

        String expectedHtml = TestData.rich_text_test_changed_single_qoutes;
        String actualHtml = richtextField.getHtmlText(ScApiSession.getAnonymousSession("http://test/"));
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testRichTextNonLinkUnchangedSingleQoutes() throws Exception {
        ScRichtextField richtextField = (ScRichtextField) mItem.findFieldByName("With Non link");
        assertEquals(ScField.Type.RICHTEXT, richtextField.getType());

        String expectedHtml = TestData.rich_text_test_nonlink_unchanged_singleqoutes;
        String actualHtml = richtextField.getHtmlText(ScApiSession.getAnonymousSession("http://test/"));
        assertEquals(expectedHtml, actualHtml);
    }
}
