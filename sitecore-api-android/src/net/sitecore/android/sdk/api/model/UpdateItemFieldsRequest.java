package net.sitecore.android.sdk.api.model;

import com.android.volley.Response;

import java.util.Map;

import net.sitecore.android.sdk.api.GetItemsResponseParser;
import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.ScResponseParser;

/**
 * Class represents update fields request.
 * @see CreateItemRequest
 * @see DeleteItemsRequest
 * @see GetItemsRequest
 * @see ScRequest
 */
public class UpdateItemFieldsRequest extends ScRequest<ItemsResponse> {

    /**
     * Creates update specified fields request.
     * @param url item full url to update
     * @param bodyFields {@link Map} with {@link ScField#getName()}/{@link ScField#getId()} : {@link String} new value entries
     * @param listener success listener for request
     * @param errorListener error listener for request
     */
    public UpdateItemFieldsRequest(String url, Map<String, String> bodyFields, Response.Listener<ItemsResponse> listener, Response.ErrorListener errorListener) {
        super(Method.PUT, url, listener, errorListener);
        mBodyFields = bodyFields;
    }

    @Override
    public ScResponseParser<ItemsResponse> getResponseParser() {
        return new GetItemsResponseParser();
    }
}
