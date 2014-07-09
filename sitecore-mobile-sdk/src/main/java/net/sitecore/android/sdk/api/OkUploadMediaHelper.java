package net.sitecore.android.sdk.api;

import java.io.IOException;
import java.io.InputStream;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import okio.BufferedSink;
import okio.Okio;

/**
 * <p>This class is designed to process media upload request.</p>
 */
class OkUploadMediaHelper {

    private final InputStream mInputStream;

    /**
     * <p>Creates {@link net.sitecore.android.sdk.api.OkUploadMediaHelper} based on {@link java.io.InputStream} </p>
     *
     * @param inputStream Data source for request
     */
    OkUploadMediaHelper(InputStream inputStream) {
        mInputStream = inputStream;
    }

    /**
     * <p>Establishes connection and sends data.</p>
     *
     * @param options Request options for this connection.
     *
     * @return {@link String} response from the server.
     * @throws java.io.IOException if error happened.
     */
    String executeRequest(UploadMediaRequestOptions options) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        final String filename = (options.getFileName() == null)
                ? options.getMediaFilePath()
                : options.getFileName();

        final RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + filename + "\";filename=\"" + filename + "\""),
                        new InputStreamRequestBody(mInputStream))
                .build();

        final Request request = new Request.Builder()
                .header("X-Scitemwebapi-Encrypted", "1")
                .header("X-Scitemwebapi-Username", options.mAuthOptions.mEncodedName)
                .header("X-Scitemwebapi-Password", options.mAuthOptions.mEncodedPassword)
                .url(options.getFullUrl())
                .post(requestBody)
                .build();

        final Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    private static class InputStreamRequestBody extends RequestBody {

        private final InputStream mInputStream;

        private InputStreamRequestBody(InputStream inputStream) {
            mInputStream = inputStream;
        }

        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.writeAll(Okio.source(mInputStream));
        }
    }
}
