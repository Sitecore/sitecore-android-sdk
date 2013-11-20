package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import net.sitecore.android.sdk.api.R;
import net.sitecore.android.sdk.api.ScApiSession;
import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScItem;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ItemsBrowserFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<List<ScItem>>,
        Response.Listener<ItemsResponse>, Response.ErrorListener {

    public interface ItemViewBinder {

        public void bindView(Context context, View v, ScItem item);

        public View newView(Context context, LayoutInflater inflater, ScItem item);

    }

    public interface OnScItemClickListener {
        public void onScItemClick(ScItem item);
    }

    @Override
    public void onResponse(ItemsResponse itemsResponse) {

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }


    private ListView mListView;

    private ScApiSession mApiSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_items_browser, container, false);
        mListView = (ListView) v.findViewById(android.R.id.list);
        if (getDialog() != null) getDialog().setTitle("Items Browser");
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        ScApiSession.getSession(getActivity(), "http://scmobileteam.cloudapp.net", "extranet\\creatorex", "creatorex",
                new Response.Listener<ScApiSession>() {
            @Override
            public void onResponse(ScApiSession scApiSession) {
                mApiSession = scApiSession;
                mApiSession.setShouldCache(true);
            }
        });
    }

    @Override
    public Loader<List<ScItem>> onCreateLoader(int id, Bundle args) {
        //TODO: add selection, args
        return new ScItemsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ScItem>> loader, List<ScItem> data) {
        mListView.setAdapter(new ScItemsAdapter(getActivity(), data));

    }

    @Override
    public void onLoaderReset(Loader<List<ScItem>> loader) {
    }
}
