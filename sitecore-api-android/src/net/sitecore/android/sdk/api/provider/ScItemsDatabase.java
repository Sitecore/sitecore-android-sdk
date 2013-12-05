package net.sitecore.android.sdk.api.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import net.sitecore.android.sdk.api.provider.ScItemsContract.FieldsColumns;
import net.sitecore.android.sdk.api.provider.ScItemsContract.ItemsColumns;

import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

public class ScItemsDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "sitecore_items.db";

    /**
     * Initial release
     */
    private static final int V_1_0 = 1;

    /**
     * Item.HAS_CHILDREN column added;
     */
    private static final int V_1_1 = 2;

    private static final int VERSION = V_1_1;

    public interface Tables {
        String ITEMS = "items";
        String FIELDS = "fields";

        String ITEMS_JOIN_FIELDS = ITEMS + " LEFT OUTER JOIN " + FIELDS + " ON "
                + ITEMS + "." + Items.ITEM_ID + "=" + FIELDS + "." + Fields.ITEM_ID;

    }

    public ScItemsDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.ITEMS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ItemsColumns.TIMESTAMP + " INTEGER NOT NULL,"
                + ItemsColumns.ITEM_ID + " TEXT NOT NULL,"
                + ItemsColumns.DISPLAY_NAME + " TEXT NOT NULL,"
                + ItemsColumns.PATH + " TEXT NOT NULL,"
                + ItemsColumns.TEMPLATE + " TEXT NOT NULL,"
                + ItemsColumns.LONG_ID + " TEXT NOT NULL,"
                + ItemsColumns.PARENT_ITEM_ID + " TEXT,"
                + ItemsColumns.VERSION + " TEXT NOT NULL,"
                + ItemsColumns.DATABASE + " TEXT NOT NULL,"
                + ItemsColumns.LANGUAGE + " TEXT NOT NULL,"
                + ItemsColumns.HAS_CHILDREN + " TEXT NOT NULL,"
                + ItemsColumns.TAG + " TEXT, "
                + "UNIQUE (" + ItemsColumns.ITEM_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.FIELDS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FieldsColumns.FIELD_ID + " TEXT NOT NULL,"
                + FieldsColumns.NAME + " TEXT NOT NULL,"
                + FieldsColumns.TYPE + " TEXT NOT NULL,"
                + FieldsColumns.VALUE + " TEXT NOT NULL,"
                + FieldsColumns.ITEM_ID + " TEXT NOT NULL,"
                + "UNIQUE (" + FieldsColumns.FIELD_ID + ", " + Fields.ITEM_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.FIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ITEMS);

        onCreate(db);
    }
}
