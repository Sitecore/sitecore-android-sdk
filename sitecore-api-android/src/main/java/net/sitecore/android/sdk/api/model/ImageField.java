package net.sitecore.android.sdk.api.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents Sitecore Image field.
 */
public class ImageField extends ScField {
    private static Pattern SRC_PATTERN = Pattern.compile("src=(\\\\?\")(.*?)\\1"); // Regexp for searching url inside "src" attribute
    private String mImageSrcUrl;

    protected ImageField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }

    /**
     * Returns valid image url.
     *
     * @return {@code String} image url.
     */
    public String getImageSrcUrl() {
        return mImageSrcUrl;
    }

    @Override
    protected void parseRawValue(String rawValue) {
        Matcher matcher = SRC_PATTERN.matcher(rawValue);
        if (matcher.find()) {
            mImageSrcUrl = "/" + matcher.group(2);
        }
    }
}
