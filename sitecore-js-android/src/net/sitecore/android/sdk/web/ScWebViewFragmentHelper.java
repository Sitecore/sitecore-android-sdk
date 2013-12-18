package net.sitecore.android.sdk.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import static net.sitecore.android.sdk.web.LogUtils.LOGD;

/** Class that initialize necessary components({@link ScPluginManager} and {@link WebView}) and controls their state. */
class ScWebViewFragmentHelper {

    private final Context mContext;

    private WebView mWebView;
    private ScPluginManager mScPluginManager;

    /**
     * Initialize {@link ScPluginManager} and loads plugins.
     *
     * @param context         current {@link Context context}.
     * @param activityContext context for handling {@link android.app.Activity Activity} workflow.
     */
    public ScWebViewFragmentHelper(Context context, ActivityContext activityContext) {
        mContext = context;

        mScPluginManager = new ScPluginManager(mContext);
        mScPluginManager.addAllPlugins();
        mScPluginManager.setActivityContext(activityContext);
    }

    /**
     * Initialize underlying {@link WebView}.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    public void onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebView = new WebView(mContext);

        initWebViewSettings(mWebView);
        mWebView.setWebViewClient(new ScWebViewClient(mScPluginManager));
        mWebView.setWebChromeClient(new LoggingChromeClient());

        mScPluginManager.setWebView(mWebView);
    }

    public void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
        mScPluginManager.onSaveState(outState);
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
            mScPluginManager.onRestoreState(savedInstanceState);
        }
    }

    public void onPause() {
        mScPluginManager.onPause();
    }

    public void onResume() {
        mScPluginManager.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mScPluginManager.onActivityResult(requestCode, resultCode, data);
    }

    public WebView getWebView() {
        return mWebView;
    }

    private void initWebViewSettings(WebView webView) {
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
    }


    private static class LoggingChromeClient extends WebChromeClient {

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            LOGD("%s (%s) : %s", consoleMessage.sourceId(), consoleMessage.lineNumber(), consoleMessage.message());
            return true;
        }
    }
}
