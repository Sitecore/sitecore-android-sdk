package net.sitecore.android.sdk.web;

import android.content.Context;

/** The class is designed for the preparation of the final {@code Javascript} code that will be injected into the page. */
class ScJsWriter {

    private final StringBuilder mBuilder = new StringBuilder();

    public ScJsWriter(Context context) {
        Context context1 = context;
        mBuilder.append(IoUtils.readRawTextFile(context1, R.raw.common));
    }

    /**
     * Adds plugin's {@code Javascript} code.
     *
     * @param plugin {@code ScPlugin} plugin.
     */
    public void addPlugin(ScPlugin plugin) {
        mBuilder.append(plugin.getPluginJsCode());
    }

    /**
     * Returns final {@code Javascript} code to inject to page.
     *
     * @return {@code String} code.
     */
    @Override
    public String toString() {
        return mBuilder.toString();
    }
}
