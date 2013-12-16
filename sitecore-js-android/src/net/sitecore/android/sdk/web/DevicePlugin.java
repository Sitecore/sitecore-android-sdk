package net.sitecore.android.sdk.web;

import android.provider.Settings;

/**
 * Plugin for retrieving device information.
 * <p>Example :
 * <pre>
 *     var device_version = scmobile.device.version;
 *     var device_name = scmobile.device.name;
 *     var device_uuid = scmobile.device.uuid;
 * </pre>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public final class DevicePlugin extends ScPlugin {

    @Override
    public String getPluginName() {
        return "device";
    }

    /**
     * Returns device Javascript code with inserted device information.
     *
     * @return {@code String} with js code.
     */
    @Override
    public String getPluginJsCode() {
        final String version = android.os.Build.VERSION.RELEASE;
        final String name = android.os.Build.MODEL;

        return String.format(IoUtils.readRawTextFile(mContext, R.raw.plugin_device), version, name, getUuid());
    }

    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) {

    }

    private String getUuid() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
