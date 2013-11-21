package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import net.sitecore.android.sdk.api.provider.ScItemsContract;

import static android.app.LoaderManager.LoaderCallbacks;
import static net.sitecore.android.sdk.api.LogUtils.LOGD;

/**
 * Items browser fragment
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsBrowserFragment extends DialogFragment {

    private static final String BY_ITEM_PARENT_ID = ScItemsContract.Items.PARENT_ITEM_ID + "=?";
    private static final String EXTRA_ITEM_ID = "item_id";
    private RequestQueue mRequestQueue;


    public interface ItemViewBinder {

        public void bindView(Context context, View v, ScItem item);

        public View newView(Context context, LayoutInflater inflater, ScItem item);

    }

    private View mContainerProgress;
    private View mContainerList;

    private View mGoUpView;
    private ListView mListView;
    private ScItemsAdapter mAdapter;

    private ScApiSession mApiSession;

    private LinkedList<ScItem> mItems = new LinkedList<ScItem>();

    private boolean mIsLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_items_browser, container, false);
        if (getDialog() != null) getDialog().setTitle("Items Browser");
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContainerProgress = view.findViewById(R.id.container_progress);
        mContainerList = view.findViewById(R.id.container_list);

        mGoUpView = view.findViewById(R.id.button_up);
        mGoUpView.setOnClickListener(mOnUpClickListener);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnItemLongClickListener(mOnItemLongClickListener);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ScApiSession.getSession(getActivity(), "http://scmobileteam.cloudapp.net", "extranet\\creatorex", "creatorex",
                new Response.Listener<ScApiSession>() {
                    @Override
                    public void onResponse(ScApiSession scApiSession) {
                        mApiSession = scApiSession;
                        mApiSession.setShouldCache(true);

                        ScRequest request = mApiSession.getItems(mFirstItemResponseListener, mErrorListener)
                                .withScope(RequestScope.SELF, RequestScope.CHILDREN)
                                .byItemPath("/sitecore/content/Home")
                                .build();

                        request.setTag(ItemsBrowserFragment.this);

                        mRequestQueue.add(request);
                        setLoading(true);
                    }
                });
    }

    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ScItem item = mAdapter.getItem(position);
            LOGD("onClick: " + item.getDisplayName());

            if (item.hasChildren()) {
                LOGD("New folder item id: " + item.getId());
                mItems.push(item);

                if (mGoUpView.getVisibility() == View.GONE) mGoUpView.setVisibility(View.VISIBLE);

                String itemId = item.getId();
                sendUpdateChildrenRequest(itemId);
                reloadChildrenFromDatabase(itemId);
            } else {
                Toast.makeText(getActivity(), "TODO: handle item without children click", Toast.LENGTH_SHORT).show();
                // TODO: handle item without children click
            }

        }
    };

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
            //TODO: add remove old children logic
            mRequestQueue.add(request);
        }
    }

    private final AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: handle long item click
            return false;
        }
    };

    private View.OnClickListener mOnUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LOGD("UP clicked in: " + mItems.peek().getId());
            mItems.pop();

            String newCurrentItemId = mItems.peek().getId();
            LOGD("New folder item id: " + newCurrentItemId);

            sendUpdateChildrenRequest(newCurrentItemId);
            reloadChildrenFromDatabase(newCurrentItemId);

            if (mItems.size() == 1) {
                mGoUpView.setVisibility(View.GONE);
            }
        }
    };

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
            Toast.makeText(getActivity(), "" + itemsResponse.getResultCount() + " items loaded", Toast.LENGTH_SHORT).show();
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            setLoading(false);
            Toast.makeText(getActivity(), "get items error", Toast.LENGTH_SHORT).show();
        }
    };

    public ItemViewBinder getItemViewBinder() {
        return new ScItemsAdapter.DefaultItemViewBinder();
    }

    private final LoaderCallbacks<List<ScItem>> mLoaderCallbacks = new LoaderCallbacks<List<ScItem>>() {

        @Override
        public Loader<List<ScItem>> onCreateLoader(int id, Bundle args) {
            if (args == null) return new ScItemsLoader(getActivity(), null, null);

            final String currentItemId = args.getString("item_id");
            return new ScItemsLoader(getActivity(), BY_ITEM_PARENT_ID, new String[]{currentItemId});
        }

        @Override
        public void onLoadFinished(Loader<List<ScItem>> loader, List<ScItem> data) {
            mAdapter = new ScItemsAdapter(getActivity(), data, getItemViewBinder());
            mListView.setAdapter(mAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<ScItem>> loader) {
            mAdapter.clear();
        }
    };


}
