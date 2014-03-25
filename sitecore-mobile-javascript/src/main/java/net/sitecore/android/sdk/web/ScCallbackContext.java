package net.sitecore.android.sdk.web;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebView;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

/**
 * Implementation of Android - Javascript bridge.
 *
 * @see ScPluginManager
 * @see ScPlugin
 * @see ScJavascriptError
 */
class ScCallbackContext {

    private static final String CALLBACK_SUCCESS_NO_MESSAGE = "scmobile.utils.sendSuccess('%1$s');";
    private static final String CALLBACK_SUCCESS = "scmobile.utils.sendSuccess('%1$s', '%2$s');";
    private static final String CALLBACK_ERROR = "scmobile.utils.sendFailure('%1$s', %2$s);";

    private WebView mWebView;
    private String mPluginId;

    /**
     * Class constructor.
     *
     * @param webView  {@code WebView} that handle callback.
     * @param pluginId {@code ID} of the plugin.
     *
     * @see ScPlugin
     */
    public ScCallbackContext(WebView webView, String pluginId) {
        mWebView = webView;
        mPluginId = pluginId;
    }

    /**
     * Sends success event with message.
     *
     * @param message {@code String} message.
     */
    public void sendSuccess(String message) {
        exec(String.format(CALLBACK_SUCCESS, mPluginId, Uri.encode(message)));
    }

    /** Sends success event with no message. */
    public void sendSuccess() {
        exec(String.format(CALLBACK_SUCCESS_NO_MESSAGE, mPluginId));
    }

    /**
     * Sends error event.
     *
     * @param error {@code ScJavascriptError} object.
     *
     * @see ScJavascriptError
     */
    public void sendError(ScJavascriptError error) {
        exec(String.format(CALLBACK_ERROR, mPluginId, error.toString()));
    }

    /**
     * Sends error event with message.
     *
     * @param error {@code String} message.
     */
    public void sendError(String error) {
        sendError(new ScJavascriptError(error));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void exec(final String message) {
        LOGD(message);
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VersionUtils.isKitKatOrLater()) {
                    mWebView.evaluateJavascript("javascript: " + message, null);
                } else {
                    mWebView.loadUrl("javascript: " + message);
                }
            }
        }, 50);
    }

}
