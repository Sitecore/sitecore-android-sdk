package net.sitecore.android.sdk.api.model;

import net.sitecore.android.sdk.api.ScRequest;

/**
 * Represents Scope parameter of {@link ScRequest}.
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
