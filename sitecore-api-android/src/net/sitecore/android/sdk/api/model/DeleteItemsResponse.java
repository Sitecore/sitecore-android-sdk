package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import java.util.ArrayList;
import java.util.List;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/** Class represents response for {@link DeleteItemsRequest}. */
public class DeleteItemsResponse extends ScResponse {

    private List<String> mDeletedItemIds;

    private int mCount;

    public List<String> getDeletedItemsIds() {
        return mDeletedItemIds;
    }

    public int getDeletedCount() {
        return mCount;
    }

    public void setDeletedItemIds(List<String> deletedItemIds) {
        mDeletedItemIds = deletedItemIds;
    }

    public void setCount(int count) {
        mCount = count;
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
