package net.sitecore.android.sdk.api.model;

/**
 * Represents Sitecore Checklist field.
 */
public class ChecklistField extends ScBaselistField {

    protected ChecklistField(String name, String id, String rawValue) {
        super(name, id, Type.CHECKLIST, rawValue);
    }
}
