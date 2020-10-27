package com.rnalarmtester;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/*
    Created this class to run as a foreground service because
    Android has limitations to background location updates.

    If background location access is essential for your app, keep in mind that Android preserves device battery life by setting background location limits on devices that run Android 8.0 (API level 26) and higher. On these versions of Android, if your app is running in the background, it can receive location updates only a few times each hour. Learn more about background location limits.
    https://developer.android.com/training/location/background
 */

public class AlarmForegroundService extends Service {
    private final String TAG = "ALARM_FOREGROUNDSERVICE";
    private final String PACKAGE_NAME = "com.rnalarmtester";

    /**
     * The name of the channel for notifications.
     */
    private final String CHANNEL_ID = "RNALARM";
    private final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    static final int SERVICE_NOTIFICATION_ID = 12345678;

    public AlarmForegroundService() {}

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate AlarmForegroundService");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // immediately schedule the first alarm
        Context context = MainApplication.getContext();
        AlarmReceiver.scheduleExactAlarm(context, (AlarmManager) context.getSystemService(Context.ALARM_SERVICE));

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("RNAlarm Service")
                .setContentText("If you see this your app should be running fine...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy AlarmForegroundService");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.i(TAG, "onTaskRemoved AlarmForegroundService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            // Set the Notification Channel for the Notification Manager.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
