package net.sitecore.android.sdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sitecore.android.sdk.api.model.ScItem;

/**
 * Defines how to map {@link net.sitecore.android.sdk.api.model.ScItem} to list item view.
 */
public interface ItemViewBinder {

    /**
     * Bind an existing view to the item.
     */
    public void bindView(Context context, View v, ScItem item);

    /**
     * Makes a new view to hold the data of the item.
     */
    public View newView(Context context, ViewGroup parent, LayoutInflater inflater, ScItem item);

}
