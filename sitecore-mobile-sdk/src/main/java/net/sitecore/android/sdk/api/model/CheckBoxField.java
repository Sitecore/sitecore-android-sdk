package net.sitecore.android.sdk.api.model;

/**
 * Represents Sitecore CheckBox field.
 */
public class CheckBoxField extends ScField {
    private boolean mChecked;

    protected CheckBoxField(String name, String id, String rawValue) {
        super(name, id, Type.CHECKBOX, rawValue);
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    protected void parseRawValue(String rawValue) {
        mChecked = rawValue.equals("1");
    }
}
