package net.sitecore.android.sdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import net.sitecore.android.sdk.api.model.ScItem;

import static net.sitecore.android.sdk.widget.ItemsBrowserFragment.ItemViewBinder;

public class ScItemsAdapter extends ArrayAdapter<ScItem> {

    private ItemViewBinder mViewBinder;

    public ScItemsAdapter(Context context, List<ScItem> objects) {
        this(context, objects, new DefaultItemViewBinder());
    }

    public ScItemsAdapter(Context context, List<ScItem> objects, ItemViewBinder viewBinder) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
        mViewBinder = viewBinder;
    }

    public void setViewBinder(ItemViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScItem item = getItem(position);
        View v;
        if (convertView == null) {
            v = mViewBinder.newView(getContext(), LayoutInflater.from(getContext()), item);
        } else {
            v = convertView;
        }
        mViewBinder.bindView(getContext(), v, item);
        return v;
    }

    static class DefaultItemViewBinder implements ItemViewBinder {

        @Override
        public void bindView(Context context, View v, ScItem item) {
            ViewHolder holder = (ViewHolder) v.getTag();

            holder.text1.setText(item.getDisplayName());
            holder.text2.setText(item.getPath());
        }

        @Override
        public View newView(Context context, LayoutInflater inflater, ScItem item) {
            final View v = inflater.inflate(android.R.layout.simple_list_item_2, null);
            final ViewHolder holder = new ViewHolder();
            holder.text1 = (TextView) v.findViewById(android.R.id.text1);
            holder.text2 = (TextView) v.findViewById(android.R.id.text2);
            v.setTag(holder);

            return v;
        }

        private static class ViewHolder {
            TextView text1;
            TextView text2;
        }

    }


}
