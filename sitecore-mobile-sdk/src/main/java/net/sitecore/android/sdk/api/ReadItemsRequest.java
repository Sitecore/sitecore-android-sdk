package net.sitecore.android.sdk.api;

import com.android.volley.Response;

import net.sitecore.android.sdk.api.model.ItemsResponse;

import org.json.JSONException;

/**
 * Class represents get item request.
 *
 * @see CreateItemRequest
 * @see EditItemsRequest
 * @see DeleteItemsRequest
 * @see ScRequest
 */
public class ReadItemsRequest extends ScRequest<ItemsResponse> {

    /**
     * Creates get item request with specified url.
     *
     * @param url             item full url to retrieve
     * @param successListener success listener for request
     * @param errorListener   error listener for request
     */
    public ReadItemsRequest(String url, Response.Listener<ItemsResponse> successListener,
            Response.ErrorListener errorListener) {
        super(Method.GET, url, successListener, errorListener);
    }

    @Override
    public ScResponse parseResponse(String response) throws JSONException {
        return new ItemsResponse.ItemsResponseParser().parseJson(response);
    }
}
