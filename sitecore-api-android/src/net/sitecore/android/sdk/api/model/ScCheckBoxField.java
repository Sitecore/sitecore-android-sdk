package net.sitecore.android.sdk.api.model;

/**
 * This class represents Checkbox field type of Sitecore CMS:
 */
public class ScCheckBoxField extends ScField {
    private boolean mChecked;

    protected ScCheckBoxField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    protected void parseRawValue(String rawValue) {
        mChecked = rawValue.equals("1");
    }
}
