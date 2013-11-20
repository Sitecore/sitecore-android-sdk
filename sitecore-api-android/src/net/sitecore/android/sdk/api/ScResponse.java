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
        return mStatusCode == 200;
    }

    protected ArrayList<ContentProviderOperation> toContentProviderOperations() {
        return new ArrayList<ContentProviderOperation>();
    }

    protected ScResponse(int statusCode) {
        mStatusCode = statusCode;
    }
}
