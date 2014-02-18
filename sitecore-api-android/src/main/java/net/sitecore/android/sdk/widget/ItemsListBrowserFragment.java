package net.sitecore.android.sdk.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.widget.AbsListView;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ItemsListBrowserFragment extends ItemsBrowserFragment {

    @Override
    protected AbsListView getContentView() {
        return new ListView(getActivity());
    }
}
