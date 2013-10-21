package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/** Class represents response for {@link DeleteItemsRequest}. */
public class DeleteItemsResponse extends ScResponse {

    @SerializedName("result")
    private Result mResult;

    private static class Result {

        @SerializedName("itemIds")
        private List<String> mDeletedItemIds;

        @SerializedName("count")
        private int mCount;
    }

    public List<String> getDeletedItemsIds() {
        return mResult.mDeletedItemIds;
    }

    public int getDeletedCount() {
        return mResult.mCount;
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

}
