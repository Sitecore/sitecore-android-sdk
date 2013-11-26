package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.LinkedList;
import java.util.List;

import net.sitecore.android.sdk.api.R;
import net.sitecore.android.sdk.api.RequestQueueProvider;
import net.sitecore.android.sdk.api.ScApiSession;
import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.RequestScope;
import net.sitecore.android.sdk.api.model.ScItem;
import net.sitecore.android.sdk.api.model.ScItemsLoader;

import static android.app.LoaderManager.LoaderCallbacks;
import static android.view.View.OnClickListener;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static net.sitecore.android.sdk.api.LogUtils.LOGD;
import static net.sitecore.android.sdk.api.provider.ScItemsContract.Items;

/**
 * Items browser fragment.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsBrowserFragment extends DialogFragment {

    private static final String BY_ITEM_PARENT_ID = Items.PARENT_ITEM_ID + "=?";
    private static final String EXTRA_ITEM_ID = "item_id";

    private static final String SAVED_ITEMS = "items";

    /**
     * Defines how to map {@link ScItem} to list item view.
     */
    public interface ItemViewBinder {

        /**
         * Bind an existing view to the item.
         */
        public void bindView(Context context, View v, ScItem item);

        /**
         * Makes a new view to hold the data of the item.
         */
        public View newView(Context context, LayoutInflater inflater, ScItem item);

    }

    /**
     * Defines navigation callback methods.
     */
    public interface NavigationEventsListener {

        /**
         * @param item Current {@link ScItem} after event finished.
         */
        public void onGoUp(ScItem item);

        /**
         *
         * @param item Current {@link ScItem} after event finished.
         */
        public void onGoInside(ScItem item);
    }

    private static final NavigationEventsListener sEmptyNavigationEventsListener = new NavigationEventsListener() {
        @Override
        public void onGoUp(ScItem item) {
        }

        @Override
        public void onGoInside(ScItem item) {
        }
    };

    /**
     * Defines network events callback methods.
     */
    public interface NetworkEventsListener {

        /**
         *
         */
        public void onUpdateRequestStarted();

        /**
         *
         */
        public void onUpdateRequestFinished();
    }

    private static final NetworkEventsListener sEmptyNetworkEventsListener = new NetworkEventsListener() {
        @Override
        public void onUpdateRequestStarted() {
        }

        @Override
        public void onUpdateRequestFinished() {
        }
    };

    private NavigationEventsListener mNavigationEventsListener = sEmptyNavigationEventsListener;
    private NetworkEventsListener mNetworkEventsListener = sEmptyNetworkEventsListener;

    private View mContainerProgress;
    private LinearLayout mContainerList;
    private View mGoUpView;
    private ListView mListView;

    private ScItemsAdapter mAdapter;

    private RequestQueue mRequestQueue;
    private ScApiSession mApiSession;

    private LinkedList<ScItem> mItems = new LinkedList<ScItem>();

    private boolean mIsLoading = true;

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
            LOGD("UP clicked in: " + mItems.peek().getId());
            mItems.pop();

            final ScItem newCurrentItem = mItems.peek();
            String newCurrentItemId = newCurrentItem.getId();
            LOGD("New folder item id: " + newCurrentItemId);

            sendUpdateChildrenRequest(newCurrentItemId);
            reloadChildrenFromDatabase(newCurrentItemId);

            if (mItems.size() == 1) {
                mGoUpView.setVisibility(View.GONE);
            }
            mNavigationEventsListener.onGoUp(newCurrentItem);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // TODO: load saved state
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: save items list, mApiSession(?)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: replace inflation with code
        final View v = inflater.inflate(R.layout.fragment_items_browser, container, false);

        if (getDialog() != null) getDialog().setTitle("Items Browser");
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContainerProgress = view.findViewById(R.id.container_progress);
        mContainerList = (LinearLayout) view.findViewById(R.id.container_list);

        mGoUpView = onCreateUpButtonView(LayoutInflater.from(getActivity()));
        mGoUpView.setVisibility(View.GONE);
        mGoUpView.setOnClickListener(mOnUpClickListener);

        mContainerList.addView(mGoUpView, 0, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    /**
     * Creates view, intended for Up navigation through items tree. This view will be added above items browser list.
     * After creation {@link OnClickListener} will be set to created view, which triggers navigation up.
     *
     * @param inflater LayoutInflater object.
     * @return View, intended for Up navigation through items tree. After creation will have
     */
    protected View onCreateUpButtonView(LayoutInflater inflater) {
        final Button upButton = new Button(getActivity());
        upButton.setText("..");
        return upButton;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
        mContainerProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mContainerList.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mRequestQueue = RequestQueueProvider.getRequestQueue(getActivity());
    }

    @Override
    public void onDetach() {
        mRequestQueue.cancelAll(this);
        super.onDetach();
    }

    public void setApiSession(ScApiSession session) {
        mApiSession = session;
        mApiSession.setShouldCache(true);

        ScRequest request = mApiSession.getItems(mFirstItemResponseListener, mErrorListener)
                .withScope(RequestScope.SELF, RequestScope.CHILDREN)
                .byItemPath("/sitecore/content/Home")
                .build();

        request.setTag(ItemsBrowserFragment.this);

        mRequestQueue.add(request);
        setLoading(true);
    }

    private void reloadChildrenFromDatabase(String itemId) {
        LOGD("Reload db children of: " + itemId);
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ITEM_ID, itemId);
        getLoaderManager().restartLoader(0, bundle, mLoaderCallbacks);
    }


    private void sendUpdateChildrenRequest(String itemId) {
        LOGD("getChildren: " + itemId);
        if (mApiSession != null) {
            ScRequest request = mApiSession.getItems(mItemsResponseListener, mErrorListener)
                    .byItemId(itemId)
                    .withScope(RequestScope.CHILDREN)
                    .build();
            request.setTag(ItemsBrowserFragment.this);

            ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Items.CONTENT_URI);
            builder.withSelection(BY_ITEM_PARENT_ID, new String[]{itemId});
            request.addOperationBeforeSuccessfulResponseSaved(builder.build());

            mRequestQueue.add(request);
        }
    }

    private final Response.Listener<ItemsResponse> mFirstItemResponseListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse itemsResponse) {
            setLoading(false);
            ScItem item = itemsResponse.getItems().get(0);
            mItems.push(item);

            final Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ITEM_ID, item.getId());
            getLoaderManager().initLoader(0, bundle, mLoaderCallbacks);
        }
    };

    private final Response.Listener<ItemsResponse> mItemsResponseListener = new Response.Listener<ItemsResponse>() {
        @Override
        public void onResponse(ItemsResponse itemsResponse) {
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            setLoading(false);
        }
    };

    private final LoaderCallbacks<List<ScItem>> mLoaderCallbacks = new LoaderCallbacks<List<ScItem>>() {

        @Override
        public Loader<List<ScItem>> onCreateLoader(int id, Bundle args) {
            if (args == null) return new ScItemsLoader(getActivity(), null, null);

            final String currentItemId = args.getString("item_id");
            return new ScItemsLoader(getActivity(), BY_ITEM_PARENT_ID, new String[]{currentItemId});
        }

        @Override
        public void onLoadFinished(Loader<List<ScItem>> loader, List<ScItem> data) {
            mAdapter = new ScItemsAdapter(getActivity(), data, onGetItemView());
            mListView.setAdapter(mAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<ScItem>> loader) {
            mAdapter.clear();
        }
    };

    /**
     * @return Current item or null if data wasn't loaded.
     */
    public ScItem getCurrentItem() {
        return mItems.peek();
    }

    /**
     * @param item clicked
     */
    public void onScItemClick(ScItem item) {
        if (item.hasChildren()) {
            LOGD("New folder item id: " + item.getId());
            mItems.push(item);

            if (mGoUpView.getVisibility() == View.GONE) mGoUpView.setVisibility(View.VISIBLE);

            String itemId = item.getId();
            sendUpdateChildrenRequest(itemId);
            reloadChildrenFromDatabase(itemId);
            mNavigationEventsListener.onGoInside(item);
        }
    }

    /**
     * @param item
     *
     * @return true if the callback consumed the long click, false otherwise
     */
    public void onScItemLongClick(ScItem item) {
    }

    /**
     * @return
     */
    protected ItemViewBinder onGetItemView() {
        return new DefaultItemViewBinder();
    }

    public void setNavigationEventsListener(NavigationEventsListener navigationEventsListener) {
        mNavigationEventsListener = navigationEventsListener;
    }

    public void setNetworkEventsListener(NetworkEventsListener networkEventsListener) {
        mNetworkEventsListener = networkEventsListener;
    }
}
