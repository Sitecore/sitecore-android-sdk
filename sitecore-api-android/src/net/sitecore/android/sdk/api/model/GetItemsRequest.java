package net.sitecore.android.sdk.api.model;

import com.android.volley.Response;

import org.json.JSONException;

import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.ScResponse;

/**
 * Class represents get item request.
 * @see CreateItemRequest
 * @see UpdateItemFieldsRequest
 * @see DeleteItemsRequest
 * @see ScRequest
 */
public class GetItemsRequest extends ScRequest<ItemsResponse> {

    /**
     * Creates get item request with specified url.
     * @param url item full url to retrieve
     * @param successListener success listener for request
     * @param errorListener error listener for request
     */
    public GetItemsRequest(String url, Response.Listener<ItemsResponse> successListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, successListener, errorListener);
    }

    @Override
    public ScResponse parseResponse(String response) throws JSONException {
        return new ItemsResponse.GetItemsResponseParser().parseJson(response);
    }
}
