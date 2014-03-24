package net.sitecore.android.sdk.api;

import java.util.Map;

import com.android.volley.Response;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScField;

import org.json.JSONException;

/**
 * Class represents update fields request.
 *
 * @see CreateItemRequest
 * @see DeleteItemsRequest
 * @see ReadItemsRequest
 * @see ScRequest
 */
public class EditItemsRequest extends ScRequest<ItemsResponse> {

    /**
     * Creates update specified fields request.
     *
     * @param url           item full url to update
     * @param bodyFields    {@link Map} with {@link ScField#getName()}/{@link ScField#getId()} : {@link String} new value entries
     * @param listener      success listener for request
     * @param errorListener error listener for request
     */
    public EditItemsRequest(String url, Map<String, String> bodyFields, Response.Listener<ItemsResponse> listener, Response.ErrorListener errorListener) {
        super(Method.PUT, url, listener, errorListener);
        mBodyFields = bodyFields;
    }

    @Override
    public ScResponse parseResponse(String response) throws JSONException {
        return new ItemsResponse.ItemsResponseParser().parseJson(response);
    }

}
