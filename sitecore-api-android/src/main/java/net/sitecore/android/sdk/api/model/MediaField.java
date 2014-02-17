package net.sitecore.android.sdk.api.model;

/**
 * This class represents such field types of Sitecore CMS:
 * <ul>
 * <li>Audio</li>
 * <li>Doc</li>
 * <li>Document</li>
 * <li>Docx</li>
 * <li>File</li>
 * <li>Flash</li>
 * <li>Jpeg</li>
 * <li>Movie</li>
 * <li>Mp3</li>
 * <li>Pdf</li>
 * <li>Zip</li>
 * </ul>
 */
public class MediaField extends ScField {
    public MediaField(String name, String id, Type type, String rawValue) {
        super(name, id, type, rawValue);
    }
}
