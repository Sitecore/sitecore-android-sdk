package net.sitecore.android.sdk.api.model;

import java.util.ArrayList;

/**
 * This class represents such list field types of Sitecore CMS:
 * <ul>
 * <li>Checklist</li>
 * <li>Multilist</li>
 * <li>Treelist</li>
 * <li>Droplink</li>
 * <li>Droptree</li>
 * </ul>
 */
public class ScBaselistField extends ScField {
    private ArrayList<String> mItemsIds;

    protected ScBaselistField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }

    @Override
    protected void parseRawValue(String rawValue) {
        String[] array = rawValue.split("\\|");
        mItemsIds = new ArrayList<String>(array.length);
        for (String s : array) {
            mItemsIds.add(s);
        }
    }

    /**
     * Returns {@link ArrayList} of item ids.
     *
     * @return {@link ArrayList} of ids.
     */
    public ArrayList<String> getItemsIds() {
        return mItemsIds;
    }

}
