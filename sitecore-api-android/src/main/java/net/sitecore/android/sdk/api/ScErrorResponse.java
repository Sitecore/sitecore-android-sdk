package net.sitecore.android.sdk.api;

class ScErrorResponse extends ScResponse {
    private String mErrorMessage;

    public ScErrorResponse(int statusCode, String errorMessage) {
        super(statusCode);
        mErrorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return false;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
