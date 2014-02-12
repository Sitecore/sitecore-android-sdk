package net.sitecore.android.sdk.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class CompressionHelper {
    interface OnCompressionFinishedCallback {
        public void onCompressionFinished(String filePath);
    }

    private OnCompressionFinishedCallback mCallback;
    private Context mContext;

    CompressionHelper(Context context, OnCompressionFinishedCallback callback) {
        mCallback = callback;
        mContext = context;
    }

    public void compress(final String imageUrl, final String compressionQuality) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    float level = Float.parseFloat(compressionQuality);
                    int percentage = (int) ((1 - level) * 100);
                    if (percentage < 0 || percentage > 100) return imageUrl;
                    return compress(percentage, "tempImage.jpg", imageUrl);
                } catch (Exception exception) {
                    return imageUrl;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                    mCallback.onCompressionFinished(result);
            }
        }.execute((Void) null);
    }

    private String compress(int level, String tempFileName, String imageUrl) throws Exception {
        String filePath = mContext.getCacheDir() + "/" + tempFileName;
        String result;

        try {
            InputStream input = getInputStreamFromUri(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(input);
            FileOutputStream os = new FileOutputStream(filePath);

            if (bmp.compress(Bitmap.CompressFormat.JPEG, level, os)) {
                result = filePath;
            } else {
                throw new Exception();
            }

            input.close();
            os.close();
            bmp.recycle();
        } catch (OutOfMemoryError error) {
            throw new Exception();
        }
        return result;
    }

    private InputStream getInputStreamFromUri(String path) throws IOException {
        if (path.startsWith("content:")) {
            Uri uri = Uri.parse(path);
            return mContext.getContentResolver().openInputStream(uri);
        }

        if (path.startsWith("http:") || path.startsWith("https:") || path.startsWith("file:")) {
            URL url = new URL(path);
            return url.openStream();
        } else {
            return new FileInputStream(path);
        }
    }
}