package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

import net.sitecore.android.sdk.api.R;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsGridBrowserFragment extends ItemsBrowserFragment {

    private static final int DEFAULT_COLUMN_COUNT = 2;

    private int mColumnCount = DEFAULT_COLUMN_COUNT;

    @Override
    protected AbsListView getContentView() {
        GridView grid = new GridView(getActivity());
        grid.setNumColumns(mColumnCount);
        return null;
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.ItemsGridBrowserFragment);
        mColumnCount = a.getInt(R.styleable.ItemsGridBrowserFragment_columnCount, DEFAULT_GRID_COLUMNS_COUNT);
        a.recycle();
    }
}
