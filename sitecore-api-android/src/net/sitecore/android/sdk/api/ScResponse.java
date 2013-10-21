package net.sitecore.android.sdk.api;

import android.content.ContentProviderOperation;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("statusCode")
    private int mStatusCode;

    @SerializedName("error")
    private Error mError;

    public int getStatusCode() {
        return mStatusCode;
    }

    public String getErrorMessage() {
        return mError == null ? null : mError.mMessage;
    }

    public boolean isSuccess() {
        return mError == null;
    }

    protected ArrayList<ContentProviderOperation> toContentProviderOperations() {
        return new ArrayList<ContentProviderOperation>();
    }

    private static class Error {

        @SerializedName("message")
        private String mMessage;
    }

}
