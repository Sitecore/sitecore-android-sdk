package net.sitecore.android.sdk.api;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.webkit.MimeTypeMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static net.sitecore.android.sdk.api.internal.LogUtils.LOGD;
import static net.sitecore.android.sdk.api.internal.LogUtils.LOGE;

/**
 * Service responsible for sending media data.
 * <p>Manifest registration is required:
 * <pre>
 *  &lt;service android:name="net.sitecore.android.sdk.api.UploadMediaService" //>
 * </pre>
 * <p/>
 *
 * @see ScApiSession#uploadMediaIntent(String, String, String)
 * @see UploadMediaIntentBuilder
 */
public class UploadMediaService extends IntentService {

    static final String EXTRA_UPLOAD_OPTIONS = "net.sitecore.android.sdk.api.EXTRA_UPLOAD_OPTIONS";
    static final String EXTRA_STATUS_RECEIVER = "net.sitecore.android.sdk.api.EXTRA_STATUS_RECEIVER";

    static final int STATUS_OK = 0;
    static final int STATUS_ERROR = 1;

    private ResultReceiver mResultReceiver;

    public UploadMediaService() {
        super("UploadMediaService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UploadMediaRequestOptions options = intent.getParcelableExtra(EXTRA_UPLOAD_OPTIONS);
        mResultReceiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);

        String mediaFilePath = options.getMediaFilePath();
        if (mediaFilePath.startsWith("content:") && options.getFileName() == null) {
            String fileExtension = getFileExtension(Uri.parse(mediaFilePath));
            if (fileExtension != null) {
                options.setFileName(options.getItemName() + "." + fileExtension);
            }
        }

        try {
            UploadMediaHelper mediaHelper = new UploadMediaHelper(getInputStreamFromUri(options.getMediaFilePath()));
            LOGD("Sending POST " + options.getFullUrl());
            String response = mediaHelper.executeRequest(options);
            LOGD("Response: " + response);
            sendResult(response);
        } catch (Exception e) {
            LOGE(e);
            sendError(e);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getBaseContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void sendResult(String message) {
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(UploadResultReceiver.EXTRA_MESSAGE, message);
            mResultReceiver.send(STATUS_OK, bundle);
        }
    }

    private void sendError(Exception exception) {
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(UploadResultReceiver.EXTRA_ERROR, exception);
            mResultReceiver.send(STATUS_ERROR, bundle);
        }
    }

    /** Get an input stream based on file path or uri content://, http://, file:// */
    private InputStream getInputStreamFromUri(String path) throws IOException {
        if (path.startsWith("content:")) {
            Uri uri = Uri.parse(path);
            return getContentResolver().openInputStream(uri);
        }

        if (path.startsWith("http:") || path.startsWith("https:") || path.startsWith("file:")) {
            URL url = new URL(path);
            return url.openStream();
        } else {
            return new FileInputStream(path);
        }
    }

}
