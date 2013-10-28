package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ScItemTest extends MockedServerAndroidTestCase {

    private ItemsResponse mResponse;

    @Before @Override
    public void setUp() throws Exception {
        super.setUp();
        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());

        setMockResponse(TestData._200_simple_response);

        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        mRequestQueue.add(session.getItems(future, future).build());
        mResponse = future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testFindFieldByName() throws Exception {
        ScItem item = mResponse.getItems().get(3);
        ScField field = item.findFieldByName("CheckBoxField");
        assertNotNull(field);

        assertEquals(ScField.Type.CHECKBOX, field.getType());
        assertEquals("CheckBoxField", field.getName());
        assertEquals("1", field.getRawValue());

        assertNull(item.findFieldByName("SomeUnknownName"));
    }

    @Test
    public void getParentIdTest() {
        ScItem item = mResponse.getItems().get(0);
        assertEquals("{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}", item.getParentItemId());
    }

    @Test
    public void getItemsAncestorsIds() {
        ScItem item = mResponse.getItems().get(0);
        LinkedList<String> list = item.getItemAncestorsIds();

        assertEquals(3, list.size());
        assertEquals("{110D559F-DEA5-42EA-9C1C-8A5DF7E70EF9}", list.get(0));
        assertEquals("{0DE95AE4-41AB-4D01-9EB0-67441B7C2450}", list.get(1));
        assertEquals(ScItem.ROOT_ITEM_ID, list.get(2));
    }

}
