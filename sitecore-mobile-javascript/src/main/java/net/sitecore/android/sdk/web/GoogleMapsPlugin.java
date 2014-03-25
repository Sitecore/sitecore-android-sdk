package net.sitecore.android.sdk.web;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

import org.json.JSONException;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

/**
 * Plugin for Javascript - Google Maps interaction.
 * <p>Example :
 * <pre>{@code function openMapsForAddressWithRoute() {
 *     var maps = scmobile.google_maps.GoogleMaps();
 *     maps.addresses = [{
 *         country: "Russia",
 *         city: "Moskow"
 *     }];
 *     maps.show();
 * }}</pre>
 */
public final class GoogleMapsPlugin extends ScPlugin {

    private static final String GMAPS_PACKAGE = "com.google.android.apps.maps";
    private static final String GMAPS_ACTIVITY = "com.google.android.maps.MapsActivity";
    private static final String MAPS = "http://maps.google.com/maps";

    @Override
    public String getPluginName() {
        return "google_maps";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_maps);
    }

    /**
     * Shows maps depending on the received parameters.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if executions fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        if (method.equals("show")) {
            try {
                showGoogleMaps(params);
            } catch (ActivityNotFoundException e) {
                callbackContext.sendError("Google Maps application is not installed.");
            }

        }
    }

    private void showGoogleMaps(ScParams params) throws JSONException {
        final String textParams = parseParams(params);

        final Uri uri = Uri.parse(!TextUtils.isEmpty(textParams)
                ? MAPS + "?" + parseParams(params)
                : MAPS);
        LOGD("Starting maps uri: " + uri.getEncodedPath() + "?" + uri.getQuery());
        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(GMAPS_PACKAGE, GMAPS_ACTIVITY);

        mContext.startActivity(intent);
    }

    private String parseParams(ScParams params) throws JSONException {
        StringBuilder builder = new StringBuilder();

        List<ScParams.ScAddress> addresses = params.getAddressesList();
        if (addresses.size() == 0) return "";

        boolean drawRoute = params.optBoolean("drawRoute", true);
        if (drawRoute) {
            builder.append("daddr=" + buildAddress(addresses.get(0)));
        } else {
            builder.append("q=" + buildAddress(addresses.get(0)));
        }

        return builder.toString();
    }

    private String buildAddress(ScParams.ScAddress address) {
        final StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(address.street)) builder.append(address.street).append(' ');
        if (!TextUtils.isEmpty(address.city)) builder.append(address.city).append(' ');
        if (!TextUtils.isEmpty(address.state)) builder.append(address.state).append(' ');
        if (!TextUtils.isEmpty(address.zip)) builder.append(address.zip).append(' ');
        if (!TextUtils.isEmpty(address.country)) builder.append(address.country);

        return builder.toString().trim();
    }

}
