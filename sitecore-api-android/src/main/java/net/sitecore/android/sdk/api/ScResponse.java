package net.sitecore.android.sdk.api;

import android.content.ContentProviderOperation;

import java.util.ArrayList;

import net.sitecore.android.sdk.api.model.DeleteItemsResponse;
import net.sitecore.android.sdk.api.model.ItemsResponse;

/**
 * Base class for responses.
 *
 * @see ItemsResponse
 * @see DeleteItemsResponse
 */
public abstract class ScResponse {

    private int mStatusCode;

    public int getStatusCode() {
        return mStatusCode;
    }

    public boolean isSuccess() {
        return mStatusCode >= 200 && mStatusCode < 300;
    }

    public final ArrayList<ContentProviderOperation> toContentProviderOperations() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        addContentProviderOperations(operations);
        return operations;
    }

    protected void addContentProviderOperations(ArrayList<ContentProviderOperation> operations) {
    }

    protected ScResponse(int statusCode) {
        mStatusCode = statusCode;
    }
}
