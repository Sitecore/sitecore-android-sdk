package net.sitecore.android.sdk.api;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import com.android.volley.Response;

import java.util.ArrayList;

import net.sitecore.android.sdk.api.model.GetRenderingHtmlRequest;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;
import static net.sitecore.android.sdk.api.RequestOptions.AuthOptions;

/**
 * This is a helper class for building {@link GetRenderingHtmlRequest requests}.
 *
 * @see GetRenderingHtmlRequest
 */
public class GetRenderingHtmlRequestBuilder {

    private static final int UNDEFINED = -1;

    private final ScApiSession mApiSession;

    private final String mRenderingId;
    private final String mItemId;

    private final Response.Listener<String> mSuccessListener;
    private final Response.ErrorListener mErrorListener;

    private String mDatabase;
    private int mItemVersion = UNDEFINED;
    private String mLanguage;
    private String mSite;
    private final AuthOptions mAuthOptions = new AuthOptions();

    private final ArrayList<Pair<String, String>> mRenderingParams;

    GetRenderingHtmlRequestBuilder(ScApiSession session, String renderingId, String itemId,
            Response.Listener<String> successListener,
            Response.ErrorListener errorListener) {
        mApiSession = session;
        mRenderingId = renderingId;
        mItemId = itemId;

        mSuccessListener = successListener;
        mErrorListener = errorListener;

        mRenderingParams = new ArrayList<Pair<String, String>>();
    }

    /**
     * Specifies database thar contain item.
     *
     * @param database database name.
     *
     * @return this builder.
     */
    public GetRenderingHtmlRequestBuilder database(String database) {
        mDatabase = database;
        return this;
    }


    /**
     * Specifies item version.
     *
     * @param itemVersion item version.
     *
     * @return this builder.
     */
    public GetRenderingHtmlRequestBuilder setItemVersion(int itemVersion) {
        mItemVersion = itemVersion;
        return this;
    }

    /**
     * Specifies item language.
     *
     * @param language item language.
     *
     * @return this builder.
     */
    public GetRenderingHtmlRequestBuilder setLanguage(String language) {
        mLanguage = language;
        return this;
    }

    /**
     * Specifies site name for request.
     *
     * @param site site name.
     *
     * @return this builder.
     */
    public GetRenderingHtmlRequestBuilder fromSite(String site) {
        mSite = site;
        return this;
    }

    /**
     * Adds rendering parameter to request.
     *
     * @param key   parameter name.
     * @param value parameter value.
     *
     * @return this builder.
     */
    public GetRenderingHtmlRequestBuilder addRenderingParameter(String key, String value) {
        if (TextUtils.isEmpty(key)) throw new IllegalArgumentException("Empty parameter names are not allowed.");

        final Pair<String, String> pair = new Pair<String, String>(key, value);
        mRenderingParams.add(pair);
        return this;
    }

    /**
     * Returns built {@code GetRenderingHtmlRequest} based on specified options.
     *
     * @return {@code GetRenderingHtmlRequest} built request.
     * @see GetRenderingHtmlRequest
     */
    public GetRenderingHtmlRequest build() {
        final String url = getFullPath() + "?" + getQueryParams();
        LOGD("Created request: GET " + url);
        GetRenderingHtmlRequest request = new GetRenderingHtmlRequest(url, mSuccessListener, mErrorListener);
        if (!mAuthOptions.mIsAnonymous) {
            request.addHeader("X-Scitemwebapi-Username", mAuthOptions.mEncodedName);
            request.addHeader("X-Scitemwebapi-Password", mAuthOptions.mEncodedPassword);
            request.addHeader("X-Scitemwebapi-Encrypted", "1");
        }
        return request;
    }

    AuthOptions getAuthOptions() {
        return mAuthOptions;
    }

    /** @return url with format {url:port}/-/item/v1/{siteName}/-/actions/GetRenderingHtml */
    private String getFullPath() {
        final StringBuilder builder = new StringBuilder(mApiSession.getBaseUrl());
        builder.append("/-/item/v1");

        if (!TextUtils.isEmpty(mSite)) builder.append(mSite);

        builder.append("/-/actions/GetRenderingHtml");

        return builder.toString();
    }

    private String getQueryParams() {
        final StringBuilder builder = new StringBuilder();
        builder.append("renderingId=").append(mRenderingId);
        builder.append("&sc_itemid=").append(mItemId);

        if (!TextUtils.isEmpty(mDatabase)) {
            builder.append("&sc_database=").append(mDatabase);
        }

        if (!TextUtils.isEmpty(mLanguage)) {
            builder.append("&language=").append(mLanguage);
        }

        if (mItemVersion > UNDEFINED) {
            builder.append("&itemversion=").append(mItemVersion);
        }

        for (Pair<String, String> param : mRenderingParams) {
            builder.append("&").append(param.first).append("=").append(param.second);
        }

        return Uri.encode(builder.toString(), "/&=");
    }


}
