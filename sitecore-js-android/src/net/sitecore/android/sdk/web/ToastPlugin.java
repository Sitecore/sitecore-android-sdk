package net.sitecore.android.sdk.web;

import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Plugin for showing Android {@link Toast} messages.
 * <p>Example :
 * <pre>
 *     scmobile.plugin_toast.showMessage('message');
 * </pre>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public final class ToastPlugin extends ScPlugin {

    @Override
    public String getPluginName() {
        return "toast";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_toast);
    }

    /**
     * Shows in {@link Toast} received message.
     *
     * @param method          {@code String} action (method) name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if executions fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        if ("showMessage".equals(method)) {
            String message = params.getString("message");
            if (!TextUtils.isEmpty(message)) Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

}
