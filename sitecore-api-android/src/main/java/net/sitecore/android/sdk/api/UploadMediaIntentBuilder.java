package net.sitecore.android.sdk.api;

import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;

import net.sitecore.android.sdk.api.model.ItemsResponse;

public class UploadMediaIntentBuilder {

    public UploadMediaIntentBuilder(String itemPath, String itemName, String mediaFilePath) {
    }

    public UploadMediaIntentBuilder setSuccessListener(Response.Listener<ItemsResponse> successListener) {
        return this;
    }

    public UploadMediaIntentBuilder setErrorListener(ErrorListener errorListener) {
        return this;
    }

    public UploadMediaIntentBuilder setUploadMediaServiceClass(Class<? extends UploadMediaService> uploaderClass) {


        return this;
    }

    void setBaseUrl(String baseUrl) {

    }

    void setAuthOptions(String encodedUsername, String encodedPassword) {
    }

    public Intent build() {
        return new Intent();
    }

}
