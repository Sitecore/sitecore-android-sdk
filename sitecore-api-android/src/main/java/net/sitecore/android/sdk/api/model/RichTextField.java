package net.sitecore.android.sdk.api.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents Sitecore RichText field.
 */
public class RichTextField extends ScField {
    private static Pattern sPattern = Pattern.compile("<img.*?src=(\"|')((~/media).*?)(\"|').*?(/>|</img>)");
    private String mHtmlText;

    protected RichTextField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }

    /**
     * Creates html in which all image urls are replaced with full image urls.
     *
     * @param baseUrl Sitecore instance URL.
     *
     * @return {@link String} modified html.
     */
    public String getHtmlText(String baseUrl) {
        if (mHtmlText == null) {
            Matcher matcher = sPattern.matcher(getRawValue());
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String url = matcher.group(2);
                matcher.appendReplacement(sb,
                        matcher.group(0).replace(url,
                                url.replace("~", baseUrl + "/~")));
            }
            matcher.appendTail(sb);
            mHtmlText = sb.toString();
        }
        return mHtmlText;
    }
}
