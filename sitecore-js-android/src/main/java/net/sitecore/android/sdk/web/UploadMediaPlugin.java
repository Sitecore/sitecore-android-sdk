package net.sitecore.android.sdk.web;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import net.sitecore.android.sdk.api.ScApiSession;
import net.sitecore.android.sdk.api.ScApiSessionFactory;
import net.sitecore.android.sdk.api.ScRequestQueue;
import net.sitecore.android.sdk.api.UploadMediaIntentBuilder;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScItem;

import org.json.JSONException;

public final class UploadMediaPlugin extends ScPlugin {
    private final String FIELDS_KEY = "fields";
    private ScCallbackContext mCallbackContext;
    private ScParams mParams;
    private ScApiSession mSession;

    private Response.Listener<ItemsResponse> mSuccessEditListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse response) {
            if (response.getItems().size() == 1) mCallbackContext.sendSuccess();
            else mCallbackContext.sendError("Failed to upload media.");
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mCallbackContext.sendError(new ScJavascriptError("Failed to upload media.", error.getMessage()));
        }
    };

    private Response.Listener<ItemsResponse> mSuccessUploadListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse response) {
            if (response.getItems().size() == 1) {
                Map<String, String> fields = mParams.getParsedJsonObject(FIELDS_KEY);
                if (!fields.isEmpty()) {
                    editItem(response.getItems().get(0));
                } else {
                    mCallbackContext.sendSuccess();
                }
            } else mCallbackContext.sendError("Failed to upload media.");
        }
    };

    private Listener<ScApiSession> mSessionListener = new Listener<ScApiSession>() {
        @Override
        public void onResponse(final ScApiSession session) {
            mSession = session;
            String compressionQuality = mParams.getString("compressionQuality");
            final String imageUrl = mParams.getString("imageUrl");

            String mediaLibraryPath = mParams.getString("mediaLibraryPath");
            if (!TextUtils.isEmpty(mediaLibraryPath)) {
                mSession.setMediaLibraryPath(mParams.getString("mediaLibraryPath"));
            }

            if (!TextUtils.isEmpty(compressionQuality)) {
                new CompressionAsyncTask(mContext, imageUrl, compressionQuality) {
                    @Override
                    protected void onPostExecute(String result) {
                        startUpload(result);
                    }
                }.execute((Void) null);
            } else {
                startUpload(imageUrl);
            }
        }
    };

    private void editItem(ScItem item) {
        Map<String, String> fields = mParams.getParsedJsonObject(FIELDS_KEY);
        new ScRequestQueue(mContext.getContentResolver())
                .add(mSession.editItemFields(item, fields, mSuccessEditListener, mErrorListener));
    }

    @Override
    public String getPluginName() {
        return "contentapi";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_contentapi);
    }

    @Override
    public void exec(String method, final ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        mParams = params;

        String login = mParams.getString("login");
        String password = mParams.getString("password");

        Uri instanceUri = Uri.parse(params.getString("instanceURL"));
        String url = instanceUri.getScheme() + "://" + instanceUri.getHost() + ":" + instanceUri.getPort();

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            ScApiSessionFactory.getAnonymousSession(url, mSessionListener);
        } else {
            ScRequestQueue queue = new ScRequestQueue(mContext.getContentResolver());
            ScApiSessionFactory.getSession(queue, url, login, password, mSessionListener, mErrorListener);
        }
    }

    private void startUpload(String itemUrl) {
        final String itemPath = mParams.getString("path");
        String itemName = mParams.getString("itemName");
        String database = mParams.getString("database");

        UploadMediaIntentBuilder mediaIntentBuilder = mSession.uploadMediaIntent(itemPath, itemName, itemUrl)
                .setDatabase(database)
                .setSuccessListener(mSuccessUploadListener)
                .setErrorListener(mErrorListener);

        final Intent intent = mediaIntentBuilder.build(mContext);
        mContext.startService(intent);
    }
}