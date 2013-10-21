package net.sitecore.android.sdk.api.model;
/**
 * Represents Scope parameter of {@code ScRequest}.
 */
public enum RequestScope {
    SELF("s"), PARENT("p"), CHILDREN("c");

    final String value;

    private RequestScope(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
