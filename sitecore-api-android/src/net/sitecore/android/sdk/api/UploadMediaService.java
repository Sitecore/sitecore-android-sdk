package net.sitecore.android.sdk.api;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import net.sitecore.android.sdk.api.model.ItemsResponse;
import net.sitecore.android.sdk.api.model.ScError;
import net.sitecore.android.sdk.api.model.ScField;

import static com.android.volley.Response.ErrorListener;
import static net.sitecore.android.sdk.api.LogUtils.LOGD;

/**
 * Service responsible for sending media data.
 * <p>Manifest registration is required:
 * <pre>
 *  &lt;service android:name="net.sitecore.android.sdk.api.UploadMediaService" //>
 * </pre>
 *
 * @see ScApiSession#uploadMedia(String, String, String)
 * @see UploadMediaRequestOptions
 */
public class UploadMediaService extends IntentService {
    static final String EXTRA_UPLOAD_OPTIONS = "net.sitecore.android.sdk.api.EXTRA_UPLOAD_OPTIONS";
    static final String EXTRA_STATUS_RECEIVER = "net.sitecore.android.sdk.api.EXTRA_STATUS_RECEIVER";

    static final int STATUS_OK = 0;
    static final int STATUS_ERROR = 1;

    public UploadMediaService(String name) {
        super(name);
    }

    /**
     * Starts to upload media content.
     *
     * @param context         application context.
     * @param options         {@code UploadMediaRequestOptions} that contains request options.
     * @param successListener success listener for request
     * @param errorListener   error listener for request
     */
    public static void startUpload(Context context, UploadMediaRequestOptions options,
            final Response.Listener<ItemsResponse> successListener,
            final ErrorListener errorListener) {
        context.startService(newUploadIntent(context, options, successListener, errorListener));
    }

    /**
     * Creates {@code Intent} to upload media content from specified {@code UploadMediaRequestOptions}.
     *
     * @param context         application context.
     * @param options         {@code UploadMediaRequestOptions} that contains request options.
     * @param successListener success listener for request
     * @param errorListener   error listener for request
     *
     * @return result {@code Intent}.
     */
    public static Intent newUploadIntent(Context context, UploadMediaRequestOptions options,
            final Response.Listener<ItemsResponse> successListener,
            final ErrorListener errorListener) {
        // Converts String response to ItemsResponse
        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type fieldsType = new TypeToken<List<ScField>>() {
                }.getType();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(fieldsType, new ScFieldDeserializer())
                        .create();

                ScResponse result = gson.fromJson(response, ScResponse.class);
                if (result.isSuccess()) {
                    successListener.onResponse(gson.fromJson(response, ItemsResponse.class));
                } else {
                    errorListener.onErrorResponse(new ScError(result.getStatusCode(), result.getErrorMessage()));
                }
            }
        };

        final UpdateResultReceiver resultReceiver = new UpdateResultReceiver(null, listener, errorListener);
        final Intent intent = new Intent(context, UploadMediaService.class);
        intent.putExtra(EXTRA_UPLOAD_OPTIONS, options);
        intent.putExtra(EXTRA_STATUS_RECEIVER, resultReceiver);

        return intent;
    }

    private ResultReceiver mResultReceiver;

    public UploadMediaService() {
        super("UploadMediaService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UploadMediaRequestOptions options = intent.getParcelableExtra(EXTRA_UPLOAD_OPTIONS);
        mResultReceiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);

        try {
            UploadMediaHelper mediaHelper = new UploadMediaHelper(getInputStreamFromUri(options.getMediaFilePath()));
            LOGD("Sending POST " + options.getFullUrl());
            String response = mediaHelper.executeRequest(options);
            LOGD("Response: " + response);
            sendResult(STATUS_OK, response);
        } catch (IOException e) {
            sendResult(STATUS_ERROR, e.getMessage());
        }
    }

    private void sendResult(int code, String message) {
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, message);
            mResultReceiver.send(code, bundle);
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

    static class UpdateResultReceiver extends ResultReceiver {

        private Response.Listener<String> mSuccessListener;
        private ErrorListener mErrorListener;

        public UpdateResultReceiver(Handler handler,
                Response.Listener<String> successListener,
                ErrorListener errorListener) {
            super(handler);
            mSuccessListener = successListener;
            mErrorListener = errorListener;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            final String message = resultData.getString(Intent.EXTRA_TEXT);

            switch (resultCode) {
                case STATUS_OK:
                    mSuccessListener.onResponse(message);
                    break;

                case STATUS_ERROR:
                    mErrorListener.onErrorResponse(new VolleyError(message));
                    break;
            }
        }
    }

}
