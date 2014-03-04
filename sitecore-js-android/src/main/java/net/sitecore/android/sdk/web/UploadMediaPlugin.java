package net.sitecore.android.sdk.web;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import net.sitecore.android.sdk.api.ScApiSession;
import net.sitecore.android.sdk.api.ScApiSessionFactory;
import net.sitecore.android.sdk.api.ScRequestQueue;
import net.sitecore.android.sdk.api.model.ItemsResponse;

import org.json.JSONException;

public final class UploadMediaPlugin extends ScPlugin {
    private ScCallbackContext mCallbackContext;
    private ScParams mParams;

    private Response.Listener<ItemsResponse> mSuccessListener = new Response.Listener<ItemsResponse>() {
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

    private Listener<ScApiSession> mSessionListener = new Listener<ScApiSession>() {
        @Override
        public void onResponse(final ScApiSession session) {
            String compressionQuality = mParams.getString("compressionQuality");
            final String imageUrl = mParams.getString("imageUrl");

            String mediaLibraryPath = mParams.getString("mediaLibraryPath");
            if (!TextUtils.isEmpty(mediaLibraryPath)) {
                session.setMediaLibraryPath(mParams.getString("mediaLibraryPath"));
            }

            if (!TextUtils.isEmpty(compressionQuality)) {
                new CompressionAsyncTask(mContext, imageUrl, compressionQuality) {
                    @Override
                    protected void onPostExecute(String result) {
                        startUpload(session, result);
                    }
                }.execute((Void) null);
            } else {
                startUpload(session, imageUrl);
            }
        }
    };

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

    private void startUpload(ScApiSession session, String itemUrl) {
        final String itemPath = mParams.getString("path");
        String itemName = mParams.getString("itemName");
        String database = mParams.getString("database");

        final Intent intent = session.uploadMediaIntent(itemPath, itemName, itemUrl)
                .setDatabase(database)
                .setSuccessListener(mSuccessListener)
                .setErrorListener(mErrorListener)
                .build(mContext);

        mContext.startService(intent);
    }
}