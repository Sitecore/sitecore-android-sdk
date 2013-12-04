package net.sitecore.android.sdk.api;

import java.util.HashMap;
import java.util.Set;

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

        boolean firstTime = true;
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (firstTime) {
                firstTime = false;
                builder.append("?");
            } else {
                builder.append("&");
            }
            String value = params.get(key);
            builder.append(key).append("=").append(value);
        }
        return builder.toString();
    }

    /**
     * Helper class for building {@link MediaParameters}.
     */
    public static class Builder {
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

        public Builder() {
            params = new HashMap<String, String>();
        }

        /**
         * The width of the image. Be sure to set {@link #allowStretch} if the width will be larger than its
         * original size.
         *
         * @param width image width.
         *
         * @return this builder.
         */
        public Builder width(int width) {
            params.put(WIDTH_KEY, String.valueOf(width));
            return this;
        }

        /**
         * The height of the image. Be sure to set {@link #allowStretch} if the height will be larger than its
         * original size.
         *
         * @param height image height.
         *
         * @return this builder.
         */
        public Builder height(int height) {
            params.put(HEIGHT_KEY, String.valueOf(height));
            return this;
        }

        /**
         * Maximum width of the image to display.  Scale the image down to this size if necessary.
         *
         * @param maxWidth maximum image width.
         *
         * @return this builder.
         */
        public Builder maxWidth(int maxWidth) {
            params.put(MAXIMUM_WIDTH_KEY, String.valueOf(maxWidth));
            return this;
        }

        /**
         * Maximum height of the image to display.  Scale the image down to this size if necessary.
         *
         * @param maxHeight maximum image height.
         *
         * @return this builder.
         */
        public Builder maxHeight(int maxHeight) {
            params.put(MAXIMUM_HEIGHT_KEY, String.valueOf(maxHeight));
            return this;
        }

        /**
         * Retrieve the image from a specific language version of the item.
         *
         * @param language language.
         *
         * @return this builder.
         */
        public Builder language(String language) {
            params.put(LANGUAGE_KEY, language);
            return this;
        }

        /**
         * Retrieve the image from a specific version of the item.
         *
         * @param version version number.
         *
         * @return this builder.
         */
        public Builder version(int version) {
            params.put(VERSION_KEY, String.valueOf(version));
            return this;
        }

        /**
         * The name of the Sitecore to pull the image from.
         *
         * @param database database name.
         *
         * @return this builder.
         */
        public Builder database(String database) {
            params.put(DATABASE_NAME_KEY, database);
            return this;
        }

        /**
         * Background color for the border added when an image is stretched beyond its original size
         * and {@link #allowStretch}=false.
         *
         * @param color color.
         *
         * @return this builder.
         */
        public Builder backgroundColor(String color) {
            params.put(BACKGROUND_COLOR_KEY, color);
            return this;
        }

        /**
         * Allow stretching the image beyond its original size.
         *
         * @param allowStretch whether to allow stretch.
         *
         * @return this builder.
         */
        public Builder allowStretch(boolean allowStretch) {
            params.put(ALLOW_STRETCH_KEY, String.valueOf(allowStretch ? 1 : 0));
            return this;
        }

        /**
         * Scale factor for the image to display.  Be sure to set {@link #allowStretch} if the image will be scaled to
         * larger than its original size.
         * Any positive floating point number using a dot such as 1.5, which corresponds to 150%.
         *
         * @param scale scale.
         *
         * @return this builder.
         */
        public Builder scale(float scale) {
            params.put(SCALE_KEY, String.valueOf(scale));
            return this;
        }

        /**
         * Display a thumbnail of the requested file, useful for images as well as other media types, such as PDF,
         * flash, and so on.
         *
         * @param thumbnail whether to show thumbnail.
         *
         * @return this builder.
         */
        public Builder thumbnail(boolean thumbnail) {
            params.put(THUMBNAIL_KEY, String.valueOf(thumbnail ? 1 : 0));
            return this;
        }

        /**
         * Disable the media cache for this request. If true, the image will always be retrieved from the database,
         * bypassing the media cache.
         *
         * @param disable whether to use media cache for request.
         *
         * @return this builder.
         */
        public Builder disableMediaCaching(boolean disable) {
            params.put(DISABLE_MEDIA_CACHING_KEY, String.valueOf(disable ? 1 : 0));
            return this;
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
