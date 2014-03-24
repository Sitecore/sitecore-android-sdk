package net.sitecore.android.sdk.web;

import android.content.Intent;

/** Defines common methods for plugins that rely on {@code Activity} workflow. */
interface ActivityContext {

    void startActivityForResult(Intent intent, int requestCode);

}
