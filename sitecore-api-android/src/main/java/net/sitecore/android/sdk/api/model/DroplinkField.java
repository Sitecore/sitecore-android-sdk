package net.sitecore.android.sdk.api.model;

/**
 * Represents Sitecore Droplink field.
 */
public class DroplinkField extends ScBaselistField {

    protected DroplinkField(String name, String id, String rawValue) {
        super(name, id, Type.DROPLINK, rawValue);
    }
}
