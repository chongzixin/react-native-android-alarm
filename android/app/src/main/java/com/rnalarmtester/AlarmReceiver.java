package com.rnalarmtester;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM_FREQUENCY = 60*1000;
    private static final String TAG = "ALARM_RECEIVER";

    static final int LOCATION_INTERVAL = 10*1000;
    static final int FASTEST_LOCATION_INTERVAL = LOCATION_INTERVAL/2;

    static Location location;
    static FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainApplication.getContext());
    static LocationRequest locationRequest = LocationRequest.create();
    static LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            location = locationResult.getLastLocation();
            Log.i(TAG, "Got a new location: " + getLocationText(location));
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, Intent intent) {
        // schedule for next alarm
        scheduleExactAlarm(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE));

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RNALARMTESTER_WAKELOCK:ALARM_RECEIVER");
        wakeLock.acquire();

        // TODO: IMPLEMENT THIS
        // process the current location if it exists, particularly important because location may not be available when the app is first launched or if permission is not given
        if(location != null)
            processLocation(location);

        // prepare to request for location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);

        try {
            Log.d(TAG, "requesting location updates");
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
        catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }

        wakeLock.release();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleExactAlarm(Context context, AlarmManager alarms) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS");
        Date resultdate = new Date((System.currentTimeMillis()+ALARM_FREQUENCY));
        String dateToShow = sdf.format(resultdate);
        Log.d(TAG, "scheduling next alarm at " + dateToShow);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarms.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+ALARM_FREQUENCY, null), pendingIntent);
    }

    private void sendEvent(String eventName, String eventText) {
        try{
            ReactContext reactContext = AlarmModule.reactContext;
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, eventText);
        }
        catch(Exception e){
            Log.d("ReactNativeJS","Exception in sendEvent in EventReminderBroadcastReceiver is:"+e.toString());
        }
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS");
        Date currentTimestamp = new Date(System.currentTimeMillis());
        String timestampToShow = sdf.format(currentTimestamp);
        return timestampToShow;
    }

    private void processLocation(Location location) {
        Context context = MainApplication.getContext();

        Log.i(TAG, "can get location here leh: " + getLocationText(location));
        String toWrite = getTimestamp() + ": " + getLocationText(location);

        // send using HeadlessJS
        Intent alarmHeadlessIntent = new Intent(context, AlarmHeadlessService.class);
        Bundle bundle = new Bundle();
        bundle.putString("ALARM_EVENT", toWrite);
        alarmHeadlessIntent.putExtras(bundle);
        context.startService(alarmHeadlessIntent);
        HeadlessJsTaskService.acquireWakeLockNow(context);
    }

    private static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }
}