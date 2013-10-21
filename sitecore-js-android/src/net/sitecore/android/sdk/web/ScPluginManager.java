package net.sitecore.android.sdk.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import java.util.HashMap;

import org.json.JSONException;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

/**
 * Class that maintains all interaction between webview and plugins.
 * <p>Catches calls from Javascript code and redirects them to appropriate plugins.</p>
 *
 * @see ScPlugin
 * @see ScCallbackContext
 * @see ScJavascriptError
 */
class ScPluginManager {

    private final Context mContext;
    private WebView mWebView;

    private final ScJsWriter mJsWriter;

    /** Map with pluginName -> plugin */
    private final HashMap<String, ScPlugin> mPlugins = new HashMap<String, ScPlugin>();

    /** Map with pluginName -> pluginId. Used to keep */
    private final HashMap<String, String> mActivePlugins = new HashMap<String, String>();

    private ActivityContext mActivityContext;
    private ScPlugin mActivityCallbackPlugin;

    /**
     * Base constructor.
     *
     * @param context current context.
     */
    public ScPluginManager(Context context) {
        mJsWriter = new ScJsWriter(context);
        mContext = context;
    }

    public void setWebView(WebView webView) {
        mWebView = webView;
    }

    public WebView getWebView() {
        return mWebView;
    }

    /** Initializes plugins and adds them to plugin storage. */
    public void addAllPlugins() {
        addPlugin(new ConsolePlugin());
        addPlugin(new ToastPlugin());
        addPlugin(new AlertPlugin());
        addPlugin(new DevicePlugin());
        addPlugin(new SharePlugin());
        addPlugin(new AccelerometerPlugin());
        addPlugin(new CameraPlugin());
        addPlugin(new ContactsPlugin());
        addPlugin(new GoogleMapsPlugin());
    }

    private void addPlugin(ScPlugin plugin) {
        LOGD("'%s' plugin added to page.", plugin.getPluginName());
        plugin.init(mContext, this);
        mPlugins.put(plugin.getPluginName(), plugin);
        mJsWriter.addPlugin(plugin);
    }

    /**
     * Catches calls from Javascript code and redirects them to the target plugin.
     *
     * @param pluginName {@code String} plugin name.
     * @param method     {@code String} plugin's method name.
     * @param pluginId   {@code String} plugin's ID.
     * @param params     {@link ScParams} plugin's method params.
     */
    public void exec(String pluginName, String method, final String pluginId, ScParams params) {
        final ScPlugin scPlugin = mPlugins.get(pluginName);
        scPlugin.setPluginId(pluginId);
        final ScCallbackContext callbackContext = new ScCallbackContext(mWebView, pluginId);
        try {
            scPlugin.exec(method, params, callbackContext);
        } catch (JSONException e) {
            ScJavascriptError error = new ScJavascriptError(String.format("Failed to execute %s.%s()", pluginName, method));
            error.setException(e);
            callbackContext.sendError(error);
        }
    }

    public ActivityContext getActivityContext(ScPlugin source) {
        mActivityCallbackPlugin = source;
        return mActivityContext;
    }

    public void setActivityContext(ActivityContext activityContext) {
        mActivityContext = activityContext;
    }

    /**
     * Returns {@code Javascript} code that is injected to the page.
     *
     * @return {@code String} code.
     */
    public String getInjectableJs() {
        return "javascript: " + mJsWriter.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mActivityCallbackPlugin != null) {
            mActivityCallbackPlugin.onActivityResult(requestCode, resultCode, data);
        } else {
            LOGD("No plugin available to consume ActivityResult");
        }
    }

    /** Binds plugins lifecycle to the lifecycle of the android components. */
    public void onPause() {
        for (ScPlugin plugin : mPlugins.values()) {
            plugin.onPause();
        }
    }

    /** Binds plugins lifecycle to the lifecycle of the android components. */
    public void onResume() {
        for (ScPlugin plugin : mPlugins.values()) {
            plugin.onResume();
        }
    }

    /**
     * Saves plugins states.
     *
     * @param outState {@code Bundle} state.
     */
    public void onSaveState(Bundle outState) {
        for (ScPlugin plugin : mPlugins.values()) {
            plugin.saveState(outState);
        }
    }

    /**
     * Loads saved plugins state.
     *
     * @param savedState {@code Bundle} state.
     */
    public void onRestoreState(Bundle savedState) {
        if (savedState != null) {
            for (ScPlugin plugin : mPlugins.values()) {
                plugin.restoreState(savedState);
            }
        }
    }

}
