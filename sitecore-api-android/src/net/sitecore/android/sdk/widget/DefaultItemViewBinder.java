package net.sitecore.android.sdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.sitecore.android.sdk.api.model.ScItem;

public class DefaultItemViewBinder implements ItemsBrowserFragment.ItemViewBinder {

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
