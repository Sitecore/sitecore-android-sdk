package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/** Represents Sitecore item. */
public class ScItem {

    public static final String ROOT_ITEM_ID = "{11111111-1111-1111-1111-111111111111}";

    @SerializedName("Database")
    private String mDatabase;

    @SerializedName("DisplayName")
    private String mDisplayName;

    @SerializedName("HasChildren")
    private boolean mHasChildren;

    @SerializedName("ID")
    private String mId;

    @SerializedName("Language")
    private String mLanguage;

    @SerializedName("LongID")
    private String mLongId;

    @SerializedName("Path")
    private String mPath;

    @SerializedName("Template")
    private String mTemplate;

    @SerializedName("Version")
    private int mVersion;

    @SerializedName("Fields")
    private List<ScField> mFields;

    @Override
    public String toString() {
        return mDisplayName;
    }

    public String getDatabase() {
        return mDatabase;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public boolean hasChildren() {
        return mHasChildren;
    }

    public String getId() {
        return mId;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getLongId() {
        return mLongId;
    }

    public String getPath() {
        return mPath;
    }

    public String getTemplate() {
        return mTemplate;
    }

    public int getVersion() {
        return mVersion;
    }

    public List<ScField> getFields() {
        return mFields;
    }

    /**
     * @return {@link #getId()} of parent item.
     */
    public String getParentItemId() {
        String parentId = null;
        String[] segments = TextUtils.split(mLongId, "/");
        if (segments.length > 1) {
            parentId = segments[segments.length - 2];
        }

        return parentId;
    }

    /**
     * Look for {@link ScField} by name.
     *
     * @param name Name of the field to look for.
     *
     * @return {@link ScField} with specified name or null.
     * @see #findFieldById(String)
     */
    public ScField findFieldByName(String name) {
        if (mFields == null) return null;
        for (ScField field : mFields) {
            if (field.getName().equals(name)) return field;
        }

        return null;
    }

    /**
     * Returns found {@link ScField}.
     *
     * @param fieldId field ID.
     *
     * @return {@link ScField} or null if not found.
     * @see #findFieldByName(String)
     */
    public ScField findFieldById(String fieldId) {
        if (mFields == null) return null;
        for (ScField field : mFields) {
            if (field.getId().equals(fieldId)) return field;
        }

        return null;
    }

    public ContentProviderOperation toInsertOperation() {
        final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Items.CONTENT_URI);

        builder.withValue(Items.ITEM_ID, mId);
        builder.withValue(Items.DISPLAY_NAME, mDisplayName);
        builder.withValue(Items.LONG_ID, mLongId);
        builder.withValue(Items.PARENT_ITEM_ID, getParentItemId());
        builder.withValue(Items.PATH, mPath);
        builder.withValue(Items.TEMPLATE, mTemplate);
        builder.withValue(Items.VERSION, mVersion);
        builder.withValue(Items.DATABASE, mDatabase);
        builder.withValue(Items.LANGUAGE, mLanguage);
        builder.withValue(Items.TIMESTAMP, new Date().getTime());

        return builder.build();
    }

    /**
     * Returns url for downloading media item.
     *
     * @return {@link String} or {@code null} if this item is not a media item.
     */
    public String getMediaDownloadUrl() {
        if (mPath.contains("/sitecore/media library/")) {
            String id = mId.replace("{", "").replace("}", "").replace("-", "");
            return String.format("/~/media/%s.ashx", id);
        } else {
            return null;
        }
    }

}
