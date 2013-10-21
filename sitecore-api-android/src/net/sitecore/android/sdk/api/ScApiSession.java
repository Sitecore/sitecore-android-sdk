package net.sitecore.android.sdk.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import net.sitecore.android.sdk.api.model.CreateItemRequest;
import net.sitecore.android.sdk.api.model.DeleteItemsRequest;
import net.sitecore.android.sdk.api.model.DeleteItemsResponse;
import net.sitecore.android.sdk.api.model.GetItemsRequest;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.UpdateItemFieldsRequest;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

/**
 * This class holds information about connection properties and user authentication properties.
 * All requests to Sitecore Item Web API are built using instances of {@link ScApiSession}.
 */
public abstract class ScApiSession {

    private static final String RSA_SUFFIX = "/-/item/v1/-/actions/getpublickey";

    /**
     * Creates anonymous session to specified backend url.
     *
     * @param url Host url with port
     *
     * @return new session.
     */
    public static ScApiSession getAnonymousSession(String url) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("Url can't be empty");
        return new ScApiSessionImpl(url);
    }

    /**
     * Creates anonymous session to specified backend url in asynchronous way.
     *
     * @param url       Host url with port
     * @param onSuccess is called immediately.
     *
     * @see #getAnonymousSession(String)
     */
    public static void getAnonymousSession(String url, final Listener<ScApiSession> onSuccess) {
        onSuccess.onResponse(new ScApiSessionImpl(url));
    }

    /**
     * Creates authenticated session.
     *
     * @param url       Sitecore instance URL / server URL.
     * @param name      User login name.
     * @param password  User password
     * @param onSuccess Success result callback.
     */
    public static void getSession(Context context, String url,
            final String name,
            final String password,
            final Listener<ScApiSession> onSuccess) {
        getSession(context, url, name, password, onSuccess, null);
    }

    /**
     * Creates authenticated session.
     *
     * @param url       Sitecore instance URL / server URL.
     * @param name      User login name.
     * @param password  User password
     * @param onSuccess Success result callback.
     * @param onError   Error result callback
     */
    public static void getSession(Context context, String url,
            final String name,
            final String password,
            final Listener<ScApiSession> onSuccess,
            final ErrorListener onError) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("Url can't be empty");

        final RsaPublicKeyResponseListener responseHandler = new RsaPublicKeyResponseListener(url, name, password, onSuccess, onError);
        final StringRequest request = new StringRequest(url + RSA_SUFFIX, responseHandler, onError);
        LOGD("Sending GET " + url + RSA_SUFFIX);

        RequestQueueProvider.getRequestQueue(context).add(request);
    }

    /**
     * Creates {@link RequestBuilder} to build the {@link GetItemsRequest}.
     *
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     *
     * @return {@link RequestBuilder} to build the request.
     */
    public abstract RequestBuilder getItems(Listener<ItemsResponse> successListener, ErrorListener errorListener);

    /**
     * Creates {@link GetItemsRequest} to load list of items with particular IDs.
     *
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     * @param itemIds         specifies the ids of the items.
     *
     * @return {@link GetItemsRequest}.
     */
    public abstract RequestBuilder getItemsByIds(ArrayList<String> itemIds, Listener<ItemsResponse> successListener,
            ErrorListener errorListener);

    /**
     * Creates {@link RequestBuilder} to build the {@link CreateItemRequest}
     *
     * @param itemName        specifies the name of the item being created. It must be a valid Sitecore item name.
     * @param template        specifies the template that the new item is based on.
     *                        It either accepts the template ID or the relative template path.
     *                        For example, {@code "Sample/MyTemplate"}
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     *
     * @return {@link RequestBuilder} to build the request.
     */
    public abstract RequestBuilder createItem(String itemName, String template,
            Listener<ItemsResponse> successListener,
            ErrorListener errorListener);

    /**
     * Creates {@link RequestBuilder} to build the {@link CreateItemRequest}
     *
     * @param branchId        specifies the branch id that the new item is based on.
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     *
     * @return {@link RequestBuilder} to build the request.
     */
    public abstract RequestBuilder createItem(String branchId,
            Listener<ItemsResponse> successListener,
            ErrorListener errorListener);

    /**
     * Creates {@link RequestBuilder} to build the {@link UpdateItemFieldsRequest}.
     *
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     *
     * @return {@link RequestBuilder} to build the request.
     */
    public abstract RequestBuilder updateItems(Listener<ItemsResponse> successListener, ErrorListener errorListener);

    /**
     * Creates {@link RequestBuilder} to build the {@link DeleteItemsRequest}.
     *
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     *
     * @return {@link RequestBuilder} to build the request.
     */
    public abstract RequestBuilder deleteItems(Listener<DeleteItemsResponse> successListener, ErrorListener errorListener);

    /**
     * Executes default items request (/-/item/v1/) to check result code.
     *
     * @param context  current context.
     * @param callback Is called after request is executed with {@code true} if request succeeded (200 <= code <= 300),
     *                 and {@code false} otherwise.
     */
    public abstract void validate(Context context, final Listener<Boolean> callback);

    /**
     * Creates {@link GetRenderingHtmlRequestBuilder} to build special request to load
     * html rendering of an item.
     *
     * @param renderingId     Id of needed rendering item.
     * @param itemId          Id of needed data source item.
     * @param successListener Success result callback.
     * @param errorListener   Error result callback
     */
    public abstract GetRenderingHtmlRequestBuilder getRenderingHtml(String renderingId, String itemId,
            Listener<String> successListener, ErrorListener errorListener);

    /**
     * Creates {@link UploadMediaRequestOptions} that describe image upload request.
     * Afterwards {@link UploadMediaService#startUpload} or {@link UploadMediaService#newUploadIntent} must be used.
     *
     * @param itemPath      Media folder path to upload media to.
     * @param itemName      Name of the media item.
     * @param mediaFilePath any of "content://"(file name should be explicitly set), "http://", "file://"
     *
     * @return {@link UploadMediaRequestOptions} that describe image upload request.
     */
    public abstract UploadMediaRequestOptions uploadMedia(String itemPath, String itemName, String mediaFilePath);

    /** @return Backend url with port. */
    public abstract String getBaseUrl();

    public abstract boolean isAnonymous();

    /** @return {@code true} if requests should cache all responses. {@code false} by default. */
    public abstract boolean shouldCache();

    /** Sets default caching value for all built requests. Setting this value in request have higher priority. */
    public abstract void setShouldCache(boolean shouldCache);

    /**
     * Creates encoded name for auth http header.
     * Returns {@code null} when {@link #isAnonymous()} is true;
     */
    abstract String createEncodedName();

    /**
     * Creates encoded password for auth http header.
     * Returns {@code null} when {@link #isAnonymous()} is true;
     */
    abstract String createEncodedPassword();

}
