package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.UploadMediaService;

import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;

/** Class represents response for {@link CreateItemRequest}, {@link UpdateItemFieldsRequest}, {@link GetItemsRequest},
 * and response for  {@link UploadMediaService Uploading media item}.
 * */
public class ItemsResponse extends ScResponse {

    private int mTotalCount;
    private int mResultCount;

    private List<ScItem> mItems;

    public List<ScItem> getItems() {
        return mItems == null ? Collections.<ScItem>emptyList() : mItems;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public int getResultCount() {
        return mResultCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public void setResultCount(int resultCount) {
        mResultCount = resultCount;
    }

    public void setItems(List<ScItem> items) {
        mItems = items;
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
}