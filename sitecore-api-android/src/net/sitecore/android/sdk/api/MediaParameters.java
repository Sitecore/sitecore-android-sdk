package net.sitecore.android.sdk.api;

import java.util.HashMap;
import java.util.Iterator;

import net.sitecore.android.sdk.api.model.ScItem;

/**
 * Class which represents Sitecore media attributes.
 *
 * @see ScItem#getMediaDownloadUrl(MediaParameters)
 */
public class MediaParameters {

    private final HashMap<String, String> params;

    private MediaParameters(HashMap<String, String> params) {
        this.params = new HashMap<String, String>(params);
    }

    /**
     * Converts previously set parameters to url form.
     *
     * @return {@code String} url form.
     */
    public String buildUrlFromParams() {
        StringBuilder builder = new StringBuilder();

        if (this.params.size() != 0) {
            builder.append("?");
        }

        Iterator<String> keyIterator = params.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = params.get(key);

            builder.append(key).append("=").append(value);
            if (keyIterator.hasNext()) builder.append("&");
        }
        return builder.toString();
    }

    /**
     * Helper class for building {@link MediaParameters}.
     */
    static class MediaParametersBuilder {
        private static final String WIDTH_KEY = "w";
        private static final String HEIGHT_KEY = "h";
        private static final String MAXIMUM_WIDTH_KEY = "mw";
        private static final String MAXIMUM_HEIGHT_KEY = "mh";
        private static final String LANGUAGE_KEY = "la";
        private static final String VERSION_KEY = "vs";
        private static final String DATABASE_NAME_KEY = "db";
        private static final String BACKGROUND_COLOR_KEY = "bc";
        private static final String ALLOW_STRETCH_KEY = "as";
        private static final String SCALE_KEY = "sc";
        private static final String THUMBNAIL_KEY = "thn";
        private static final String DISABLE_MEDIA_CACHING_KEY = "dmc";

        private HashMap<String, String> params;

        public MediaParametersBuilder() {
            params = new HashMap<String, String>();
        }

        /**
         * The width of the image. Be sure to set {@link #allowStretch} if the width will be larger than its
         * original size.
         *
         * @param width image width.
         */
        public void width(int width) {
            params.put(WIDTH_KEY, String.valueOf(width));
        }

        /**
         * The height of the image. Be sure to set {@link #allowStretch} if the height will be larger than its
         * original size.
         *
         * @param height image height.
         */
        public void height(int height) {
            params.put(HEIGHT_KEY, String.valueOf(height));
        }

        /**
         * Maximum width of the image to display.  Scale the image down to this size if necessary.
         *
         * @param maxWidth maximum image width.
         */
        public void maxWidth(int maxWidth) {
            params.put(MAXIMUM_WIDTH_KEY, String.valueOf(maxWidth));
        }

        /**
         * Maximum height of the image to display.  Scale the image down to this size if necessary.
         *
         * @param maxHeight maximum image height.
         */
        public void maxHeight(int maxHeight) {
            params.put(MAXIMUM_HEIGHT_KEY, String.valueOf(maxHeight));
        }

        /**
         * Retrieve the image from a specific language version of the item.
         *
         * @param language language.
         */
        public void language(String language) {
            params.put(LANGUAGE_KEY, language);
        }

        /**
         * Retrieve the image from a specific version of the item.
         *
         * @param version version number.
         */
        public void version(int version) {
            params.put(VERSION_KEY, String.valueOf(version));
        }

        /**
         * The name of the Sitecore to pull the image from.
         *
         * @param database database name.
         */
        public void database(String database) {
            params.put(DATABASE_NAME_KEY, database);
        }

        /**
         * Background color for the border added when an image is stretched beyond its original size
         * and {@link #allowStretch}=false.
         *
         * @param color color.
         */
        public void backgroundColor(String color) {
            params.put(BACKGROUND_COLOR_KEY, color);
        }

        /**
         * Allow stretching the image beyond its original size.
         *
         * @param allowStretch whether to allow stretch.
         */
        public void allowStretch(boolean allowStretch) {
            params.put(ALLOW_STRETCH_KEY, String.valueOf(allowStretch ? 1 : 0));
        }

        /**
         * Scale factor for the image to display.  Be sure to set {@link #allowStretch} if the image will be scaled to
         * larger than its original size.
         * Any positive floating point number using a dot such as 1.5, which corresponds to 150%.
         *
         * @param scale scale.
         */
        public void scale(float scale) {
            params.put(SCALE_KEY, String.valueOf(scale));
        }

        /**
         * Display a thumbnail of the requested file, useful for images as well as other media types, such as PDF,
         * flash, and so on.
         *
         * @param thumbnail whether to show thumbnail.
         */
        public void thumbnail(boolean thumbnail) {
            params.put(THUMBNAIL_KEY, String.valueOf(thumbnail ? 1 : 0));
        }

        /**
         * Disable the media cache for this request. If true, the image will always be retrieved from the database,
         * bypassing the media cache.
         *
         * @param disable whether to use media cache for request.
         */
        public void disableMediaCaching(boolean disable) {
            params.put(DISABLE_MEDIA_CACHING_KEY, String.valueOf(disable ? 1 : 0));
        }

        /**
         * Builds {@link net.sitecore.android.sdk.api.MediaParameters}.
         *
         * @return {@code MediaParameters} parameters.
         */
        public MediaParameters build() {
            return new MediaParameters(params);
        }
    }
}
