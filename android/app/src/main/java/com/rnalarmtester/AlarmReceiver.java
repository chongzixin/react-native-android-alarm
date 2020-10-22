package com.rnalarmtester;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM_FREQUENCY = 3*1000;
    private static final String TAG = "ALARM_RECEIVER";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, Intent intent) {
        // schedule for next alarm
        scheduleExactAlarm(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE));

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RNALARMTESTER_WAKELOCK:ALARM_RECEIVER");
        wakeLock.acquire();

        // TODO: IMPLEMENT THIS
        // start with just a log, then after that implement event to send this back to react native layer
        String timestamp = getTimestamp();

        // sends event using Native Emitter
        // sendEvent("ALARM_EVENT", timestamp);

        // send using HeadlessJS
        Intent alarmHeadlessIntent = new Intent(context, AlarmHeadlessService.class);
        Bundle bundle = new Bundle();
        bundle.putString("ALARM_EVENT", timestamp);
        alarmHeadlessIntent.putExtras(bundle);
        context.startService(alarmHeadlessIntent);
        HeadlessJsTaskService.acquireWakeLockNow(context);


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
}