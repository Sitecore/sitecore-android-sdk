package net.sitecore.android.sdk.api;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

/**
 * Use next imports for clean code:
 * <pre>
 * import static net.sitecore.android.sdk.api.LogUtils.LOGD;
 * import static net.sitecore.android.sdk.api.LogUtils.LOGE;
 * </pre>
 */
public class LogUtils {

    private static final String TAG = "ScMobile";

    private static boolean sLogEnabled = false;

    public static void setLogEnabled(boolean logEnabled) {
        sLogEnabled = logEnabled;
    }

    public static void LOGD(String text) {
        if (sLogEnabled) {
            Log.d(TAG, text);
        }
    }

    public static void LOGD(String text, Object... args) {
        if (sLogEnabled) {
            Log.d(TAG, args == null ? text : String.format(text, args));
        }
    }

    public static void LOGV(String text, Object... args) {
        if (sLogEnabled) {
            Log.v(TAG, args == null ? text : String.format(text, args));
        }
    }

    public static void LOGE(Throwable th) {
        Log.e(TAG, th.getMessage(), th);
    }

    public static void LOGE(String message, Throwable th) {
        Log.e(TAG, message, th);
    }

    public static void LOGE(String message, Object... args) {
        Log.e(TAG, String.format(message, args));
    }

    public static void logCursor(Cursor c) {
        LOGD(TextUtils.join("\t", c.getColumnNames()));
        int columns = c.getColumnCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                StringBuilder builder = new StringBuilder("" + i + ": ");
                for (int j = 0; j < columns; j++) {
                    builder.append(c.getString(j)).append(",\t");
                }
                LOGD(builder.toString());
                if (!c.moveToNext()) break;
            }
        }
    }

}
