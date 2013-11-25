package net.sitecore.android.sdk.api.provider;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import net.sitecore.android.sdk.api.model.ScItem;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

import static net.sitecore.android.sdk.api.LogUtils.LOGV;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Fields;
import static net.sitecore.android.sdk.api.provider.ScItemsDatabase.Tables;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScItemsLoader extends AsyncTaskLoader<List<ScItem>> {

    private final ForceLoadContentObserver mObserver;

    private String mSelection;
    private String[] mSelectionArgs;

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
                ItemsFieldsQuery.PROJECTION,
                mSelection, mSelectionArgs,
                ItemsFieldsQuery.SORT_ORDER);

        LOGV("ScItemsLoader loading %s [%s]", mSelection, mSelectionArgs);
        if (c != null) {
            c.registerContentObserver(mObserver);
        }

        return parseCursor(c);
    }

    private ArrayList<ScItem> parseCursor(Cursor c) {
        ArrayList<ScItem> result = new ArrayList<ScItem>();
        if (c.moveToFirst()) {
            String currentItemId;
            ScItem currentItem = null;
            do {
                //String rowItemId = c.getString(Items.Query.ITEM_ID);
                //ScField rowField = fieldFrom(c);
                if (currentItem == null) {
                    //currentItem.addField(rowField);
                } else {
                    //currentItem.addField(rowField);


                }

                currentItem = ScItem.from(c);
                result.add(currentItem);


            } while (c.moveToNext());
        }

        return result;
    }

    @Override
    public void deliverResult(List<ScItem> data) {
        if (isReset()) {
            return;
        }

        mItems = data;

        if (isStarted()) super.deliverResult(data);

        //TODO: release old resources
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

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        LOGV("ScItemsLoader.onContentChanged()");
    }

    interface ItemsFieldsQuery {
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
                Fields.FIELD_ID
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

        String SORT_ORDER = Tables.ITEMS + "." + Items.ITEM_ID + " desc";
    }
}
