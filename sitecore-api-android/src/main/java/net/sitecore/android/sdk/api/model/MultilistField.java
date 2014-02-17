package net.sitecore.android.sdk.api.model;

/**
 * Represents Sitecore Multilist field.
 */
public class MultilistField extends ScBaselistField {

    protected MultilistField(String name, String id, String rawValue) {
        super(name, id, Type.MULTILIST, rawValue);
    }
}
