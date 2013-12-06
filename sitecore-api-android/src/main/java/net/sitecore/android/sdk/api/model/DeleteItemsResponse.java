package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.ScResponseParser;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/** Class represents response for {@link DeleteItemsRequest}. */
public class DeleteItemsResponse extends ScResponse {

    private List<String> mDeletedItemIds;

    private int mCount;

    private DeleteItemsResponse(int statusCode) {
        super(statusCode);
        mDeletedItemIds = new ArrayList<String>();
    }

    public List<String> getDeletedItemsIds() {
        return mDeletedItemIds;
    }

    public int getDeletedCount() {
        return mCount;
    }

    @Override
    protected ArrayList<ContentProviderOperation> toContentProviderOperations() {
        final ArrayList<ContentProviderOperation> operations = super.toContentProviderOperations();

        for (String itemId : getDeletedItemsIds()) {
            final ContentProviderOperation.Builder deleteFields = ContentProviderOperation.newDelete(Fields.CONTENT_URI);
            deleteFields.withSelection(Fields.ITEM_ID + "=?", new String[]{itemId});
            operations.add(deleteFields.build());

            final ContentProviderOperation.Builder deleteItem = ContentProviderOperation.newDelete(Items.CONTENT_URI);
            deleteItem.withSelection(Items.ITEM_ID + "=?", new String[]{itemId});
            operations.add(deleteItem.build());
        }

        return operations;
    }

    static class DeleteItemsResponseParser extends ScResponseParser {
        @Override
        public ScResponse parseSuccess(int statusCode, JSONObject success) throws JSONException {
            DeleteItemsResponse response = new DeleteItemsResponse(statusCode);
            response.mCount = success.getInt("count");

            JSONArray deleteItemIdsJsonArray = success.getJSONArray("itemIds");
            for (int i = 0; i < deleteItemIdsJsonArray.length(); i++) {
                response.mDeletedItemIds.add(deleteItemIdsJsonArray.getString(i));
            }
            return response;
        }
    }
}
