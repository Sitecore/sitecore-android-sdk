package net.sitecore.android.sdk.web;

import org.json.JSONException;
import org.json.JSONObject;

/** Class represents wrapper for Java errors that are transmitted to Javascript code. */
class ScJavascriptError {

    private static final int UNDEFINED = -1;

    private String mDomain = "";
    private int mCode = UNDEFINED;
    private String mDescription = "";
    private Throwable mException;

    /**
     * Class constructor that creates {@code ScJavascriptError} based on {@code String} description.
     *
     * @param description {@code String} error description.
     */
    public ScJavascriptError(String description) {
        mDescription = description;
    }

    /**
     * Class constructor that creates {@code ScJavascriptError} based on {@code String} description and {@code String} domain.
     *
     * @param description {@code String} error description.
     * @param domain      {@code String} domain name.
     */
    public ScJavascriptError(String description, String domain) {
        mDescription = description;
        mDomain = domain;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Throwable getException() {
        return mException;
    }

    public void setException(Throwable exception) {
        mException = exception;
    }

    /**
     * Returns {@code String} representation of the error that will be used to send to {@code Javascript} code.
     *
     * @return {@code String} representation.
     */
    @Override
    public String toString() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("domain", mDomain);
            obj.put("localizedDescription", mDescription);
            if (mCode != UNDEFINED) {
                obj.put("code", mCode);
            }
            if (mException != null && mException.getMessage() != null) {
                obj.put("error", mException.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
