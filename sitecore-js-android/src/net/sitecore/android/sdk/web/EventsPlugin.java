package net.sitecore.android.sdk.web;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import java.sql.Timestamp;
import java.util.Calendar;

import org.json.JSONException;

import static android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME;
import static android.provider.CalendarContract.EXTRA_EVENT_END_TIME;
import static android.provider.CalendarContract.Events;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EventsPlugin extends ScPlugin {
    private static final int REQUEST_NEW_EVENT = 2;

    private ScCallbackContext mCallbackContext;

    @Override
    public String getPluginName() {
        return "events";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_events);
    }

    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mCallbackContext.sendError("Creating calendar events is only supported in Android 4.0 or later!");
            return;
        }
        if (method.equalsIgnoreCase("save")) {
            String startDate = params.getString("startDate");
            String endDate = params.getString("endDate");

            String title = params.getString("title");
            String notes = params.getString("notes");
            String location = params.getString("location");

            Intent intent = new Intent(Intent.ACTION_INSERT).setData(Events.CONTENT_URI);

            if (startDate != null) {
                try {
                    Calendar startTime = parseTime(startDate);
                    intent.putExtra(EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
                } catch (NumberFormatException exception) {
//                    In case of exception we simply don't set it to the intent.
                }
            }

            if (endDate != null) {
                try {
                    Calendar endTime = parseTime(endDate);
                    intent.putExtra(EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                } catch (NumberFormatException exception) {
//                    In case of exception we simply don't set it to the intent.
                }
            }

            intent.putExtra(Events.TITLE, title)
                    .putExtra(Events.DESCRIPTION, notes)
                    .putExtra(Events.EVENT_LOCATION, location);

            startActivityForResult(intent, REQUEST_NEW_EVENT);
        }

    }

    private Calendar parseTime(String millis) throws NumberFormatException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(Long.valueOf(millis)));
        return calendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_EVENT) {
            mCallbackContext.sendSuccess();
        }
    }
}