package net.sitecore.android.sdk.api.model;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;

import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.ScResponse;

/**
 * Class represents delete item request.
 * @see CreateItemRequest
 * @see UpdateItemFieldsRequest
 * @see GetItemsRequest
 * @see ScRequest
 */
public class DeleteItemsRequest extends ScRequest<DeleteItemsResponse> {

    /**
     * Creates delete request to delete item with specified url.
     * @param url item full url to delete
     * @param successListener success listener for request
     * @param errorListener error listener for request
     */
    public DeleteItemsRequest(String url,
            Response.Listener<DeleteItemsResponse> successListener,
            Response.ErrorListener errorListener) {
        super(Request.Method.DELETE, url, successListener, errorListener);
    }

    @Override
    protected ScResponse parseResponse(String response) throws JSONException {
        return new DeleteItemsResponse.DeleteItemsResponseParser().parseJson(response);
    }
}
