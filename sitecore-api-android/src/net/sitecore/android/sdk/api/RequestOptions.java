package net.sitecore.android.sdk.api;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sitecore.android.sdk.api.model.PayloadType;
import net.sitecore.android.sdk.api.model.RequestScope;

class RequestOptions {

    private static final int DEFAULT_API_VERSION = 1;
    private static final String ITEMS_API_PREFIX = "/-/item";

    private static final int UNDEFINED = -1;

    final AuthOptions mAuthOptions;
    final UrlOptions mUrlOptions;
    final QueryScopeOptions mQueryScopeOptions;

    String mItemName;
    String mTemplate;

    final HashMap<String, String> mFieldValues;

    RequestOptions(String baseUrl) {
        mAuthOptions = new AuthOptions();
        mUrlOptions = new UrlOptions();
        mUrlOptions.mBaseUrl = baseUrl;
        mQueryScopeOptions = new QueryScopeOptions();
        mFieldValues = new HashMap<String, String>();
    }

    String getFullUrl() {
        return mUrlOptions.getUrl() + mQueryScopeOptions.getQueryParameters();
    }

    static class AuthOptions {
        boolean mIsAnonymous = true;
        String mEncodedName;
        String mEncodedPassword;
    }

    static class UrlOptions {
        int mApiVersion = DEFAULT_API_VERSION;
        String mBaseUrl;
        String mSite;
        String mItemPath;

        /**
         * Creates Items Web Api url in the following format:
         * {baseUrl}/-/item/v{version}/{siteName}/{itemPath}
         *
         * @return url suffix.
         */
        String getUrl() {
            final StringBuilder builder = new StringBuilder(mBaseUrl);
            builder.append(ITEMS_API_PREFIX);
            builder.append("/v").append(mApiVersion);

            if (!TextUtils.isEmpty(mSite)) builder.append(mSite);
            if (!TextUtils.isEmpty(mItemPath)) builder.append(encodeString(mItemPath));

            return builder.toString();
        }
    }

    static class QueryScopeOptions {
        String mItemId;
        int mItemVersion = UNDEFINED;
        String mDatabase;
        String mLanguage;
        Set<String> mFields = new LinkedHashSet<String>();
        PayloadType mPayloadType;
        Set<RequestScope> mScopes = new LinkedHashSet<RequestScope>();
        String mSitecoreQuery;
        int mPage = UNDEFINED;
        int mPageSize = UNDEFINED;

        /**
         *
         * @return Query string starting with '?' or empty string.
         */
        String getQueryParameters() {
            final StringBuilder builder = new StringBuilder();

            if (!TextUtils.isEmpty(mItemId)) builder.append("&sc_itemid=").append(encodeString(mItemId));

            if (mItemVersion > UNDEFINED) builder.append("&sc_itemversion=").append(mItemVersion);

            if (!TextUtils.isEmpty(mDatabase)) builder.append("&sc_database=").append(mDatabase);

            if (!TextUtils.isEmpty(mLanguage)) builder.append("&language=").append(mLanguage);

            if (!mFields.isEmpty()) builder.append("&fields=").append(encodeString(joinSet(mFields)));

            if (mPayloadType != null) builder.append("&payload=").append(mPayloadType.toString());

            if (!mScopes.isEmpty()) {
                builder.append("&scope=").append(encodeString(joinSet(mScopes)));
            }

            if (!TextUtils.isEmpty(mSitecoreQuery)) {
                builder.append("&query=").append(Uri.encode(mSitecoreQuery));
            }

            if (mPage > UNDEFINED) builder.append("&page=").append(mPage);
            if (mPageSize > UNDEFINED) builder.append("&pageSize=").append(mPageSize);

            return TextUtils.isEmpty(builder.toString())
                    ? ""
                    : "?" + builder.toString().substring(1); // replaces first '&' to '?'
        }
    }

    String getCreateItemParams() {
        StringBuilder builder = new StringBuilder("template=");
        builder.append(encodeString(mTemplate));

        if (!TextUtils.isEmpty(mItemName)) {
            builder.append("&name=").append(encodeString(mItemName));
        }
        return builder.toString();
    }

    private static String joinSet(Set<?> set) {
        return TextUtils.join("|", set.toArray());
    }

    private static String encodeString(String text) {
        return Uri.encode(text, "/");
    }
}
