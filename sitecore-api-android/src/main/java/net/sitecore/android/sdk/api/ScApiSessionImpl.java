package net.sitecore.android.sdk.api;

import android.content.Context;
import android.text.TextUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import net.sitecore.android.sdk.api.internal.CryptoUtils;
import net.sitecore.android.sdk.api.model.DeleteItemsResponse;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.RequestScope;
import net.sitecore.android.sdk.api.model.ScItem;

import static net.sitecore.android.sdk.api.internal.LogUtils.LOGE;

class ScApiSessionImpl implements ScApiSession {

    private final String mBaseUrl;
    private final RSAPublicKey mPublicKey;
    private final String mName;
    private final String mPassword;
    private final boolean mIsAnonymous;
    private String mDefaultDatabase;
    private String mDefaultSite;
    private String mDefaultLanguage;

    private boolean mShouldCache = false;

    ScApiSessionImpl(String baseUrl) {
        this(baseUrl, null, null, null, true);
    }

    ScApiSessionImpl(String baseUrl, RSAPublicKey key, String name, String password) {
        this(baseUrl, key, name, password, false);
    }

    private ScApiSessionImpl(String baseUrl, RSAPublicKey key, String name, String password, boolean isAnonymous) {
        mBaseUrl = baseUrl;
        mPublicKey = key;
        mName = name;
        mPassword = password;

        mIsAnonymous = isAnonymous;
    }

    @Override
    public RequestBuilder readItemsRequest(Listener<ItemsResponse> successListener, ErrorListener errorListener) {
        final RequestBuilder builder = new RequestBuilder(this, Request.Method.GET);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public RequestBuilder readItemsByIdsRequest(ArrayList<String> itemIds, Listener<ItemsResponse> successListener, ErrorListener errorListener) {
        if (itemIds == null) throw new IllegalArgumentException("itemIds can't be null");
        if (itemIds.isEmpty()) throw new IllegalArgumentException("itemIds can't be empty");

        final RequestBuilder builder = new RequestBuilder(this, Request.Method.GET);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);

        ScQueryBuilder queryBuilder = new ScQueryBuilder();
        queryBuilder.setFast(false);
        for (String id : itemIds) {
            queryBuilder.addIdParam(id);
        }
        builder.bySitecoreQuery(queryBuilder.build());

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public RequestBuilder createItemRequest(String itemName, String template,
            Listener<ItemsResponse> successListener,
            ErrorListener errorListener) {
        final RequestBuilder builder = new RequestBuilder(this, Request.Method.POST);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);
        builder.setCreateItemData(itemName, template);

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public RequestBuilder createItemRequest(String branchId,
            Listener<ItemsResponse> successListener,
            ErrorListener errorListener) {
        final RequestBuilder builder = new RequestBuilder(this, Request.Method.POST);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);
        builder.setCreateItemFromBranchData(branchId);

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public RequestBuilder editItemsRequest(Listener<ItemsResponse> successListener, ErrorListener errorListener) {
        final RequestBuilder builder = new RequestBuilder(this, Request.Method.PUT);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public RequestBuilder deleteItemsRequest(Listener<DeleteItemsResponse> successListener, ErrorListener errorListener) {
        final RequestBuilder builder = new RequestBuilder(this, Request.Method.DELETE);
        builder.setSuccessListener(successListener);
        builder.setErrorListener(errorListener);

        setDefaultOptions(builder);

        return builder;
    }

    @Override
    public Request checkCredentialsRequest(Context context, final Listener<Boolean> callback) {
        final Listener<ItemsResponse> responseListener = new Listener<ItemsResponse>() {
            @Override
            public void onResponse(ItemsResponse response) {
                int code = response.getStatusCode();
                callback.onResponse(code >= 200 && code <= 300);
            }
        };

        final ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResponse(false);
            }
        };
        return readItemsRequest(responseListener, errorListener).build();
    }

    @Override
    public GetRenderingHtmlRequestBuilder getRenderingHtmlRequest(String renderingId, String itemId,
            Listener<String> successListener, ErrorListener errorListener) {
        if (TextUtils.isEmpty(renderingId)) {
            throw new IllegalArgumentException("RenderingId cannot be empty.");
        }

        if (TextUtils.isEmpty(itemId)) {
            throw new IllegalArgumentException("ItemId cannot be empty.");
        }

        final GetRenderingHtmlRequestBuilder builder = new GetRenderingHtmlRequestBuilder(this, renderingId, itemId,
                successListener, errorListener);
        if (!mIsAnonymous) {
            RequestOptions.AuthOptions options = builder.getAuthOptions();
            options.mIsAnonymous = false;
            options.mEncodedName = createEncodedName();
            options.mEncodedPassword = createEncodedPassword();
        }

        if (mDefaultDatabase != null) builder.database(mDefaultDatabase);
        if (mDefaultLanguage != null) builder.setLanguage(mDefaultLanguage);
        if (mDefaultSite != null) builder.fromSite(mDefaultSite);

        return builder;
    }

    @Override
    public UploadMediaIntentBuilder uploadMediaIntent(String itemPath, String itemName, String mediaFilePath) {
        final UploadMediaIntentBuilder builder = new UploadMediaIntentBuilder(itemPath, itemName, mediaFilePath);
        builder.setBaseUrl(mBaseUrl)
                .setAuthOptions(createEncodedName(), createEncodedPassword())
                .setDatabase(mDefaultDatabase);

        return builder;
    }

    @Override
    public DeleteItemsRequest deleteItem(ScItem item, Listener<DeleteItemsResponse> successListener,
            ErrorListener errorListener) {
        RequestBuilder builder = deleteItemsRequest(successListener, errorListener);
        builder.byItemId(item.getId());

        return (DeleteItemsRequest) builder.build();
    }

    @Override
    public GetItemsRequest getItemChildren(ScItem parentItem, Listener<ItemsResponse> successListener,
            ErrorListener errorListener) {
        RequestBuilder builder = readItemsRequest(successListener, errorListener);

        builder.byItemId(parentItem.getId());
        builder.withScope(RequestScope.CHILDREN);

        return (GetItemsRequest) builder.build();
    }

    @Override
    public UpdateItemFieldsRequest updateItemFields(ScItem item, Map<String, String> fields,
            Listener<ItemsResponse> successListener, ErrorListener errorListener) {
        RequestBuilder builder = editItemsRequest(successListener, errorListener);

        builder.byItemId(item.getId());
        for (String fieldName : fields.keySet()) {
            builder.updateFieldValue(fieldName, fields.get(fieldName));
        }

        return (UpdateItemFieldsRequest) builder.build();
    }

    @Override
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @Override
    public void setDefaultSite(String site) {
        this.mDefaultSite = site;
    }

    @Override
    public void setDefaultLanguage(String language) {
        this.mDefaultLanguage = language;
    }

    @Override
    public void setDefaultDatabase(String database) {
        this.mDefaultDatabase = database;
    }

    private void setDefaultOptions(RequestBuilder builder) {
        if (!TextUtils.isEmpty(mDefaultSite)) {
            builder.fromSite(mDefaultSite);
        }
        if (!TextUtils.isEmpty(mDefaultLanguage)) {
            builder.setLanguage(mDefaultLanguage);
        }
        if (!TextUtils.isEmpty(mDefaultDatabase)) {
            builder.database(mDefaultDatabase);
        }
    }

    @Override
    public boolean isAnonymous() {
        return mIsAnonymous;
    }

    @Override
    public boolean shouldCache() {
        return mShouldCache;
    }

    @Override
    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

    @Override
    public String createEncodedName() {
        try {
            return CryptoUtils.encodeRsaMessage(mPublicKey, mName);
        } catch (InvalidKeyException e) {
            LOGE(e);
            return null;
        } catch (BadPaddingException e) {
            LOGE(e);
            return null;
        } catch (IllegalBlockSizeException e) {
            LOGE(e);
            return null;
        } catch (NoSuchPaddingException e) {
            LOGE(e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGE(e);
            return null;
        }
    }

    @Override
    public String createEncodedPassword() {
        try {
            return CryptoUtils.encodeRsaMessage(mPublicKey, mPassword);
        } catch (InvalidKeyException e) {
            LOGE(e);
            return null;
        } catch (BadPaddingException e) {
            LOGE(e);
            return null;
        } catch (IllegalBlockSizeException e) {
            LOGE(e);
            return null;
        } catch (NoSuchPaddingException e) {
            LOGE(e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGE(e);
            return null;
        }
    }

    class ScQueryBuilder {
        private boolean isFast;
        private String mInitialPath;
        private StringBuilder mAttributes;
        private StringBuilder mQuery;

        ScQueryBuilder() {
            mInitialPath = "";
            mQuery = new StringBuilder();
            mAttributes = new StringBuilder();
        }

        void setInitialPath(String initialPath) {
            mInitialPath = initialPath;
        }

        String build() {
            if (isFast) mQuery.append("fast:");
            if (!TextUtils.isEmpty(mInitialPath)) mQuery.append(mInitialPath);
            if (mAttributes.length() != 0) {
                mQuery.append(mAttributes);
            }
            return mQuery.toString();
        }

        public void addIdParam(String id) {
            if (mAttributes.length() != 0) mAttributes.append("|");
            mAttributes.append(String.format("//*[@@id='%s']", id));
        }

        void setFast(boolean fast) {
            isFast = fast;
        }
    }
}
