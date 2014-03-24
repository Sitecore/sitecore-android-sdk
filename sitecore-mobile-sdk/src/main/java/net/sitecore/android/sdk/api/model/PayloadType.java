package net.sitecore.android.sdk.api.model;

/**
 * Represents Payload parameter of {@code ScRequest}.
 * <p>This parameter changes the set of item fields in the Item Web API response.
 * <ul>
 * <li>{@code min} - no fields are returned in the service response.</li>
 * <li>{@code content} - only content fields are returned in the service response.</li>
 * <li>{@code full} - all the item fields, including content and standard fields, are returned in the service response.</li>
 * </ul>
 */
public enum PayloadType {
    MIN("min"), CONTENT("content"), FULL("full");

    final String value;

    private PayloadType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}