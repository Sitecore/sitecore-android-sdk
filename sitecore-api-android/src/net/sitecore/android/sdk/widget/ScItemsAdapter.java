package net.sitecore.android.sdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import net.sitecore.android.sdk.api.model.ScItem;

public class ScItemsAdapter extends ArrayAdapter<ScItem> {

    private ItemsBrowserFragment.ItemViewBinder mViewBinder;

    public ScItemsAdapter(Context context, List<ScItem> objects, ItemsBrowserFragment.ItemViewBinder viewBinder) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
        mViewBinder = viewBinder;
    }

    public void setItemViewBinder(ItemsBrowserFragment.ItemViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScItem item = getItem(position);
        View v;
        if (convertView == null) {
            v = mViewBinder.newView(getContext(), parent, LayoutInflater.from(getContext()), item);
        } else {
            v = convertView;
        }
        mViewBinder.bindView(getContext(), v, item);
        return v;
    }


}
