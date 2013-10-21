package net.sitecore.android.sdk.api;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Collections;
import java.util.Set;

import net.sitecore.android.sdk.api.model.CreateItemRequest;
import net.sitecore.android.sdk.api.model.DeleteItemsRequest;
import net.sitecore.android.sdk.api.model.GetItemsRequest;
import net.sitecore.android.sdk.api.model.PayloadType;
import net.sitecore.android.sdk.api.model.RequestScope;
import net.sitecore.android.sdk.api.model.UpdateItemFieldsRequest;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

/**
 * Helper class for building {@code requests}:
 * <ul>
 * <li>{@link GetItemsRequest}</li>
 * <li>{@link DeleteItemsRequest}</li>
 * <li>{@link UpdateItemFieldsRequest}</li>
 * <li>{@link CreateItemRequest}</li>
 * </ul>
 */
public class RequestBuilder {

    private final ScApiSession mSession;

    private final int mRequestMethod;
    private final RequestOptions mOptions;

    private Response.Listener mSuccessListener;
    private Response.ErrorListener mErrorListener;

    RequestBuilder(ScApiSession session, int requestMethod) {
        mSession = session;
        mRequestMethod = requestMethod;
        mOptions = new RequestOptions(session.getBaseUrl());
    }

    /**
     * Specifies Sitecore Item Web API version.
     *
     * @param version web api version.
     *
     * @return this builder.
     */
    public RequestBuilder apiVersion(int version) {
        mOptions.mUrlOptions.mApiVersion = version;
        return this;
    }

    /**
     * Specifies site name for Sitecore site resolving mechanism.
     *
     * @param site site name.
     *
     * @return this builder.
     */
    public RequestBuilder fromSite(String site) {
        if (!TextUtils.isEmpty(site) && !site.startsWith("/")) {
            throw new IllegalArgumentException("Site name must start with '/'.");
        }
        mOptions.mUrlOptions.mSite = site;
        return this;
    }

    /**
     * Specifies item path.
     *
     * @param path item path.
     *
     * @return this builder.
     */
    public RequestBuilder byItemPath(String path) {
        if (!TextUtils.isEmpty(path) && !path.startsWith("/")) {
            throw new IllegalArgumentException("Items path must start with '/'.");
        }
        mOptions.mUrlOptions.mItemPath = path;
        return this;
    }

    /**
     * Specifies the ID of the content item.
     *
     * @param itemId item ID.
     *
     * @return this builder.
     */
    public RequestBuilder byItemId(String itemId) {
        mOptions.mQueryScopeOptions.mItemId = itemId;
        return this;
    }

    /**
     * Specifies the version number of the content item.
     * <p>By default the latest version is used.
     *
     * @param version item version.
     *
     * @return this builder.
     */
    public RequestBuilder itemVersion(int version) {
        mOptions.mQueryScopeOptions.mItemVersion = version;
        return this;
    }

    /**
     * Specifies the database that contains the content items.
     *
     * @param database database name.
     *
     * @return this builder.
     */
    public RequestBuilder database(String database) {
        mOptions.mQueryScopeOptions.mDatabase = database;
        return this;
    }

    /**
     * Specifies the context language for the request.
     * <p>Example : {@code da-DK}.
     * <p>Default language : {@code default}.
     *
     * @param language language.
     *
     * @return this builder.
     */
    public RequestBuilder setLanguage(String language) {
        mOptions.mQueryScopeOptions.mLanguage = language;
        return this;
    }

    /**
     * Specifies set of the fields to return in response.
     *
     * @param fields {@code Set} of fields names or IDs.
     *
     * @return this builder.
     */
    public RequestBuilder setFields(Set<String> fields) {
        mOptions.mQueryScopeOptions.mFields.addAll(fields);
        return this;
    }

    /**
     * Specifies set of fields to return in response.
     *
     * @param fields fields names or IDs.
     *
     * @return this builder.
     */
    public RequestBuilder setFields(String... fields) {
        Collections.addAll(mOptions.mQueryScopeOptions.mFields, fields);
        return this;
    }

    /**
     * Specifies amount of fields to load. (MIN/CONTENT/FULL).
     * <p>One of {@code PayloadType}.
     *
     * @param payloadType {@code PayloadType} of fields payload in response.
     *
     * @return this builder.
     * @see PayloadType
     */
    public RequestBuilder withPayloadType(PayloadType payloadType) {
        mOptions.mQueryScopeOptions.mPayloadType = payloadType;
        return this;
    }

    /**
     * Specifies the set of the items that you are working with.
     *
     * @param scope       scope of items.
     * @param otherScopes extra scopes of items.
     *
     * @return this builder.
     * @throws IllegalArgumentException if {@code otherScopes} > 2.
     */
    public RequestBuilder withScope(RequestScope scope, RequestScope... otherScopes) {
        if (otherScopes.length > 2) {
            throw new IllegalArgumentException("Maximum number os scopes is 3: s|p|c.");
        }

        mOptions.mQueryScopeOptions.mScopes.add(scope);
        Collections.addAll(mOptions.mQueryScopeOptions.mScopes, otherScopes);

        return this;
    }

    /**
     * Specifies Sitecore Query or Sitecore Fast Query
     *
     * @param query Sitecore query.
     *
     * @return this builder.
     * @throws IllegalStateException if {@code query} is already set.
     */
    public RequestBuilder bySitecoreQuery(String query) {
        if (!TextUtils.isEmpty(mOptions.mQueryScopeOptions.mSitecoreQuery)) {
            throw new IllegalStateException("Query is already set");
        }
        mOptions.mQueryScopeOptions.mSitecoreQuery = query;
        return this;
    }

    /**
     * If the response contains numerous result items, you can use paging to obtain parts
     * of the whole result set as pages.
     *
     * @param pageNumber number is the index of a given page in a set that starts with 0.
     * @param pageSize   page size is the number of the result items that are presented in the page.
     *                   Should be greater than 0
     *
     * @return this builder.
     * @throws IllegalArgumentException if {@code pageNumber} < 0 or {@code pageSize} < 1.
     */
    public RequestBuilder setPage(int pageNumber, int pageSize) {
        if (pageNumber < 0) throw new IllegalArgumentException("Page number must be >= 0.");
        if (pageSize < 1) throw new IllegalArgumentException("Page size must be > 0.");

        mOptions.mQueryScopeOptions.mPage = pageNumber;
        mOptions.mQueryScopeOptions.mPageSize = pageSize;

        return this;
    }

    RequestBuilder setCreateItemData(String name, String template) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }

        if (TextUtils.isEmpty(template)) {
            throw new IllegalArgumentException("Template name/id cannot be empty.");
        }

        mOptions.mItemName = name;
        mOptions.mTemplate = template;

        return this;
    }

    RequestBuilder setCreateItemFromBranchData(String branchId) {
        if (TextUtils.isEmpty(branchId)) {
            throw new IllegalArgumentException("Branch id cannot be empty.");
        }

        mOptions.mTemplate = branchId;

        return this;
    }

    /**
     * Update field value.
     * Used by {@link CreateItemRequest} and {@link UpdateItemFieldsRequest} only.
     *
     * @param key   field ID or name to update
     * @param value updated field value
     *
     * @return this builder.
     */
    public RequestBuilder updateFieldValue(String key, String value) {
        mOptions.mFieldValues.put(key, value);
        return this;
    }

    void setSuccessListener(Response.Listener successListener) {
        mSuccessListener = successListener;
    }

    void setErrorListener(Response.ErrorListener errorListener) {
        mErrorListener = errorListener;
    }

    /**
     * Creates {@link ScRequest} with specified parameters.
     *
     * @return this builder.
     * @throws IllegalArgumentException if request method isn't recognized.
     */
    public ScRequest<? extends ScResponse> build() {
        final String url = mOptions.getFullUrl();

        ScRequest request;
        switch (mRequestMethod) {
            case Request.Method.GET:
                request = new GetItemsRequest(url, mSuccessListener, mErrorListener);
                break;

            case Request.Method.POST:
                final String postUrl = url.contains("?")
                        ? url + "&" + mOptions.getCreateItemParams()
                        : url + "?" + mOptions.getCreateItemParams();

                request = new CreateItemRequest(postUrl, mOptions.mFieldValues, mSuccessListener, mErrorListener);
                break;

            case Request.Method.PUT:
                request = new UpdateItemFieldsRequest(url, mOptions.mFieldValues, mSuccessListener, mErrorListener);
                break;

            case Request.Method.DELETE:
                request = new DeleteItemsRequest(url, mSuccessListener, mErrorListener);
                break;

            default:
                throw new IllegalArgumentException("Incorrect request method: " + mRequestMethod);
        }

        if (!mSession.isAnonymous()) {
            request.addHeader("X-Scitemwebapi-Username", mSession.createEncodedName());
            request.addHeader("X-Scitemwebapi-Password", mSession.createEncodedPassword());
            request.addHeader("X-Scitemwebapi-Encrypted", "1");
        }

        if (mSession.shouldCache()) request.setShouldCache(true);

        LOGD("Created request: " + request);
        return request;
    }

}
