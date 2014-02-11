package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentProviderOperation;
import android.content.Loader;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.LinkedList;
import java.util.List;

import net.sitecore.android.sdk.api.R;
import net.sitecore.android.sdk.api.ScApiSession;
import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.RequestScope;
import net.sitecore.android.sdk.api.model.ScItem;
import net.sitecore.android.sdk.api.model.ScItemsLoader;
import net.sitecore.android.sdk.api.provider.ScItemsProvider;

import static android.app.LoaderManager.LoaderCallbacks;
import static android.view.View.OnClickListener;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static net.sitecore.android.sdk.api.LogUtils.LOGE;
import static net.sitecore.android.sdk.api.LogUtils.LOGV;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/**
 * <p>
 * A fragment that allows to browse through Sitecore content tree. It manages all network events
 * and caches successful responses using {@link ScItemsProvider} content provider.
 * Under the hood items are loaded using {@link ScApiSession} and cached in database using {@link ScItemsProvider}.
 * </p>
 * Since fragment extends {@link DialogFragment}, it can be used as modal dialog as well.
 * <p><strong>UI customization</strong></p>
 * Fragment provides default UI, and it can be fully customized. By default content is represented by {@link ListView},
 * and it can be changed to {@link GridView} using {@link #setGridStyle(int)} method.
 * Next methods also can be overridden:
 * <ul>
 * <li> {@link #onCreateHeaderView} creates view, which is shown above the content and always visible.
 * <li> {@link #onCreateFooterView} creates view, which is shown below the content and always visible.
 * <li> {@link #onCreateUpButtonView} creates 'up' navigation button.
 * <li> {@link #onCreateEmptyView} creates view, which will be shown if there's no content on current level.
 * By default empty {@link TextView} is created, and it's value can be set using {@link #setEmptyText}.
 * <li> {@link #onCreateItemViewBinder} returns instance of {@link ItemViewBinder} interface,
 * which defines how {@link View} will be create from {@link ScItem}.
 * </ul>
 * <p>
 * <strong>Listening to events</strong>
 * </p>
 * Next methods can be used for listening various events:
 * <ul>
 * <li> {@link #onScItemClick(ScItem)}
 * <li> {@link #onScItemLongClick(ScItem)}
 * <li> {@link #setContentTreePositionListener}
 * <li> {@link #setNetworkEventsListener}
 * </ul>
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsBrowserFragment extends DialogFragment {

    /**
     * Default root items browser folder.
     *
     * @see #setRootFolder(String)
     */
    public static final String DEFAULT_ROOT_FOLDER = "/sitecore/content/Home";

    /** Default number of columns in grid mode. */
    public static final int DEFAULT_GRID_COLUMNS_COUNT = 2;

    private static final String EXTRA_ITEM_ID = "item_id";
    private static final String EXTRA_ITEM_PATH = "item_path";

    private static final int STYLE_LIST = 0;
    private static final int STYLE_GRID = 1;

    private static final int LOADER_CHILD_ITEMS = 0;
    private static final int LOADER_ROOT_ITEM = 1;

    /**
     * Defines content tree position change callback methods.
     */
    public interface ContentTreePositionListener {

        /**
         * Notifies that content tree position changed to ancestor item.
         *
         * @param item Current {@link ScItem} after event finished.
         */
        public void onGoUp(ScItem item);

        /**
         * Notifies that content tree position changed to descendant item.
         *
         * @param item Current {@link ScItem} after event finished.
         */
        public void onGoInside(ScItem item);

        /**
         * Notifies that content tree loaded using root item.
         *
         * @param item Current {@link ScItem} after initialization.
         *
         * @see #DEFAULT_ROOT_FOLDER
         * @see #setRootFolder(String)
         */
        public void onInitialized(ScItem item);
    }

    /**
     * Defines network events callback methods.
     */
    public interface NetworkEventsListener {

        /**
         * Notifies that items update request has started.
         */
        public void onUpdateRequestStarted();

        /**
         * Notifies that items update request has finished successfully.
         *
         * @param itemsResponse List of {@link ScItem} received by update operation.
         */
        public void onUpdateSuccess(ItemsResponse itemsResponse);

        /**
         * Notifies that items update request has finished with error.
         *
         * @param error describes error details.
         */
        public void onUpdateError(VolleyError error);
    }

    private static final ContentTreePositionListener sEmptyContentTreePositionListener = new ContentTreePositionListener() {
        @Override
        public void onGoUp(ScItem item) {
        }

        @Override
        public void onGoInside(ScItem item) {
        }

        @Override
        public void onInitialized(ScItem item) {
        }
    };

    private static final NetworkEventsListener sEmptyNetworkEventsListener = new NetworkEventsListener() {
        @Override
        public void onUpdateRequestStarted() {
        }

        @Override
        public void onUpdateSuccess(ItemsResponse itemsResponse) {
        }

        @Override
        public void onUpdateError(VolleyError error) {
        }
    };

    private ContentTreePositionListener mContentTreePositionListener = sEmptyContentTreePositionListener;
    private NetworkEventsListener mNetworkEventsListener = sEmptyNetworkEventsListener;

    private View mContainerEmpty;
    private View mContainerProgress;
    private LinearLayout mContainerList;
    private View mGoUpView;
    private AbsListView mListView;

    private ScItemsAdapter mAdapter;
    private ItemViewBinder mItemViewBinder = new DefaultItemViewBinder();

    private RequestQueue mRequestQueue;
    private ScApiSession mApiSession;

    private int mStyle = STYLE_LIST;
    private int mColumnCount = 2;
    private String mRootFolder = DEFAULT_ROOT_FOLDER;

    private LinkedList<ScItem> mItems = new LinkedList<ScItem>();

    private boolean mLoadContentWithoutConnection = false;

    private boolean mIsInitialized = false;

    /**
     * Describes whether fragment is created for the first time, or recreated from retained instance.
     */
    private boolean mFirstLoad = true;

    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onScItemClick(mAdapter.getItem(position));
        }
    };

    private final AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            onScItemLongClick(mAdapter.getItem(position));
            return true;
        }
    };

    private final OnClickListener mOnUpClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LOGV("UP clicked in: " + mItems.peek().getId());
            mItems.pop();

            final ScItem newCurrentItem = mItems.peek();
            String newCurrentItemId = newCurrentItem.getId();
            LOGV("New folder item id: " + newCurrentItemId);

            reloadChildrenFromNetwork(newCurrentItemId);
            reloadChildrenFromDatabase(newCurrentItemId);

            if (mItems.size() == 1) {
                mGoUpView.setVisibility(View.GONE);
            }
            mContentTreePositionListener.onGoUp(newCurrentItem);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_items_browser, container, false);

        mContainerProgress = v.findViewById(R.id.container_progress);
        mContainerList = (LinearLayout) v.findViewById(R.id.container_list);

        // Add header view if exists
        final View header = onCreateHeaderView(inflater);
        if (header != null) {
            mContainerList.addView(header, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }

        // Add up button
        mGoUpView = onCreateUpButtonView(inflater);
        if (mItems.size() < 2) mGoUpView.setVisibility(View.GONE);
        mGoUpView.setOnClickListener(mOnUpClickListener);
        mContainerList.addView(mGoUpView, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        // Add ListView/GridView
        if (mStyle == STYLE_LIST) {
            mListView = new ListView(getActivity());
        } else {
            GridView grid = new GridView(getActivity());
            grid.setNumColumns(mColumnCount);
            mListView = grid;
        }

        mListView.setDrawSelectorOnTop(false);
        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        listParams.weight = 1;
        mContainerList.addView(mListView, listParams);

        mContainerEmpty = onCreateEmptyView(inflater);
        mContainerEmpty.setVisibility(View.GONE);
        final LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        emptyParams.weight = 1;
        mContainerList.addView(mContainerEmpty, emptyParams);
        mListView.setEmptyView(mContainerEmpty);

        // Add footer view if exists
        final View footer = onCreateFooterView(inflater);
        if (footer != null) {
            mContainerList.addView(footer, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        changeProgressVisibility(mIsInitialized);

        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnItemLongClickListener(mOnItemLongClickListener);

        if (mFirstLoad || mItems.size() == 0) {
            if (mLoadContentWithoutConnection) {
                loadRootFromCache();

                mIsInitialized = true;
                changeProgressVisibility(mIsInitialized);
            } else if (mApiSession != null && mRequestQueue != null) {
                loadRootFromNetwork();

                mIsInitialized = false;
                changeProgressVisibility(mIsInitialized);
            }

            mFirstLoad = false;
        } else {
            // Deliver already loaded results.
            final Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ITEM_ID, mItems.peek().getId());
            getLoaderManager().initLoader(LOADER_CHILD_ITEMS, bundle, mChildrenLoader);
        }
    }

    /**
     * @param inflater {@link LayoutInflater} object that can be used to inflate any views.
     *
     * @return Created header view. {@code null} is returned by default.
     */
    protected View onCreateHeaderView(LayoutInflater inflater) {
        return null;
    }

    /**
     * @param inflater {@link LayoutInflater} object that can be used to inflate any views.
     *
     * @return Created footer view. {@code null} is returned by default.
     */
    protected View onCreateFooterView(LayoutInflater inflater) {
        return null;
    }

    /**
     * Creates view, intended for Up navigation through items tree. This view will be added above items browser list.
     * After creation {@link OnClickListener} will be set to created view, which triggers navigation up.
     *
     * @param inflater {@link LayoutInflater} object that can be used to inflate any views.
     *
     * @return View, intended for Up navigation through items tree.
     */
    protected View onCreateUpButtonView(LayoutInflater inflater) {
        final Button upButton = new Button(getActivity());
        upButton.setText("..");
        return upButton;
    }

    /**
     * @param inflater {@link LayoutInflater} object that can be used to inflate any views.
     *
     * @return Created view.
     */
    protected View onCreateEmptyView(LayoutInflater inflater) {
        final TextView empty = new TextView(getActivity());
        empty.setGravity(Gravity.CENTER);
        return empty;
    }

    /**
     * Override this method to change the way content views are created from {@link ScItem}.
     *
     * @return {@link ItemViewBinder}
     * @see ScItemsAdapter
     */
    protected ItemViewBinder onCreateItemViewBinder() {
        return mItemViewBinder;
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.ItemsBrowserFragment);

        mStyle = a.getInt(R.styleable.ItemsBrowserFragment_style, STYLE_LIST);
        mColumnCount = a.getInt(R.styleable.ItemsBrowserFragment_columnCount, DEFAULT_GRID_COLUMNS_COUNT);
        String root = a.getString(R.styleable.ItemsBrowserFragment_rootFolder);
        if (!TextUtils.isEmpty(root)) mRootFolder = root;

        mLoadContentWithoutConnection = a.getBoolean(R.styleable.ItemsBrowserFragment_loadContentWithoutConnection, false);

        a.recycle();
    }

    private void changeProgressVisibility(boolean isInitialized) {
        mContainerProgress.setVisibility(isInitialized ? View.GONE : View.VISIBLE);
        mContainerList.setVisibility(isInitialized ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDetach() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    /**
     * @param requestQueue {@link RequestQueue} which will execute the requests.
     * @param session      {@link ScApiSession} to create the requests.
     */
    public void setApiProperties(RequestQueue requestQueue, ScApiSession session) {
        mRequestQueue = requestQueue;

        mApiSession = session;
        mApiSession.setShouldCache(true);

        loadRootFromNetwork();
    }

    /**
     * @param loadContentWithoutConnection Use {@code true} to show cached content without setting {@link #setApiProperties}.
     */
    public void setLoadContentWithoutConnection(boolean loadContentWithoutConnection) {
        mLoadContentWithoutConnection = true;
        if (getView() != null) {
            loadRootFromCache();

            mIsInitialized = true;
            changeProgressVisibility(mIsInitialized);
        }
    }

    /**
     * @param rootFolder Top-level content tree folder. Fragment will be initialized using it as current folder.
     */
    public void setRootFolder(String rootFolder) {
        mRootFolder = rootFolder;
    }

    /**
     * Trigger manual update of current folder.
     */
    public void update() {
        final ScItem item = mItems.peek();
        reloadChildrenFromNetwork(item.getId());
    }

    private void loadRootFromCache() {
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ITEM_PATH, mRootFolder);
        getLoaderManager().initLoader(LOADER_ROOT_ITEM, bundle, mCachedRootItemLoader);
    }

    private void loadRootFromNetwork() {
        mNetworkEventsListener.onUpdateRequestStarted();
        ScRequest request = mApiSession.readItemsRequest(mFirstItemResponseListener, mErrorListener)
                .withScope(RequestScope.SELF, RequestScope.CHILDREN)
                .byItemPath(mRootFolder)
                .build();
        request.setTag(ItemsBrowserFragment.this);

        mRequestQueue.add(request);
    }

    private void reloadChildrenFromDatabase(String itemId) {
        LOGV("Reload db children of: " + itemId);
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ITEM_ID, itemId);
        getLoaderManager().restartLoader(LOADER_CHILD_ITEMS, bundle, mChildrenLoader);
    }

    private void reloadChildrenFromNetwork(String itemId) {
        LOGV("getChildren: " + itemId);
        if (mApiSession != null) {
            mNetworkEventsListener.onUpdateRequestStarted();
            ScRequest request = mApiSession.readItemsRequest(mItemsResponseListener, mErrorListener)
                    .byItemId(itemId)
                    .withScope(RequestScope.CHILDREN)
                    .build();
            request.setTag(ItemsBrowserFragment.this);

            ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Items.CONTENT_URI);
            builder.withSelection(Items.Query.BY_ITEM_PARENT_ID, new String[]{itemId});
            request.addOperationBeforeSuccessfulResponseSaved(builder.build());

            mRequestQueue.add(request);
        }
    }

    private final Response.Listener<ItemsResponse> mFirstItemResponseListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse itemsResponse) {
            mIsInitialized = true;
            changeProgressVisibility(mIsInitialized);

            if (itemsResponse.getItems().size() == 0) {
                return;
            }

            ScItem item = itemsResponse.getItems().get(0);
            mItems.push(item);

            mContentTreePositionListener.onInitialized(item);
            mNetworkEventsListener.onUpdateSuccess(itemsResponse);

            final Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ITEM_ID, item.getId());
            getLoaderManager().initLoader(LOADER_CHILD_ITEMS, bundle, mChildrenLoader);
        }
    };

    private final Response.Listener<ItemsResponse> mItemsResponseListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse itemsResponse) {
            mNetworkEventsListener.onUpdateSuccess(itemsResponse);
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            LOGE("Error response: " + error);

            if (!mIsInitialized) {
                mIsInitialized = true;
                changeProgressVisibility(mIsInitialized);
            }
            mNetworkEventsListener.onUpdateError(error);
        }
    };

    private final LoaderCallbacks<List<ScItem>> mCachedRootItemLoader = new LoaderCallbacks<List<ScItem>>() {

        @Override
        public Loader<List<ScItem>> onCreateLoader(int id, Bundle args) {
            final String path = args.getString(EXTRA_ITEM_PATH);
            return new ScItemsLoader(getActivity(), Items.Query.BY_ITEM_PATH, new String[]{path});
        }

        @Override
        public void onLoadFinished(Loader<List<ScItem>> loader, List<ScItem> data) {
            // Don't load items when root item is not in cache
            if (data == null || data.size() == 0) return;

            final ScItem item = data.get(0);
            if (item != null) mItems.push(item);

            mContentTreePositionListener.onInitialized(item);

            final Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ITEM_ID, item.getId());
            getLoaderManager().initLoader(LOADER_CHILD_ITEMS, bundle, mChildrenLoader);
        }

        @Override
        public void onLoaderReset(Loader<List<ScItem>> loader) {
        }
    };

    private final LoaderCallbacks<List<ScItem>> mChildrenLoader = new LoaderCallbacks<List<ScItem>>() {

        @Override
        public Loader<List<ScItem>> onCreateLoader(int id, Bundle args) {
            if (args == null) {
                return new ScItemsLoader(getActivity(), null, null);
            }

            final String currentItemId = args.getString(EXTRA_ITEM_ID);
            return new ScItemsLoader(getActivity(), Items.Query.BY_ITEM_PARENT_ID, new String[]{currentItemId});
        }

        @Override
        public void onLoadFinished(Loader<List<ScItem>> loader, List<ScItem> data) {
            mAdapter = new ScItemsAdapter(getActivity(), data, onCreateItemViewBinder());
            mListView.setAdapter(mAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<ScItem>> loader) {
            mAdapter.clear();
        }
    };

    /**
     * @param text Text to set.
     */
    public void setEmptyText(String text) {
        if (mContainerEmpty instanceof TextView) {
            final TextView tv = (TextView) mContainerEmpty;
            tv.setText(text);
        }
    }

    /**
     * @param textResourceId String resource id.
     */
    public void setEmptyText(int textResourceId) {
        if (mContainerEmpty instanceof TextView) {
            final TextView tv = (TextView) mContainerEmpty;
            tv.setText(textResourceId);
        }
    }

    /**
     * Show items as list.
     */
    public void setListStyle() {
        mStyle = STYLE_LIST;
    }

    /**
     * Show items as grid.
     *
     * @param columnCount Number of columns. Default value is {@link #DEFAULT_GRID_COLUMNS_COUNT}
     */
    public void setGridStyle(int columnCount) {
        mStyle = STYLE_GRID;
        mColumnCount = columnCount;
    }

    /**
     * @return Current item or null if data wasn't loaded.
     */
    public ScItem getCurrentItem() {
        return mItems.peek();
    }

    /**
     * @param item which received click event.
     */
    public void onScItemClick(ScItem item) {
        LOGV("New folder item id: " + item.getId());
        mItems.push(item);

        if (mGoUpView.getVisibility() == View.GONE) mGoUpView.setVisibility(View.VISIBLE);

        String itemId = item.getId();
        reloadChildrenFromNetwork(itemId);
        reloadChildrenFromDatabase(itemId);
        mContentTreePositionListener.onGoInside(item);
    }

    /**
     * @param item which received long click.
     */
    public void onScItemLongClick(ScItem item) {
    }

    /**
     * Register a callback to be invoked when content state changes.
     *
     * @param contentTreePositionListener the callback to be invoked.
     */
    public void setContentTreePositionListener(ContentTreePositionListener contentTreePositionListener) {
        mContentTreePositionListener = contentTreePositionListener;
    }

    /**
     * Register a callback to be invoked when network operations state changes.
     *
     * @param networkEventsListener the callback to be invoked.
     */
    public void setNetworkEventsListener(NetworkEventsListener networkEventsListener) {
        mNetworkEventsListener = networkEventsListener;
    }

}
