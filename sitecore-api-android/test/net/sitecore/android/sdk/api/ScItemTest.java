package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScItem;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ScItemTest extends MockedServerAndroidTestCase {

    @Test
    public void testFindFieldByName() throws Exception {
        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());

        setMockResponse(TestData._200_simple_response);

        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        mRequestQueue.add(session.getItems(future, future).build());

        ItemsResponse response = future.get(5, TimeUnit.SECONDS);

        ScItem item = response.getItems().get(3);
        ScField field = item.findFieldByName("CheckBoxField");
        assertNotNull(field);

        assertEquals(ScField.Type.CHECKBOX, field.getType());
        assertEquals("CheckBoxField", field.getName());
        assertEquals("1", field.getRawValue());

        assertNull(item.findFieldByName("SomeUnknownName"));
    }

}
