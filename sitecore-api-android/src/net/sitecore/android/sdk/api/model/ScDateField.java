package net.sitecore.android.sdk.api.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
/**
 * This class represents such field types of Sitecore CMS:
 * <ul>
 *     <li>Date</li>
 *     <li>Datetime</li>
 * </ul>
 *
 * The only difference between them is accuracy.
 */
public class ScDateField extends ScField {
    public static SimpleDateFormat DATE_FORMAT;
    private long mDateInMillis;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    protected ScDateField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }

    @Override
    protected void parseRawValue(String rawValue) {
        try {
            mDateInMillis = DATE_FORMAT.parse(rawValue).getTime();
        } catch (ParseException e) {}
    }

    /**
     * Returns the Date as a millisecond value.
     * @return milliseconds.
     */
    public long getDate() {
        return mDateInMillis;
    }
}
