package net.sitecore.android.sdk.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;

/** Base class for implementation Javascript - Android - Javascript bridge. */
abstract class ScPlugin {

    protected Context mContext;
    protected ScPluginManager mPluginManager;
    protected String mPluginId;

    /**
     * Base constructor.
     *
     * @param context       current context.
     * @param pluginManager {@link ScPluginManager} instance.
     */
    public void init(Context context, ScPluginManager pluginManager) {
        mContext = context;
        mPluginManager = pluginManager;
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        mPluginManager.getActivityContext(this).startActivityForResult(intent, requestCode);
    }

    /**
     * Returns plugin name.
     *
     * @return {@code String} plugin name.
     */
    public abstract String getPluginName();

    /**
     * Returns javascript code that will be attached to the page.
     * <p>This code defines what functionality of this plugin we will use from our page.
     *
     * @return {@code String} js code.
     */
    public abstract String getPluginJsCode();

    /**
     * Executes specific method of this plugin.
     * <p>Is called when user triggers {@code plugin.exec()} Javascript function.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by method.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if executions fails.
     * @see ScPluginManager
     */
    public abstract void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    /**
     * Saves plugin's state.
     *
     * @param outState state.
     */
    public void saveState(Bundle outState) {
    }

    /**
     * Restores saved plugin's state.
     *
     * @param savedState saved state.
     */
    public void restoreState(Bundle savedState) {
    }

    /**
     * Specifies plugin's id.
     *
     * @param pluginId {@code String} plugin's id.
     */
    public void setPluginId(String pluginId) {
        mPluginId = pluginId;
    }

}
