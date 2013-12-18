package net.sitecore.android.sdk.web;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Plugin for retrieving device motion data.
 * <p>Example :
 * <pre>
 *     var accelerometer;
 *     function start() {
 *         var xMotion = document.getElementById('xMotion');
 *         var yMotion = document.getElementById('yMotion');
 *         var zMotion = document.getElementById('zMotion');
 *         var timestamp = document.getElementById('timestamp');
 *         accelerometer = new scmobile.motion_manager.Accelerometer();
 *         var onAcceleration = function(data) {
 *             xMotion.innerHTML = 'X: ' + data.x;
 *             yMotion.innerHTML = 'Y: ' + data.y;
 *             zMotion.innerHTML = 'Z: ' + data.z;
 *             timestamp.innerHTML = 'Timestamp: ' + data.timestamp;
 *         }
 *
 *         var onError = function(data) {
 *             console.log("error: " + data.localizedDescription);
 *          }
 *
 *         accelerometer.start(onAcceleration, onError);
 *     }
 *
 *     function stop() {
 *         accelerometer.stop();
 *     }
 * </pre>
 *
 * @since Sitecore Mobile SDK for Android 1.0
 */
public final class AccelerometerPlugin extends ScPlugin implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ScCallbackContext mCallbackContext;

    private int mStatus;

    @Override
    public String getPluginName() {
        return "motion_manager";
    }

    @Override
    public String getPluginJsCode() {
        return IoUtils.readRawTextFile(mContext, R.raw.plugin_accelerometer);
    }

    @Override
    public void init(Context context, ScPluginManager pluginManager) {
        super.init(context, pluginManager);
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * Triggers accelerometer on/off.
     *
     * @param method          {@code String} method name.
     * @param params          {@link ScParams} params that will be used by action.
     * @param callbackContext {@link ScCallbackContext} callback for action.
     *
     * @throws JSONException if execution fails.
     */
    @Override
    public void exec(String method, ScParams params, ScCallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        if (method.equals("start")) {
            if (mSensor != null) {
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            }
        } else if (method.equals("stop")) {
            mSensorManager.unregisterListener(this, mSensor);
        }
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        long timestamp = System.currentTimeMillis();
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        JSONObject data = new JSONObject();
        try {
            data.put("x", x);
            data.put("y", y);
            data.put("z", z);
            data.put("timestamp", timestamp);
        } catch (JSONException e) {
            mCallbackContext.sendError(e.getMessage());
        }

        //Log.d(TAG, "sensor send success");
        mCallbackContext.sendSuccess(data.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
