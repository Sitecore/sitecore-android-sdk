package net.sitecore.android.sdk.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

/**
 * Plugin for loading image from the device.
 * <p>Example :
 * <pre>
 *      function getImage(source) {
 *          var options = {};
 *          options.sourceType = source;
 *
 *          scmobile.camera.getPicture(onSuccess, onError, options);
 *      }
 *
 *      function getImageWithoutOptions() {
 *          scmobile.camera.getPicture(onSuccess, onError);
 *      }
 *
 *      function getImageWithoutSourceType() {
 *          var options = {};
 *          scmobile.camera.getPicture(onSuccess, onError, options);
 *      }
 * </pre>
 */
public final class CameraPlugin extends ScPlugin {

    private static final int SOURCE_TYPE_GALLERY = 0;
    private static final int SOURCE_TYPE_CAMERA = 1;

    private static final SimpleDateFormat IMAGE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private ScCallbackContext mCallbackContext;

    private Uri mImageUri;

    @Override
    public String getPluginName() {
        return "camera";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_camera);
    }

    /**
     * Loads image from camera, album, library.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if execution fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        int sourceType = params.optInt("sourceType", SOURCE_TYPE_GALLERY);

        if (method.equals("getPicture")) {
            if (sourceType == SOURCE_TYPE_CAMERA) {
                showCamera();
            } else {
                showGallery();
            }
        }
    }

    private void showCamera() {
        final File photo = getOutputMediaFile();
        mImageUri = Uri.fromFile(photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        mPluginManager.getActivityContext(this).startActivityForResult(intent, SOURCE_TYPE_CAMERA);
    }

    private File getOutputMediaFile() {
        String imageName = "IMG_" + IMAGE_DATE_FORMAT.format(new Date()) + ".png";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ScMobile");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return new File(IoUtils.getTempDirectoryPath(mContext), imageName);
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + imageName);
    }

    private void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (VersionUtils.isKitKatOrLater()) {
            intent.setAction(Intent.ACTION_PICK);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        mPluginManager.getActivityContext(this).startActivityForResult(intent, SOURCE_TYPE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SOURCE_TYPE_CAMERA) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mImageUri));
                mContext.getContentResolver().notifyChange(mImageUri, null);
                mCallbackContext.sendSuccess(mImageUri.toString());
            } else if (requestCode == SOURCE_TYPE_GALLERY) {
                mCallbackContext.sendSuccess(data.getDataString());
            }
        } else {
            if (requestCode == SOURCE_TYPE_CAMERA) {
                mCallbackContext.sendError("Camera image request cancelled.");
            } else {
                mCallbackContext.sendError("Gallery image request cancelled.");
            }
        }
    }

}
