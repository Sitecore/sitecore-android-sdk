package net.sitecore.android.sdk.web;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;

/**
 * Class responsible for catching special URLs that loads inside {@link WebView}.
 *
 * @see WebViewClient
 */
class ScWebViewClient extends WebViewClient {

    private static final String TAG = "ScMobile";

    private ScPluginManager mPluginManager;

    /**
     * Constructs {@code ScWebViewClient} based on {@code ScPluginManager}.
     *
     * @param pluginManager {@code ScPluginManager} instance.
     *
     * @see ScPluginManager
     */
    public ScWebViewClient(ScPluginManager pluginManager) {
        mPluginManager = pluginManager;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.loadUrl(mPluginManager.getInjectableJs());
        view.loadUrl("javascript: scmobile.utils.sendScmobileReadyEvent()");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("sc://")) {
            Log.d(TAG, "URL intercepted: " + url);
            handleNativeUri(url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * Parses URL for plugin information and executes {@link ScPluginManager#exec(String, String, String, ScParams)} method with parsed parameters.
     * <p>URL format :
     * {@code sc://localhost/plugin_name/method_name?guid=XXXX&params={...}}
     *
     * @param url received URL.
     */
    private void handleNativeUri(String url) {
        Uri uri = Uri.parse(url);
        String plugin = uri.getPathSegments().get(0);
        String method = uri.getPathSegments().get(1);

        String pluginId = uri.getQueryParameter("guid");
        String data = uri.getQueryParameter("params");

        try {
            ScParams scParams = TextUtils.isEmpty(data)
                    ? new ScParams()
                    : new ScParams(data);
            mPluginManager.exec(plugin, method, pluginId, scParams);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}
