package net.sitecore.android.sdk.web;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Fragment that displays a WebView.
 * <p/>The WebView is paused or resumed when the Fragment is paused or resumed.
 * <p>For versions prior Android 3.0 use {@link ScWebViewSupportFragment}.
 *
 * @see ScWebViewSupportFragment
 */
@TargetApi(14)
public class ScWebViewFragment extends Fragment implements ActivityContext {

    private ScWebViewFragmentHelper mFragmentHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentHelper = new ScWebViewFragmentHelper(getActivity(), this);
    }

    /** Called to instantiate the view. Creates and returns the WebView. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentHelper.onCreateView(inflater, container, savedInstanceState);
        return mFragmentHelper.getWebView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mFragmentHelper.onViewStateRestored(savedInstanceState);
    }

    /** Called when the fragment is no longer resumed. Pauses the WebView. */
    @Override
    public void onPause() {
        super.onPause();
        mFragmentHelper.onPause();
    }

    /** Called when the fragment is visible to the user and actively running. Resumes the WebView. */
    @Override
    public void onResume() {
        mFragmentHelper.onResume();
        super.onResume();
    }

    public WebView getWebView() {
        return mFragmentHelper.getWebView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragmentHelper.onActivityResult(requestCode, resultCode, data);
    }
}