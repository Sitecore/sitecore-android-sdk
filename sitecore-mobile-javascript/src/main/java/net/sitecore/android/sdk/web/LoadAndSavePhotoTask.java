package net.sitecore.android.sdk.web;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.graphics.BitmapFactory.Options;
import static net.sitecore.android.sdk.web.LogUtils.LOGE;

class LoadAndSavePhotoTask extends AsyncTask<String, Void, Boolean> {

    private static final int MAX_PHOTO_SIZE = 512;

    private final ScCallbackContext mCallbackContext;
    private final ContentResolver mResolver;
    private final long mContactId;

    LoadAndSavePhotoTask(ScCallbackContext callbackContext, ContentResolver resolver, long contactId) {
        mCallbackContext = callbackContext;
        mResolver = resolver;
        mContactId = contactId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String photoUri = params[0];

        try {
            //Get bitmap size to check if compression is needed
            Options tmpOptions = new Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getPathFromUri(photoUri), null, tmpOptions);

            //count scale factor
            int tmpWidth = tmpOptions.outWidth, tmpHeight = tmpOptions.outHeight;
            int scale = 1;
            while (tmpWidth > MAX_PHOTO_SIZE || tmpHeight > MAX_PHOTO_SIZE) {
                tmpWidth /= 2;
                tmpHeight /= 2;
                scale *= 2;
            }

            final Options options = new Options();
            options.inSampleSize = scale;
            Bitmap compressed = BitmapFactory.decodeStream(getPathFromUri(photoUri), null, options);

            //Convert bitmap to byte[] and save to db
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            compressed.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();
            updateDb(data);
            return true;
        } catch (IOException e) {
            LOGE("Cant create contact", e);
            //add log
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        mCallbackContext.sendSuccess();
        if (!success) {
            LOGE("Failed to set contact photo.");
        }
    }

    private void updateDb(byte[] data) {
        //Delete old photo
        mResolver.delete(ContactsContract.Data.CONTENT_URI,
                ContactCreator.SELECTION_RAW_ID_MIMETYPE,
                new String[]{
                        "" + mContactId,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                });

        //Insert new photo
        final ContentValues value = new ContentValues();
        value.put(ContactsContract.Data.RAW_CONTACT_ID, mContactId);
        value.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
        value.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        value.put(ContactsContract.CommonDataKinds.Photo.PHOTO, data);

        mResolver.insert(ContactsContract.Data.CONTENT_URI, value);
    }

    /**
     * Get an input stream based on file path or uri content://, http://, file://
     */
    private InputStream getPathFromUri(String path) throws IOException {
        if (path.startsWith("content:")) {
            Uri uri = Uri.parse(path);
            return mResolver.openInputStream(uri);
        }

        if (path.startsWith("http:") || path.startsWith("https:") || path.startsWith("file:")) {
            URL url = new URL(path);
            return url.openStream();
        } else {
            return new FileInputStream(path);
        }
    }

}