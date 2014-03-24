package net.sitecore.android.sdk.api;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/** This class represents request options for {@link UploadMediaService}. */
public class UploadMediaRequestOptions implements Parcelable {

    private final String mItemName;
    private final String mMediaFilePath;
    private String mFileName;

    private String mDatabase;

    final RequestOptions.AuthOptions mAuthOptions;
    final RequestOptions.UrlOptions mUrlOptions;

    /**
     * Class constructor specifying:
     *
     * @param itemPath      media folder path to upload media to.
     * @param itemName      item name.
     * @param mediaFilePath path to media file.
     */
    UploadMediaRequestOptions(String itemPath, String itemName, String mediaFilePath) {
        mItemName = itemName;
        mMediaFilePath = mediaFilePath;

        mAuthOptions = new RequestOptions.AuthOptions();
        mUrlOptions = new RequestOptions.UrlOptions();
        mUrlOptions.mItemPath = itemPath;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setDatabase(String database) {
        mDatabase = database;
    }

    public void setSite(String site) {
        mUrlOptions.mSite = site;
    }

    /**
     * <p>Creates encoded url for uploading item based on:
     * <ul>
     * <li>item name</li>
     * <li>item path</li>
     * <li>database</li>
     * <li>site</li>
     * </ul>
     *
     * @return {@link String} encoded url.
     * @see #UploadMediaRequestOptions(String, String, String)
     */
    public String getFullUrl() {
        StringBuilder builder = new StringBuilder(mUrlOptions.getUrl());
        builder.append("?name=").append(Uri.encode(mItemName));
        if (!TextUtils.isEmpty(mDatabase)) builder.append("&sc_database=").append(mDatabase);
        return builder.toString();
    }

    public String getMediaFilePath() {
        return mMediaFilePath;
    }

    private UploadMediaRequestOptions(Parcel source) {
        mItemName = source.readString();
        mMediaFilePath = source.readString();
        mDatabase = source.readString();
        mFileName = source.readString();

        mAuthOptions = new RequestOptions.AuthOptions();
        mAuthOptions.mIsAnonymous = source.readInt() == 1;
        mAuthOptions.mEncodedName = source.readString();
        mAuthOptions.mEncodedPassword = source.readString();

        mUrlOptions = new RequestOptions.UrlOptions();
        mUrlOptions.mBaseUrl = source.readString();
        mUrlOptions.mItemPath = source.readString();
        mUrlOptions.mSite = source.readString();
        mUrlOptions.mApiVersion = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemName);
        dest.writeString(mMediaFilePath);
        dest.writeString(mDatabase);
        dest.writeString(mFileName);

        dest.writeInt(mAuthOptions.mIsAnonymous ? 1 : 0);
        dest.writeString(mAuthOptions.mEncodedName);
        dest.writeString(mAuthOptions.mEncodedPassword);

        dest.writeString(mUrlOptions.mBaseUrl);
        dest.writeString(mUrlOptions.mItemPath);
        dest.writeString(mUrlOptions.mSite);
        dest.writeInt(mUrlOptions.mApiVersion);
    }

    public static final Creator<UploadMediaRequestOptions> CREATOR = new Creator<UploadMediaRequestOptions>() {

        @Override
        public UploadMediaRequestOptions createFromParcel(Parcel source) {
            return new UploadMediaRequestOptions(source);
        }

        @Override
        public UploadMediaRequestOptions[] newArray(int size) {
            return new UploadMediaRequestOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
