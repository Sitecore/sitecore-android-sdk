package net.sitecore.android.sdk.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.sitecore.android.sdk.api.internal.LogUtils.LOGD;
import static net.sitecore.android.sdk.api.internal.LogUtils.LOGE;

/**
 * <p>This class is designed to process media upload request.</p>
 * Under the hood it uses {@link HttpURLConnection}.
 */
class UploadMediaHelper {
    private static final String BOUNDARY = "----WebKitFormBoundaryT5WjphCNABLVDqLd";
    private static final int BUFFER_SIZE = 1024; // bytes

    private InputStream mInputStream;

    /**
     * <p>Creates {@link UploadMediaHelper} based on {@link InputStream} </p>
     *
     * @param inputStream Data source for request
     */
    UploadMediaHelper(InputStream inputStream) {
        mInputStream = inputStream;
    }

    /**
     * <p>Establishes connection and sends data.</p>
     *
     * @param options Request options for this connection.
     *
     * @return {@link String} response from the server.
     * @throws IOException if error happened.
     */
    String executeRequest(UploadMediaRequestOptions options) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader responseStreamReader = null;
        DataOutputStream request = null;

        String filename = (options.getFileName() == null) ? options.getMediaFilePath() : options.getFileName();
        try {
            String crlf = "\r\n";
            String twoHyphens = "--";

            URL url = new URL(options.getFullUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(BUFFER_SIZE);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Scitemwebapi-Encrypted", "1");
            connection.setRequestProperty("X-Scitemwebapi-Username", options.mAuthOptions.mEncodedName);
            connection.setRequestProperty("X-Scitemwebapi-Password", options.mAuthOptions.mEncodedPassword);
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + BOUNDARY + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + filename + "\";filename=\"" + filename + "\"" + crlf);
            request.writeBytes(crlf);

            copyStream(mInputStream, request);

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + BOUNDARY + twoHyphens + crlf);

            request.flush();

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } finally {
            try {
                if (responseStreamReader != null) responseStreamReader.close();
                if (request != null) request.flush();
            } catch (IOException exception) {
                responseStreamReader = null;
                request = null;
                LOGE(exception);
            }
        }
    }

    /**
     * <p>Copies the data from the {@link InputStream} into a {@link OutputStream}</p>
     *
     * @param input  data source stream.
     * @param output target stream.
     *
     * @throws IOException if error happened.
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        long copied = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            copied += bytesRead;
        }

        LOGD("" + copied + " bytes of media uploaded.");
    }

}
