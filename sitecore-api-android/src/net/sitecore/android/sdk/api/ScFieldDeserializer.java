package net.sitecore.android.sdk.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sitecore.android.sdk.api.model.ScField;

/** {@code JsonDeserializer} for parsing request response into {@code List} of {@link net.sitecore.android.sdk.api.model.ScField items}. */
class ScFieldDeserializer implements JsonDeserializer<List<ScField>> {

    @Override
    public List<ScField> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final List<ScField> result = new ArrayList<ScField>();

        final JsonObject jsonField = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonField.entrySet()) {
            final String id = entry.getKey();

            final JsonObject fieldValues = (JsonObject) entry.getValue();
            final String name = fieldValues.getAsJsonPrimitive(Tags.NAME).getAsString();
            final String value = fieldValues.getAsJsonPrimitive(Tags.VALUE).getAsString();
            final String type = fieldValues.getAsJsonPrimitive(Tags.TYPE).getAsString();

            result.add(ScField.createFieldFromType(ScField.Type.getByName(type), name, id, value));
        }

        return result;
    }

    private interface Tags {
        String NAME = "Name";
        String VALUE = "Value";
        String TYPE = "Type";
    }
}
