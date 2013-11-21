package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import net.sitecore.android.sdk.api.model.ScItem;
import net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

import static net.sitecore.android.sdk.api.LogUtils.LOGV;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScItemsLoader extends AsyncTaskLoader<List<ScItem>> {

    private final ForceLoadContentObserver mObserver;

    private String mSelection;
    private String[] mSelectionArgs;

    private List<ScItem> mItems;

    public ScItemsLoader(Context context) {
        this(context, null, null);
    }

    public ScItemsLoader(Context context, String selection, String[] selectionArgs) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mSelection = selection;
        mSelectionArgs = selectionArgs;
    }

    @Override
    public List<ScItem> loadInBackground() {
        Cursor c = getContext().getContentResolver().query(Items.CONTENT_URI, Items.Query.PROJECTION, mSelection, mSelectionArgs, null);
        LOGV("ScItemsLoader loading %s [%s]", mSelection, mSelectionArgs);
        if (c != null) {
            c.registerContentObserver(mObserver);
        }

        ArrayList<ScItem> result = new ArrayList<ScItem>();
        if (c.moveToFirst()) {
            do {
                result.add(ScItem.from(c));
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
}
