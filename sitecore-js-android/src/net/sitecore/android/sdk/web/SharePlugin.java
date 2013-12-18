package net.sitecore.android.sdk.web;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;

import java.util.ArrayList;

import static net.sitecore.android.sdk.web.LogUtils.LOGE;

/**
 * Plugin for sharing information to social services.
 * <p>Example :
 * <ul>
 * <li>Social :</li>
 * <pre>
 *      var social = new scmobile.share.Social();
 *      social.text = 'Text to send';
 *      social.imageUrls = [imageUrl];
 *      social.send(onSuccess, onError);
 * </pre>
 * <li>Email:</li>
 * <pre>
 *      var email = new scmobile.share.Email();
 *      email.toRecipients = ['some.mail@email.com'];
 *      email.ccRecipients = ['cc_recipients_email'];
 *      email.bccRecipients = 'bcc_recipients_email';
 *      email.subject = 'Send Email With Subject And Body';
 *      email.messageBody = '<b>Test</b> Body in JS';
 *      email.isHtml = false;
 *      email.send(onSuccess);
 * </pre>
 * </ul>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public final class SharePlugin extends ScPlugin {

    private static final String CAMERA_ATTACHMENT_URI = "file://";
    private static final String GALLERY_ATTACHMENT_URI = "content://";

    private ScCallbackContext mCallbackContext;

    @Override
    public String getPluginName() {
        return "share";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_share);
    }

    /**
     * Shares information.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) {
        mCallbackContext = callbackContext;
        if (method.equals("sendEmail")) {
            sendEmail(params);
        } else if (method.equals("sendSocial")) {
            sendSocial(params);
        }
    }

    private void sendSocial(ScParams params) {
        final Intent intent = new Intent();
        String text = params.getString("text");

        String[] images = params.getStringArray("imageUrls");

        ArrayList<Uri> internalUris = new ArrayList<Uri>(images.length);
        for (String image : images) {
            if (image.startsWith(CAMERA_ATTACHMENT_URI) ||
                    image.startsWith(GALLERY_ATTACHMENT_URI)) {
                // image from device
                internalUris.add(Uri.parse(image));
            } else {
                // image from web
                text += " " + image;
            }
        }

        if (internalUris.size() > 1) {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, internalUris);
        } else if (internalUris.size() == 1) {
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, internalUris.get(0));
        } else {
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
        }

        intent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            final ActivityContext context = mPluginManager.getActivityContext(this);
            context.startActivityForResult(Intent.createChooser(intent, "Send"), 0);
        } catch (ActivityNotFoundException e) {
            LOGE("Installed social applications not found.");
        }
    }

    private void sendEmail(ScParams params) {
        final Intent intent = new Intent(Intent.ACTION_SENDTO);

        String uri = buildMailtoUri(params);
        intent.setData(Uri.parse(uri));

        boolean isHtml = params.optBoolean("isHtml");
        if (isHtml) {
            final String body = params.getString("body");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
        }

        try {
            final ActivityContext context = mPluginManager.getActivityContext(this);
            context.startActivityForResult(Intent.createChooser(intent, "Send email"), 0);
        } catch (ActivityNotFoundException e) {
            LOGE("Installed email client not found.");
        }
    }

    private String buildMailtoUri(ScParams params) {
        final String[] to = params.getStringArray("to");
        final String[] cc = params.getStringArray("cc");
        final String[] bcc = params.getStringArray("bcc");
        final String subject = params.getString("subject");
        final String body = params.getString("body");
        final boolean isHtml = params.optBoolean("isHtml");

        final StringBuilder uriBuilder = new StringBuilder("mailto:");
        if (to.length > 0) {
            uriBuilder.append(toCsv(to));
        }

        final Uri.Builder queryBuilder = new Uri.Builder();
        queryBuilder.appendQueryParameter("cc", toCsv(cc));
        queryBuilder.appendQueryParameter("bcc", toCsv(bcc));
        queryBuilder.appendQueryParameter("subject", subject);
        if (!isHtml) queryBuilder.appendQueryParameter("body", body);

        final String query = queryBuilder.build().getQuery();
        if (!TextUtils.isEmpty(query)) {
            uriBuilder.append("?").append(query);
        }

        return uriBuilder.toString();
    }

    /** ['a', 'b'] --> "a,b" */
    private String toCsv(String[] array) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (String token : array) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(",");
            }
            sb.append(token);
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: callback must be restored on activity recreate
        mCallbackContext.sendSuccess("" + resultCode);
    }
}
