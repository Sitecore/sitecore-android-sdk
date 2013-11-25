package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/** Represents Sitecore item. */
public class ScItem implements Parcelable {

    public static final String ROOT_ITEM_ID = "{11111111-1111-1111-1111-111111111111}";

    private String mDatabase;
    private String mDisplayName;
    private boolean mHasChildren;
    private String mId;
    private String mLanguage;
    private String mLongId;
    private String mPath;
    private String mTemplate;
    private int mVersion;
    private List<ScField> mFields;

    @Override
    public String toString() {
        return mDisplayName;
    }

    void setDatabase(String database) {
        mDatabase = database;
    }

    void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    void setHasChildren(boolean hasChildren) {
        mHasChildren = hasChildren;
    }

    void setId(String id) {
        mId = id;
    }

    void setLanguage(String language) {
        mLanguage = language;
    }

    void setLongId(String longId) {
        mLongId = longId;
    }

    void setPath(String path) {
        mPath = path;
    }

    void setTemplate(String template) {
        mTemplate = template;
    }

    void setVersion(int version) {
        mVersion = version;
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

    ScItem() {
        mFields = new ArrayList<ScField>();
    }

    void addField(ScField field) {
        mFields.add(field);
    }

    protected ScItem(Parcel in) {
        mDatabase = in.readString();
        mDisplayName = in.readString();
        mHasChildren = in.readByte() != 0x00;
        mId = in.readString();
        mLanguage = in.readString();
        mLongId = in.readString();
        mPath = in.readString();
        mTemplate = in.readString();
        mVersion = in.readInt();
        mFields = new ArrayList<ScField>();
        in.readList(mFields, ScItem.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDatabase);
        dest.writeString(mDisplayName);
        dest.writeByte((byte) (mHasChildren ? 0x01 : 0x00));
        dest.writeString(mId);
        dest.writeString(mLanguage);
        dest.writeString(mLongId);
        dest.writeString(mPath);
        dest.writeString(mTemplate);
        dest.writeInt(mVersion);
        dest.writeList(mFields);
    }

    public static final Parcelable.Creator<ScItem> CREATOR = new Parcelable.Creator<ScItem>() {
        public ScItem createFromParcel(Parcel in) {
            return new ScItem(in);
        }

        public ScItem[] newArray(int size) {
            return new ScItem[size];
        }
    };

    /** @return {@link #getId()} of parent item. */
    public String getParentItemId() {
        String parentId = null;
        String[] segments = TextUtils.split(mLongId, "/");
        if (segments.length > 1) {
            parentId = segments[segments.length - 2];
        }

        return parentId;
    }

    /**
     * Returns {@link LinkedList} containing {@link #getId()} of all ancestor items of current item.
     * Current item id comes first in results, root item id will be the last one.
     *
     * @return {@link LinkedList} with item Ids.
     */
    public LinkedList<String> getItemAncestorsIds() {
        LinkedList<String> result = new LinkedList<String>();
        String[] segments = TextUtils.split(mLongId, "/");

        for (String segment : segments) {
            if (!TextUtils.isEmpty(segment)) result.addFirst(segment);
        }

        return result;
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
        builder.withValue(Items.HAS_CHILDREN, mHasChildren ? 1 : 0);
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
