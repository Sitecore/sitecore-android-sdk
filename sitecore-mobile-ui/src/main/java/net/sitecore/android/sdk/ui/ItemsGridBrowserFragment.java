package net.sitecore.android.sdk.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * <p>
 * A fragment that allows to browse through Sitecore content tree. It manages all network events
 * and caches successful responses using {@link net.sitecore.android.sdk.api.provider.ScItemsProvider} content provider.
 * Under the hood items are loaded using {@link net.sitecore.android.sdk.api.ScApiSession} and cached in database using {@link net.sitecore.android.sdk.api.provider.ScItemsProvider}.
 * </p>
 * Since fragment extends {@link android.app.DialogFragment}, it can be used as modal dialog as well.
 * <p><strong>UI customization</strong></p>
 * Fragment provides default UI as {@link GridView}.
 * <p/>
 * Next methods also can be overridden:
 * <ul>
 * <li> {@link #onCreateHeaderView} creates view, which is shown above the content and always visible.
 * <li> {@link #onCreateFooterView} creates view, which is shown below the content and always visible.
 * <li> {@link #onCreateUpButtonView} creates 'up' navigation button.
 * <li> {@link #onCreateEmptyView} creates view, which will be shown if there's no content on current level.
 * By default empty {@link android.widget.TextView} is created, and it's value can be set using {@link #setEmptyText}.
 * <li> {@link #onCreateItemViewBinder} returns instance of {@link ItemViewBinder} interface,
 * which defines how {@link android.view.View} will be create from {@link net.sitecore.android.sdk.api.model.ScItem}.
 * </ul>
 * <p>
 * <strong>Listening to events</strong>
 * </p>
 * Next methods can be used for listening various events:
 * <ul>
 * <li> {@link #onScItemClick(net.sitecore.android.sdk.api.model.ScItem)}
 * <li> {@link #onScItemLongClick(net.sitecore.android.sdk.api.model.ScItem)}
 * <li> {@link #setContentTreePositionListener}
 * <li> {@link #setNetworkEventsListener}
 * </ul>
 *
 * @see ItemsListBrowserFragment
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsGridBrowserFragment extends ItemsBrowserFragment {

    private static final int DEFAULT_COLUMN_COUNT = 2;

    private int mColumnCount = DEFAULT_COLUMN_COUNT;

    @Override
    protected AbsListView getContentView() {
        GridView grid = new GridView(getActivity());
        grid.setNumColumns(mColumnCount);
        return grid;
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.ItemsGridBrowserFragment);
        mColumnCount = a.getInt(R.styleable.ItemsGridBrowserFragment_columnCount, DEFAULT_GRID_COLUMNS_COUNT);
        a.recycle();
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
    }
}
