package net.sitecore.android.sdk.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sitecore.android.sdk.api.model.DeleteItemsResponse;

public class DeleteItemsResponseParser extends ScResponseParser<DeleteItemsResponse> {

    @Override
    public DeleteItemsResponse parseJson(String json) throws JSONException {
        DeleteItemsResponse response = new DeleteItemsResponse();
        JSONObject jsonObject = new JSONObject(json);

        JSONObject result = parseResponseTitle(response, jsonObject);
        if (!response.isSuccess()) return response;

        int count = result.getInt("count");
        response.setCount(count);

        JSONArray deleteItemIdsJsonArray = result.getJSONArray("itemIds");
        ArrayList<String> deleteItemIds = new ArrayList<String>(deleteItemIdsJsonArray.length());

        for (int i = 0; i < deleteItemIdsJsonArray.length(); i++) {
            deleteItemIds.add(deleteItemIdsJsonArray.getString(i));
        }
        response.setDeletedItemIds(deleteItemIds);

        return response;
    }
}
