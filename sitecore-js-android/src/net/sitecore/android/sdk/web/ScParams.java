package net.sitecore.android.sdk.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents parameters received from {@code Javascript} code.
 *
 * @see ScPlugin
 * @see ScPluginManager
 */
class ScParams {
    private final String ADDRESS_KEY = "addresses";

    private JSONObject mJSONObject;

    public ScParams() {
        mJSONObject = new JSONObject();
    }

    /**
     * Creates {@code ScParams} based on {@code String} resource.
     *
     * @param data {@code String} resource.
     *
     * @throws JSONException if the parse fails or doesn't yield a JSONObject.
     */
    public ScParams(String data) throws JSONException {
        mJSONObject = new JSONObject(data);
    }

    /**
     * Returns html decoded value mapped by key if it exists, coercing it if necessary.
     * Returns the empty string if no such mapping exists.
     *
     * @param key key.
     *
     * @return {@code String} decoded value.
     */
    public String getString(String key) {
        return getDecodedString(mJSONObject.optString(key));
    }

    /**
     * Returns html decoded values, stored by key (JSONArray / String) in {@code String[]} format.
     *
     * @param key {@code String} key name.
     *
     * @return {@code String[]} for specified key.
     */
    public String[] getStringArray(String key) {
        if (!mJSONObject.has(key)) return new String[]{};

        String[] result = new String[]{
        };

        try {
            Object value = mJSONObject.get(key);
            if (value instanceof String) {
                result = new String[]{
                        getDecodedString((String) value)
                };
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                result = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    result[i] = getDecodedString(array.get(i).toString());
                }
            }
        } catch (JSONException e) {
        }

        return result;
    }

    private String getDecodedString(String value) {
        return StringEscapeUtils.unescapeHtml4(value);
    }

    /**
     * Returns the value mapped by name if it exists and is an int or can be coerced to an int. Returns 0 otherwise.
     *
     * @param name name.
     *
     * @return {@code int} value.
     */
    public int optInt(String name) {
        return mJSONObject.optInt(name);
    }

    /**
     * Returns the value mapped by name if it exists and is a boolean or can be coerced to a boolean. Returns false otherwise.
     *
     * @param name name.
     *
     * @return {@code boolean} value.
     */
    public boolean optBoolean(String name) {
        return mJSONObject.optBoolean(name);
    }

    /**
     * Returns the value mapped by name if it exists and is a boolean or can be coerced to a boolean. Returns fallback otherwise.
     *
     * @param name     name.
     * @param fallback {@code boolean} fallback.
     *
     * @return {@code boolean} value.
     */
    public boolean optBoolean(String name, boolean fallback) {
        return mJSONObject.optBoolean(name, fallback);
    }

    /**
     * Returns the value mapped by name if it exists and is an int or can be coerced to an int. Returns fallback otherwise.
     *
     * @param name     name.
     * @param fallback {@code int} fallback.
     *
     * @return {@code int} value.
     */
    public int optInt(String name, int fallback) {
        return mJSONObject.optInt(name, fallback);
    }

    /**
     * Returns {@link List} of {@code ScAddress} parsed from JSON.
     *
     * @return {@code List} or empty {@code List} if no array in JSON.
     */
    public List<ScAddress> getAddressesList() {
        List<ScAddress> resultList = new ArrayList<ScAddress>();
        JSONArray array = mJSONObject.optJSONArray(ADDRESS_KEY);
        if (array == null) return resultList;

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String street = getDecodedString(object.optString("street"));
                String state = getDecodedString(object.optString("state"));
                String city = getDecodedString(object.optString("city"));
                String zip = getDecodedString(object.optString("zip"));
                String country = getDecodedString(object.optString("country"));
                resultList.add(new ScAddress(street, state, city, zip, country));
            } catch (JSONException e) {
                break;
            }
        }
        return resultList;
    }

    /** Class represents address {@code JSON} object. */
    public class ScAddress {
        public final String street;
        public final String state;
        public final String city;
        public final String zip;
        public final String country;

        ScAddress(String street, String state, String city, String zip, String country) {
            this.street = street;
            this.state = state;
            this.city = city;
            this.zip = zip;
            this.country = country;
        }
    }

}
