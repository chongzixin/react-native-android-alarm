package com.rnalarmtester;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class AlarmModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactContext;

    // constructor
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlarmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    // Mandatory function getName that specifies the module name
    @NonNull
    @Override
    public String getName() {
        return "AlarmModule";
    }

    @ReactMethod
    public void startService() {
        // start the service
        this.reactContext.startService(new Intent(this.reactContext, AlarmForegroundService.class));
    }
}
