package net.sitecore.android.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ScResponseParser<T extends ScResponse> {

    public final JSONObject parseResponseTitle(ScResponse response, JSONObject json) throws JSONException {
        int statusCode = json.getInt("statusCode");
        response.setStatusCode(statusCode);

        JSONObject error = json.optJSONObject("error");
        if (error != null) {
            String message = error.getString("message");
            response.setErrorMessage(message);
            return null;
        }
        return json.getJSONObject("result");
    }

    public abstract T parseJson(String json) throws JSONException;
}
