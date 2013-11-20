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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScItemsLoader extends AsyncTaskLoader<List<ScItem>> {

    private final ForceLoadContentObserver mObserver;


    public ScItemsLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    @Override
    public List<ScItem> loadInBackground() {
        Cursor c = getContext().getContentResolver().query(Items.CONTENT_URI, Items.Query.PROJECTION, null, null, null);

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
    protected void onStartLoading() {
        if (isStarted()) forceLoad();
    }

}
