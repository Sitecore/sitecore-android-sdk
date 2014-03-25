package net.sitecore.android.sdk.api.model;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;
import net.sitecore.android.sdk.api.provider.ScItemsProvider;

import static net.sitecore.android.sdk.api.internal.LogUtils.LOGV;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import static net.sitecore.android.sdk.api.provider.ScItemsDatabase.Tables;

/**
 * A loader that queries {@link ScItemsProvider} and returns {@code List<ScItem>}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScItemsLoader extends AsyncTaskLoader<List<ScItem>> {

    private final ForceLoadContentObserver mObserver;

    private final String mSelection;
    private final String[] mSelectionArgs;

    private List<ScItem> mItems;
    private ContentResolver mContentResolver;

    public ScItemsLoader(Context context) {
        this(context, null, null);
    }

    public ScItemsLoader(Context context, String selection, String[] selectionArgs) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mSelection = selection;
        mSelectionArgs = selectionArgs;

        mContentResolver = context.getContentResolver();
    }

    void setContentResolver(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    @Override
    public List<ScItem> loadInBackground() {
        final Cursor c = mContentResolver.query(Items.ITEMS_JOIN_FIELDS_URI,
                ItemsQuery.PROJECTION,
                mSelection, mSelectionArgs,
                ItemsQuery.SORT_ORDER);

        if (c != null) {
            c.registerContentObserver(mObserver);
        }

        return parseCursor(c);
    }

    private ArrayList<ScItem> parseCursor(Cursor c) {
        final ArrayList<ScItem> result = new ArrayList<ScItem>();
        if (!c.moveToFirst()) return result;

        String currentItemId = null;
        ScItem currentItem = null;
        do {
            final String rowItemId = c.getString(ItemsQuery.ITEM_ID);
            if (currentItemId == null || !currentItemId.equals(rowItemId)) {
                currentItemId = rowItemId;

                currentItem = itemFrom(c);
                result.add(currentItem);
            }

            final String fieldId = c.getString(ItemsQuery.FIELD_ID);
            if (!TextUtils.isEmpty(fieldId)) {
                ScField rowField = fieldFrom(c);
                currentItem.addField(rowField);
            }
        } while (c.moveToNext());

        return result;
    }

    private ScItem itemFrom(Cursor c) {
        final ScItem item = new ScItem();

        item.setId(c.getString(ItemsQuery.ITEM_ID));
        item.setDisplayName(c.getString(ItemsQuery.DISPLAY_NAME));
        item.setPath(c.getString(ItemsQuery.PATH));
        item.setTemplate(c.getString(ItemsQuery.TEMPLATE));
        item.setLongId(c.getString(ItemsQuery.LONG_ID));
        item.setVersion(c.getInt(ItemsQuery.VERSION));
        item.setDatabase(c.getString(ItemsQuery.DATABASE));
        item.setLanguage(c.getString(ItemsQuery.LANGUAGE));
        item.setHasChildren(c.getInt(ItemsQuery.HAS_CHILDREN) == 1);

        //TODO: add timestamp?
        //item.mTimestamp = c.getString(ItemsQuery.TIMESTAMP);
        //TODO: add tag?

        return item;
    }

    private ScField fieldFrom(Cursor c) {
        final String fieldId = c.getString(ItemsQuery.FIELD_ID);
        final String fieldName = c.getString(ItemsQuery.FIELD_NAME);
        final String fieldValue = c.getString(ItemsQuery.FIELD_VALUE);
        final ScField.Type type = ScField.Type.getByName(c.getString(ItemsQuery.FIELD_TYPE));

        return ScField.createFieldFromType(type, fieldName, fieldId, fieldValue);
    }

    @Override
    public void deliverResult(List<ScItem> data) {
        LOGV("ScItemsLoader loaded %s items.", data.size());
        if (isReset()) {
            return;
        }

        mItems = data;

        if (isStarted()) super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mItems != null) {
            deliverResult(mItems);
        }

        if (takeContentChanged() || mItems == null) {
            forceLoad();
        }
    }

    @Override
    public void onReset() {
        super.onReset();
        onStopLoading();
        mItems = null;
    }

    @Override
    protected void onForceLoad() {
        mItems = null;
        super.onForceLoad();
    }

    private interface ItemsQuery {
        String[] PROJECTION = {
                // Items columns
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
                Items.LANGUAGE,
                Items.HAS_CHILDREN,
                Items.TAG,

                // Fields columns
                Fields.FIELD_ID,
                Fields.NAME,
                Fields.TYPE,
                Fields.VALUE
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
        int HAS_CHILDREN = 11;
        int TAG = 12;

        int FIELD_ID = 13;
        int FIELD_NAME = 14;
        int FIELD_TYPE = 15;
        int FIELD_VALUE = 16;

        String SORT_ORDER = Tables.ITEMS + "." + Items.ITEM_ID + " asc, " + Tables.FIELDS + "." + Fields._ID + " asc";
    }
}
