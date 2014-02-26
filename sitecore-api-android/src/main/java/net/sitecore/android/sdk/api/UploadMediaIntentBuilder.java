package net.sitecore.android.sdk.api;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScError;

import org.json.JSONException;

/**
 * Class is used to build an {@link Intent}, which will start the {@link android.app.Service} to upload media item.
 */
public class UploadMediaIntentBuilder {

    private final UploadMediaRequestOptions mUploadMediaRequestOptions;

    private Class<? extends UploadMediaService> mMediaUploaderClass = UploadMediaService.class;
    private Response.Listener<ItemsResponse> mSuccessListener = sEmptySuccessListener;
    private Response.ErrorListener mErrorListener = sEmptyErrorListener;

    /**
     * @param itemPath      media folder path to upload media to.
     * @param itemName      item name.
     * @param mediaFilePath path to media file.
     */
    public UploadMediaIntentBuilder(String itemPath, String itemName, String mediaFilePath) {
        mUploadMediaRequestOptions = new UploadMediaRequestOptions(itemPath, itemName, mediaFilePath);

    }

    /**
     * @param successListener Successful result callback
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setSuccessListener(Response.Listener<ItemsResponse> successListener) {
        mSuccessListener = successListener;
        return this;
    }

    /**
     * @param errorListener Error result callback
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setErrorListener(Response.ErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    /**
     * Since Android requires every {@link android.app.Service} component to have unique name, 2 different apps can't use
     * {@link UploadMediaService} at the same time. To solve this collision, create empty subclass of
     * {@link UploadMediaService}.
     * <p>Please note that manifest registration of newly created class is still required.</p>
     *
     * @param mediaUploaderServiceClass Which will perform uploading.
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setUploadMediaServiceClass(Class<? extends UploadMediaService> mediaUploaderServiceClass) {
        mMediaUploaderClass = mediaUploaderServiceClass;
        return this;
    }

    /**
     * @param database Database name
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setDatabase(String database) {
        mUploadMediaRequestOptions.setDatabase(database);
        return this;
    }

    /**
     * @param fileName Uploaded file name
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setFileName(String fileName) {
        mUploadMediaRequestOptions.setFileName(fileName);
        return this;
    }

    /**
     * @param site Site name
     *
     * @return this builder.
     */
    public UploadMediaIntentBuilder setSite(String site) {
        mUploadMediaRequestOptions.setSite(site);
        return this;
    }

    UploadMediaIntentBuilder setBaseUrl(String baseUrl) {
        mUploadMediaRequestOptions.mUrlOptions.mBaseUrl = baseUrl;
        return this;
    }

    UploadMediaIntentBuilder setAuthOptions(String encodedUsername, String encodedPassword) {
        mUploadMediaRequestOptions.mAuthOptions.mIsAnonymous = false;
        mUploadMediaRequestOptions.mAuthOptions.mEncodedName = encodedUsername;
        mUploadMediaRequestOptions.mAuthOptions.mEncodedPassword = encodedPassword;

        return this;
    }

    /**
     * Creates {@link Intent} which starts {@link android.app.Service} to upload media item.
     *
     * @param context Context object
     *
     * @return Created intent
     * @see Context#startService(android.content.Intent)
     */
    public Intent build(Context context) {
        if (!isServiceRegistered(context, mMediaUploaderClass)) {
            throw new RuntimeException("Class " + mMediaUploaderClass + " is not registered in AndroidManifest.xml");
        }

        // Converts String response to ItemsResponse
        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ScResponse result = new ItemsResponse.ItemsResponseParser().parseJson(response);
                    if (result.isSuccess()) {
                        mSuccessListener.onResponse((ItemsResponse) result);
                    } else {
                        final ScError error = new ScError(result.getStatusCode(), ((ScErrorResponse) result).getErrorMessage());
                        mErrorListener.onErrorResponse(error);
                    }
                } catch (JSONException e) {
                    mErrorListener.onErrorResponse(new ParseError(e));
                }
            }
        };

        final UploadResultReceiver resultReceiver = new UploadResultReceiver(null, listener, mErrorListener);
        final Intent intent = new Intent(context, mMediaUploaderClass);
        intent.putExtra(UploadMediaService.EXTRA_UPLOAD_OPTIONS, mUploadMediaRequestOptions);
        intent.putExtra(UploadMediaService.EXTRA_STATUS_RECEIVER, resultReceiver);

        return intent;
    }

    private boolean isServiceRegistered(Context context, Class<?> clazz) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(context, clazz);
        List resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

    private static final ErrorListener sEmptyErrorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };

    private static final Response.Listener<ItemsResponse> sEmptySuccessListener = new Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse response) {
        }
    };

}
