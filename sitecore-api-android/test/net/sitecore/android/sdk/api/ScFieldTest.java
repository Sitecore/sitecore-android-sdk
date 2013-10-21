package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScBaselistField;
import net.sitecore.android.sdk.api.model.ScCheckBoxField;
import net.sitecore.android.sdk.api.model.ScDateField;
import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScImageField;
import net.sitecore.android.sdk.api.model.ScItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ScFieldTest extends MockedServerAndroidTestCase {

    private ScItem mItem;

    @Before
    public void sendRequest() throws Exception {
        super.setUp();
        setMockResponse(TestData._200_1_item_20_fields);

        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());

        RequestFuture<ItemsResponse> future = RequestFuture.newFuture();
        mRequestQueue.add(session.getItems(future, future).build());

        ItemsResponse mItemsResponse = future.get(5, TimeUnit.SECONDS);

        assertEquals(200, mItemsResponse.getStatusCode());
        assertEquals(1, mItemsResponse.getItems().size());

        mItem = mItemsResponse.getItems().get(0);
        assertEquals(20, mItem.getFields().size());
    }

    @Test
    public void testDefault() throws Exception {
        ScField field1 = mItem.getFields().get(0);
        assertEquals("Text", field1.getName());
        assertEquals("Text", field1.getRawValue());
        assertEquals(ScField.Type.SINGLE_LINE_TEXT, field1.getType());

        ScField field19 = mItem.getFields().get(18);
        assertEquals("NotAllowedItem", field19.getName());
        assertEquals("Not_Allowed_Parent", field19.getRawValue());
        assertEquals(ScField.Type.DROPLIST, field19.getType());
    }

    @Test
    public void testCheckboxField() throws Exception {
        ScCheckBoxField checkboxField = (ScCheckBoxField) mItem.findFieldByName("CheckBoxField");
        assertTrue(checkboxField.isChecked());
        assertEquals("1", checkboxField.getRawValue());
        assertEquals(ScField.Type.CHECKBOX, checkboxField.getType());
    }

    @Test
    public void testListFields() {
        ScBaselistField checklistfield = (ScBaselistField) mItem.findFieldById("{BA2B9E54-AD3B-4648-8400-AF7935FBF5A0}");
        assertEquals("CheckListField", checklistfield.getName());
        assertEquals(1, checklistfield.getItemsIds().size());
        assertEquals("{2075CBFF-C330-434D-9E1B-937782E0DE49}", checklistfield.getRawValue());
        assertEquals(ScField.Type.CHECKLIST, checklistfield.getType());

        ScBaselistField treelistfield = (ScBaselistField) mItem.getFields().get(6);
        assertEquals("TreeListField", treelistfield.getName());
        assertEquals(2, treelistfield.getItemsIds().size());
        assertEquals("{2075CBFF-C330-434D-9E1B-937782E0DE49}|{00CB2AC4-70DB-482C-85B4-B1F3A4CFE643}", treelistfield.getRawValue());
        assertEquals(ScField.Type.TREELIST, treelistfield.getType());

        ScBaselistField droptreeField = (ScBaselistField) mItem.getFields().get(9);
        assertEquals("DropTreeFieldNormal", droptreeField.getName());
        assertEquals(1, droptreeField.getItemsIds().size());
        assertEquals("{2075CBFF-C330-434D-9E1B-937782E0DE49}", droptreeField.getRawValue());
        assertEquals(ScField.Type.DROPTREE, droptreeField.getType());
    }

    @Test
    public void testDateFields() {
        ScDateField dateField = (ScDateField) mItem.getFields().get(4);
        assertEquals("DateTimeField", dateField.getName());
        assertEquals(Long.parseLong("1328097600000"), dateField.getDate());
        assertEquals("20120201T120000", dateField.getRawValue());
        assertEquals(ScField.Type.DATE_TIME, dateField.getType());
    }

    @Test
    public void testImageField() {
        ScImageField imageField = (ScImageField) mItem.getFields().get(1);
        assertEquals("Image", imageField.getName());
        assertEquals("/~/media/4F20B519D5654472B01891CB6103C667.ashx", imageField.getImageSrcUrl());
        assertEquals(ScField.Type.IMAGE, imageField.getType());
    }
}
