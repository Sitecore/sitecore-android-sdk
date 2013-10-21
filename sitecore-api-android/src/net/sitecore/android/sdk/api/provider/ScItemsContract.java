package net.sitecore.android.sdk.api.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import net.sitecore.android.sdk.api.model.ScField;
import net.sitecore.android.sdk.api.model.ScItem;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE;

/**
 * Contract between {@link ScItemsProvider} and other applications. Contains supported URIs and data columns.
 * </p>
 * <h3>Data tables:</h3>
 * <ul>
 * <li>{@link Items} stores downloaded items.</li>
 * <li>{@link Fields} contains fields of particular item.</li>
 * </ul>
 * <p/>
 * <h1>Supported Query URIs:</h1>
 * <p/>
 * <ul>
 * <li>{@code content://AUTHORITY/items} returns all items.</li>
 * <li>{@code content://AUTHORITY/items/{item_id}} returns specific item.</li>
 * <li>{@code content://AUTHORITY/fields} returns all fields.</li>
 * </ul>
 * <p/>
 * <h3>Delete:</h3>
 * <ul>
 * <li>{@code content://AUTHORITY/items} delete all items.</li>
 * <li>{@code content://AUTHORITY/fields} delete all fields item.</li>
 * </ul>
 */
public class ScItemsContract {

    public static final String CONTENT_AUTHORITY = "net.sitecore.android.sdk.api.provider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Defines data model how {@link ScItem} are stored in {@link ScItemsProvider}
     */
    public static class Items implements BaseColumns, ItemsColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("items").build();

        public static final String CONTENT_TYPE = CURSOR_DIR_BASE_TYPE + "/vnd.sitecore.items";
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM_BASE_TYPE + "/vnd.sitecore.item";

        /**
         * Builds {@link Uri} to specific item.
         */
        public static Uri buildItemUri(String itemId) {
            return CONTENT_URI.buildUpon().appendPath(itemId).build();
        }

        /** Read {@link #ITEM_ID} from {@link Items} {@link Uri} */
        public static String getItemId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        /** Defines PROJECTION and column indexes for {@link Items#CONTENT_URI} queries. */
        public interface Query {
            String[] PROJECTION = {
                    Items._ID,
                    Items.ITEM_ID,
                    Items.DISPLAY_NAME,
                    Items.PATH,
                    Items.TEMPLATE,
                    Items.LONG_ID,
                    Items.PARENT_ITEM_ID,
                    Items.TIMESTAMP,
                    Items.VERSION,
                    Items.DATABASE,
                    Items.LANGUAGE
            };

            int _ID = 0;
            int ITEM_ID = 1;
            int DISPLAY_NAME = 2;
            int PATH = 3;
            int TEMPLATE = 4;
            int LONG_ID = 5;
            int PARENT_ITEM_ID = 6;
            int TIMESTAMP = 7;
            int VERSION = 8;
            int DATABASE = 9;
            int LANGUAGE = 10;
        }
    }

    /**
     * Defines data model how {@link ScField} are stored in {@link ScItemsProvider}
     */
    public static class Fields implements BaseColumns, FieldsColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("fields").build();

        public static final String CONTENT_TYPE = CURSOR_DIR_BASE_TYPE + "/vnd.sitecore.fields";
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM_BASE_TYPE + "/vnd.sitecore.field";

        /**
         * Builds {@link Uri} to specific field.
         */
        public static Uri buildFieldUri(String fieldId) {
            return CONTENT_URI.buildUpon().appendPath(fieldId).build();
        }

        /** Read {@link #FIELD_ID} from {@link Fields} {@link Uri} */
        public static String getFieldId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        /** Defines PROJECTION and column indexes for {@link Fields#CONTENT_URI} queries. */
        public interface Query {
            String[] PROJECTION = {
                    Fields._ID,
                    Fields.FIELD_ID,
                    Fields.NAME,
                    Fields.TYPE,
                    Fields.VALUE,
                    Fields.ITEM_ID
            };

            int _ID = 0;
            int FIELD_ID = 1;
            int NAME = 2;
            int TYPE = 3;
            int VALUE = 4;
            int ITEM_ID = 5;
        }
    }

    interface ItemsColumns {
        String ITEM_ID = "item_id";
        String DISPLAY_NAME = "display_name";
        String PATH = "path";
        String TEMPLATE = "template";
        String LONG_ID = "long_id";
        String PARENT_ITEM_ID = "parent_item_id";

        /** Timestamp when row was inserted/updated */
        String TIMESTAMP = "timestamp";

        String VERSION = "version";
        String DATABASE = "database";
        String LANGUAGE = "language";
    }

    interface FieldsColumns {
        String FIELD_ID = "field_id";
        String NAME = "name";
        String TYPE = "type";
        String VALUE = "value";
        String ITEM_ID = "item_id";
    }

}
