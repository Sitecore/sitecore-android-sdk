package net.sitecore.android.sdk.web;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;

import static net.sitecore.android.sdk.web.LogUtils.LOGE;

/**
 * Plugin for showing android alert dialog.
 * <p>Example :
 * <pre>
 * scmobile.notification.alert('Normal alert', 'message content',
 *          alertCallback, 'button cancel, button ok, button 3, button 4');
 * </pre>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public class AlertPlugin extends ScPlugin {

    private static final int BUTTON_NEGATIVE = 0;
    private static final int BUTTON_NEUTRAL = 1;
    private static final int BUTTON_POSITIVE = 2;

    private ScParams mParams;
    private boolean mIsAlertVisible = false;
    private AlertDialog mAlertDialog;

    @Override
    public String getPluginName() {
        return "notification";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_notification);
    }

    /**
     * Shows alert dialog.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if execution fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mParams = params;
        if (method.equalsIgnoreCase("alert")) {
            alert(params, callbackContext);
        }
    }

    private void alert(ScParams params, final ScCallbackContext callbackContext) {
        final String title = params.getString("title");
        final String message = params.getString("message");
        final String buttons = params.getString("buttons");

        if (TextUtils.isEmpty(buttons)) {
            LOGE("AlertPlugin requires at least 1 button.");
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final String[] buttonNames = TextUtils.split(buttons, ",");

        builder.setTitle(title).setMessage(message);

        if (buttonNames.length > BUTTON_NEGATIVE) {
            builder.setNegativeButton(buttonNames[BUTTON_NEGATIVE], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mIsAlertVisible = false;
                    callbackContext.sendSuccess("" + BUTTON_NEGATIVE);
                }
            });
        }

        if (buttonNames.length > BUTTON_NEUTRAL) {
            builder.setNeutralButton(buttonNames[BUTTON_NEUTRAL], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mIsAlertVisible = false;
                    callbackContext.sendSuccess("" + BUTTON_NEUTRAL);
                }
            });
        }

        if (buttonNames.length > BUTTON_POSITIVE) {
            builder.setPositiveButton(buttonNames[BUTTON_POSITIVE], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mIsAlertVisible = false;
                    callbackContext.sendSuccess("" + BUTTON_POSITIVE);
                }
            });
        }

        if (buttonNames.length > BUTTON_POSITIVE + 1) {
            LOGE("Only first 3 buttons are supported by AlertPlugin, but %d buttons declared.", buttonNames.length);
        }

        mAlertDialog = builder.create();
        mAlertDialog.show();
        mIsAlertVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAlertDialog != null && mIsAlertVisible) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * Saves current settings of dialog.
     *
     * @param outState current state.
     */
    @Override
    public void saveState(Bundle outState) {
        if (mIsAlertVisible) {
            outState.putString("params", mParams.toString());
            outState.putString("plugin_id", mPluginId);
            outState.putBoolean("alert_visible", mIsAlertVisible);
        }
    }

    /**
     * Loads settings of dialog.
     *
     * @param savedState saved state.
     */
    @Override
    public void restoreState(Bundle savedState) {
        mIsAlertVisible = savedState.getBoolean("alert_visible", false);
        if (mIsAlertVisible) {
            String params = savedState.getString("params");
            String pluginId = savedState.getString("plugin_id");
            try {
                mParams = new ScParams(params);
                ScCallbackContext callback = new ScCallbackContext(mPluginManager.getWebView(), pluginId);
                alert(mParams, callback);
            } catch (JSONException e) {
                LOGE(e);
            }
        }

    }
}
