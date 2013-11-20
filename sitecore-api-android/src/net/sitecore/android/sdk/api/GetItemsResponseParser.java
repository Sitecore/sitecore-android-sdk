package net.sitecore.android.sdk.api;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScItem;

public class GetItemsResponseParser extends ScResponseParser<ItemsResponse> {
    @Override
    public ItemsResponse parseJson(String json) throws JSONException {
        ItemsResponse response = new ItemsResponse();
        JSONObject jsonObject = new JSONObject(json);

        JSONObject result = parseResponseTitle(response, jsonObject);
        if (!response.isSuccess()) return response;

        int totalCount = result.getInt("totalCount");
        int resultCount = result.getInt("resultCount");

        response.setTotalCount(totalCount);
        response.setResultCount(resultCount);

        JSONArray itemsJsonArray = result.getJSONArray("items");
        ArrayList<ScItem> items = new ArrayList<ScItem>(itemsJsonArray.length());

        for (int i = 0; i < itemsJsonArray.length(); i++) {
            items.add(parseItem(itemsJsonArray.getJSONObject(i)));
        }
        response.setItems(items);

        return response;
    }

    private ScItem parseItem(JSONObject json) throws JSONException {
        ScItem item = new ScItem();
        item.setDisplayName(json.optString("DisplayName"));
        item.setDatabase(json.optString("Database"));
        item.setHasChildren(json.optBoolean("HasChildren"));
        item.setId(json.optString("ID"));
        item.setLanguage(json.optString("Language"));
        item.setLongId(json.optString("LongID"));
        item.setPath(json.optString("Path"));
        item.setTemplate(json.optString("Template"));
        item.setVersion(json.optInt("Version"));

        JSONObject fieldsObject = json.optJSONObject("Fields");

        Iterator<String> keyIterator = fieldsObject.keys();
        ArrayList<ScField> fields = new ArrayList<ScField>();
        while (keyIterator.hasNext()) {
            String id =  keyIterator.next();
            fields.add(parseFiled(id, fieldsObject.getJSONObject(id)));
        }
        item.setFields(fields);
        return item;
    }

    private ScField parseFiled(String id, JSONObject json) {
        final String name = json.optString("Name");
        final String type = json.optString("Type");
        final String value = json.optString("Value");

        return ScField.createFieldFromType(ScField.Type.getByName(type), name, id, value);
    }
}
