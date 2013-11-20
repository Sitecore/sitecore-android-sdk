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
public class ScResponse {

    private int mStatusCode;

    private String mErrorMessage;

    public int getStatusCode() {
        return mStatusCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public boolean isSuccess() {
        return mErrorMessage == null;
    }

    protected ArrayList<ContentProviderOperation> toContentProviderOperations() {
        return new ArrayList<ContentProviderOperation>();
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
