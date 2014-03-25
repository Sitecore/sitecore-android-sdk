package net.sitecore.android.sdk.api.internal;

import net.sitecore.android.sdk.api.ScErrorResponse;
import net.sitecore.android.sdk.api.ScResponse;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ScResponseParser {

    public abstract ScResponse parseSuccess(int statusCode, JSONObject success) throws JSONException;

    public ScResponse parseJson(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        int statusCode = response.getInt("statusCode");
        if (statusCode >= 200 && statusCode < 300) {
            return parseSuccess(statusCode, response.getJSONObject("result"));
        } else {
            return parseError(statusCode, response.optJSONObject("error"));
        }
    }

    public ScErrorResponse parseError(int statusCode, JSONObject error) throws JSONException {
        String message = error.getString("message");
        return new ScErrorResponse(statusCode, message);
    }
}
