package net.sitecore.android.sdk.api.model;

/**
 * Represents Sitecore Treelist field.
 */
public class TreelistField extends ScBaselistField {

    protected TreelistField(String name, String id, String rawValue) {
        super(name, id, Type.TREELIST, rawValue);
    }
}
