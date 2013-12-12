package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.ScResponseParser;
import net.sitecore.android.sdk.api.UploadMediaService;

import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;

/**
 * Class represents response for {@link net.sitecore.android.sdk.api.CreateItemRequest}, {@link net.sitecore.android.sdk.api.UpdateItemFieldsRequest}, {@link net.sitecore.android.sdk.api.GetItemsRequest},
 * and response for  {@link UploadMediaService Uploading media item}.
 */
public class ItemsResponse extends ScResponse {

    private int mTotalCount;
    private int mResultCount;

    private List<ScItem> mItems;

    private ItemsResponse(int statusCode) {
        super(statusCode);
        mItems = new ArrayList<ScItem>();
    }

    public List<ScItem> getItems() {
        return mItems == null ? Collections.<ScItem>emptyList() : mItems;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public int getResultCount() {
        return mResultCount;
    }

    @Override
    protected ArrayList<ContentProviderOperation> toContentProviderOperations() {
        final ArrayList<ContentProviderOperation> operations = super.toContentProviderOperations();

        for (ScItem item : getItems()) {
            operations.add(item.toInsertOperation());
            operations.add(newDeleteFieldsOperation(item.getId()));
            for (ScField field : item.getFields()) {
                operations.add(field.toInsertOperation(item.getId()));
            }
        }
        return operations;
    }

    private ContentProviderOperation newDeleteFieldsOperation(String itemId) {
        final ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Fields.CONTENT_URI);
        builder.withSelection(Fields.ITEM_ID + "=?", new String[]{itemId});

        return builder.build();
    }

    public static class ItemsResponseParser extends ScResponseParser {

        @Override
        public ScResponse parseSuccess(int statusCode, JSONObject success) throws JSONException {
            ItemsResponse response = new ItemsResponse(statusCode);

            response.mTotalCount = success.getInt("totalCount");
            response.mResultCount = success.getInt("resultCount");

            JSONArray itemsJsonArray = success.getJSONArray("items");

            for (int i = 0; i < itemsJsonArray.length(); i++) {
                response.mItems.add(parseItem(itemsJsonArray.getJSONObject(i)));
            }

            return response;
        }

        private ScItem parseItem(JSONObject json) throws JSONException {
            ScItem item = new ScItem();
            item.setDisplayName(json.optString("DisplayName"));
            item.setDatabase(json.optString("Database"));
            item.setHasChildren(json.optBoolean("HasChildren"));
            item.setId(json.optString("ID"));
            item.setLanguage(json.optString("Language"));
            item.setLongId(json.optString("LongID"));
            item.setPath(json.optString("Path"));
            item.setTemplate(json.optString("Template"));
            item.setVersion(json.optInt("Version"));
            item.setHasChildren(json.optBoolean("HasChildren"));

            JSONObject fieldsObject = json.optJSONObject("Fields");

            Iterator<String> keyIterator = fieldsObject.keys();
            while (keyIterator.hasNext()) {
                String id = keyIterator.next();
                item.addField(parseField(id, fieldsObject.getJSONObject(id)));
            }
            return item;
        }

        private ScField parseField(String id, JSONObject json) {
            String name = json.optString("Name");
            String type = json.optString("Type");
            String value = json.optString("Value");

            return ScField.createFieldFromType(ScField.Type.getByName(type), name, id, value);
        }
    }
}