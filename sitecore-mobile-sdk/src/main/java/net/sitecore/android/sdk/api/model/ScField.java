package net.sitecore.android.sdk.api.model;

import android.content.ContentProviderOperation;
import android.os.Parcel;
import android.os.Parcelable;

import net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;

/**
 * Base class for Sitecore Field
 *
 * @see MediaField
 * @see ScDateField
 * @see ImageField
 * @see RichTextField
 * @see MultilistField
 * @see DroplinkField
 * @see DroptreeField
 * @see ChecklistField
 * @see TreelistField
 * @see CheckBoxField
 */
public class ScField implements Parcelable {

    public enum Type implements Parcelable {
        TEXT("Text"),
        RICHTEXT("Rich Text"),
        SINGLE_LINE_TEXT("Single-Line Text"),
        IMAGE("Image"),
        CHECKBOX("Checkbox"),
        DATE("Date"),
        DATE_TIME("Datetime"),
        MULTILIST("Multilist"),
        TREELIST("Treelist"),
        CHECKLIST("Checklist"),
        DROPLINK("Droplink"),
        DROPTREE("Droptree"),
        DROPLIST("Droplist"),
        AUDIO("Audio"),
        DOC("Doc"),
        DOCUMENT("Document"),
        DOCX("Docx"),
        FILE("File"),
        FLASH("Flash"),
        JPEG("Jpeg"),
        MOVIE("Movie"),
        MP3("Mp3"),
        PDF("Pdf"),
        ZIP("Zip"),
        GENERAL_LINK("General Link"),
        UNKNOWN("unknown");

        private final String mValue;

        private Type(String value) {
            mValue = value;
        }

        public String getValue() {
            return mValue;
        }

        public static Type getByName(String name) {
            for (Type type : values()) {
                if (type.mValue.equals(name)) return type;
            }

            return UNKNOWN;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<Type> CREATOR = new Creator<Type>() {
            @Override
            public Type createFromParcel(final Parcel source) {
                return Type.values()[source.readInt()];
            }

            @Override
            public Type[] newArray(final int size) {
                return new Type[size];
            }
        };
    }

    private final String mId;
    private final String mName;
    private final Type mType;
    private final String mRawValue;

    /**
     * Class constructor
     *
     * @param name     field name
     * @param id       field id
     * @param type     field type
     * @param rawValue raw value of the field
     */
    ScField(String name, String id, Type type, String rawValue) {
        mId = id;
        mName = name;
        mRawValue = rawValue;
        mType = type;
        parseRawValue(rawValue);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Type getType() {
        return mType;
    }

    public String getRawValue() {
        return mRawValue;
    }

    protected void parseRawValue(String rawValue) {
    }

    /**
     * Factory method that creates a {@link ScField} of the specified field type.
     *
     * @param name     field name
     * @param id       field id
     * @param type     field type
     * @param rawValue raw value of the field
     *
     * @return {@link ScField} object
     */
    public static ScField createFieldFromType(Type type, String name, String id, String rawValue) {
        switch (type) {
            case CHECKBOX:
                return new CheckBoxField(name, id, rawValue);

            case CHECKLIST:
                return new ChecklistField(name, id, rawValue);

            case MULTILIST:
                return new MultilistField(name, id, rawValue);

            case TREELIST:
                return new TreelistField(name, id, rawValue);

            case DROPLINK:
                return new DroplinkField(name, id, rawValue);

            case DROPTREE:
                return new DroptreeField(name, id, rawValue);

            case DATE:
                return new ScDateField(name, id, Type.DATE, rawValue);
            case DATE_TIME:
                return new ScDateField(name, id, Type.DATE_TIME, rawValue);

            case RICHTEXT:
                return new RichTextField(name, id, rawValue);

            case IMAGE:
                return new ImageField(name, id, rawValue);

            case AUDIO:
                return new MediaField(name, id, Type.AUDIO, rawValue);
            case DOC:
                return new MediaField(name, id, Type.DOC, rawValue);
            case DOCUMENT:
                return new MediaField(name, id, Type.DOCUMENT, rawValue);
            case DOCX:
                return new MediaField(name, id, Type.DOCX, rawValue);
            case FILE:
                return new MediaField(name, id, Type.FILE, rawValue);
            case FLASH:
                return new MediaField(name, id, Type.FLASH, rawValue);
            case JPEG:
                return new MediaField(name, id, Type.JPEG, rawValue);
            case MOVIE:
                return new MediaField(name, id, Type.MOVIE, rawValue);
            case MP3:
                return new MediaField(name, id, Type.MP3, rawValue);
            case PDF:
                return new MediaField(name, id, Type.PDF, rawValue);
            case ZIP:
                return new MediaField(name, id, Type.ZIP, rawValue);

            default:
                return new ScField(name, id, type, rawValue);
        }
    }

    public ContentProviderOperation toInsertOperation(String itemId) {
        final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Fields.CONTENT_URI);

        builder.withValue(Fields.ITEM_ID, itemId);
        builder.withValue(Fields.FIELD_ID, mId);
        builder.withValue(Fields.NAME, mName);
        builder.withValue(Fields.TYPE, mType.mValue);
        builder.withValue(Fields.VALUE, mRawValue);

        return builder.build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeParcelable(this.mType, flags);
        dest.writeString(this.mRawValue);
    }

    public static Parcelable.Creator<ScField> CREATOR = new Parcelable.Creator<ScField>() {
        public ScField createFromParcel(Parcel source) {
            String id = source.readString();
            String name = source.readString();
            Type type = source.readParcelable(Type.class.getClassLoader());
            String rawValue = source.readString();

            return ScField.createFieldFromType(type, name, id, rawValue);
        }

        public ScField[] newArray(int size) {
            return new ScField[size];
        }
    };
}
