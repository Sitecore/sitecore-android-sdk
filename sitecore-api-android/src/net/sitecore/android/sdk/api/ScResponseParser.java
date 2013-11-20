package net.sitecore.android.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;

import net.sitecore.android.sdk.api.model.ScErrorResponse;

public abstract class ScResponseParser {

    public abstract ScResponse parseSuccess(int statusCode, JSONObject success) throws JSONException ;

    public ScResponse parseJson(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        int statusCode = response.getInt("statusCode");
        if (statusCode != 200) {
            return parseError(statusCode, response.optJSONObject("error"));
        } else {
            return parseSuccess(statusCode, response.getJSONObject("result"));
        }
    }

    public ScErrorResponse parseError(int statusCode, JSONObject error) throws JSONException {
        String message = error.getString("message");
        return new ScErrorResponse(statusCode, message);
    }
}
