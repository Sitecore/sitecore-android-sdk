package net.sitecore.android.sdk.api;

import java.util.HashMap;
import java.util.Iterator;

public class MediaParameters {

    private final HashMap<String, String> params;

    private MediaParameters(HashMap<String, String> params) {
        this.params = new HashMap<String, String>(params);
    }

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

        public void width(int width) {
            params.put(WIDTH_KEY, String.valueOf(width));
        }

        public void height(int height) {
            params.put(HEIGHT_KEY, String.valueOf(height));
        }

        public void maxWidth(int maxWidth) {
            params.put(MAXIMUM_WIDTH_KEY, String.valueOf(maxWidth));
        }

        public void maxHeight(int maxHeight) {
            params.put(MAXIMUM_HEIGHT_KEY, String.valueOf(maxHeight));
        }

        public void language(String language) {
            params.put(LANGUAGE_KEY, language);
        }

        public void version(int version) {
            params.put(VERSION_KEY, String.valueOf(version));
        }

        public void database(String database) {
            params.put(DATABASE_NAME_KEY, database);
        }

        public void backgroundColor(String color) {
            params.put(BACKGROUND_COLOR_KEY, color);
        }

        public void allowStretch(boolean allowStretch) {
            params.put(ALLOW_STRETCH_KEY, String.valueOf(allowStretch ? 1 : 0));
        }

        public void scale(float scale) {
            params.put(SCALE_KEY, String.valueOf(scale));
        }

        public void thumbnail(boolean thumbnail) {
            params.put(THUMBNAIL_KEY, String.valueOf(thumbnail ? 1 : 0));
        }

        public void disableMediaCaching(boolean disable) {
            params.put(DISABLE_MEDIA_CACHING_KEY, String.valueOf(disable ? 1 : 0));
        }

        public MediaParameters build() {
            return new MediaParameters(params);
        }
    }
}
