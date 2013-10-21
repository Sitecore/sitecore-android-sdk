package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.UploadMediaService;

import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;

/** Class represents response for {@link CreateItemRequest}, {@link UpdateItemFieldsRequest}, {@link GetItemsRequest}, and response for  {@link UploadMediaService Uploading media item}. */
public class ItemsResponse extends ScResponse {

    @SerializedName("result")
    private Result mResult;

    public List<ScItem> getItems() {
        return mResult == null ? Collections.<ScItem>emptyList() : mResult.mItems;
    }

    public int getTotalCount() {
        return mResult == null ? 0 : mResult.mTotalCount;
    }

    public int getResultCount() {
        return mResult == null ? 0 : mResult.mResultCount;
    }

    private static class Result {

        @SerializedName("totalCount")
        private int mTotalCount;

        @SerializedName("resultCount")
        private int mResultCount;

        @SerializedName("items")
        private List<ScItem> mItems;
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