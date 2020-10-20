package com.rnalarmtester;

import android.os.Build;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class DeviceModule extends ReactContextBaseJavaModule {

    // constructor
    public DeviceModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    // Mandatory function getName that specifies the module name
    @NonNull
    @Override
    public String getName() {
        return "Device";
    }

    // Custom function that we export to JS
    @ReactMethod
    public void getDeviceName(Callback cb) {
        try {
            cb.invoke(null, Build.MODEL);
        } catch (Exception e) {
            cb.invoke(e.toString(), null);
        }
    }
}
