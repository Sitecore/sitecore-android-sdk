package net.sitecore.android.sdk.web;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

/**
 * Plugin for transmission Javascript messages to Android.
 * <p>Example :
 * <pre>scmobile.plugin_console.log('message');</pre>
 */
public final class ConsolePlugin extends ScPlugin {

    @Override
    public String getPluginName() {
        return "console";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_console);
    }

    /**
     * Loges received message.
     *
     * @param method          {@code String} action (method) name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if executions fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        if (method.equalsIgnoreCase("log")) {
            String message = params.getString("message");
            if (!TextUtils.isEmpty(message)) Log.d("ScMobile", message);
        }
    }

}
