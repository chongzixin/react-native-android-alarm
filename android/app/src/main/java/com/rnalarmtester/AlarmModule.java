package com.rnalarmtester;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class AlarmModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactContext;

    // constructor
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlarmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        // immediately schedule the first alarm
        AlarmReceiver.scheduleExactAlarm(reactContext, (AlarmManager) reactContext.getSystemService(Context.ALARM_SERVICE));
        Log.d("AlarmModule", "AlarmModule Constructor called");
    }

    // Mandatory function getName that specifies the module name
    @NonNull
    @Override
    public String getName() {
        return "AlarmModule";
    }
}
