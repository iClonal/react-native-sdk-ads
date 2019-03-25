package io.github.ichonal.sdk.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public abstract class RNSdkAdsRewardedModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    public static final String REACT_CLASS = "RNSdkAdsRewarded";
    public static final String TAG = "RNSdkAdsRewarded";

    public RNSdkAdsRewardedModule(ReactApplicationContext reactContext) {
        super(reactContext);
        //reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }


    ///-----------------------------------------------------------------------
    // Activity Lifecycle Callbacks

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    //
//    @Override
//    public void onActivityCreated(Activity activity, Bundle bundle) {
//    }
//
//    @Override
//    public void onActivityStarted(Activity activity) {
//    }
//
//    @Override
//    public void onActivityResumed(Activity activity) {
//    }
//
//    @Override
//    public void onActivityPaused(Activity activity) {
//    }
//
//    @Override
//    public void onActivityStopped(Activity activity) {
//    }
//
//    @Override
//    public void onActivityDestroyed(Activity activity) {
//    }
//
//    @Override
//    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//    }
    ///-----------------------------------------------------------------------

    protected void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    public void setTestDevices(ReadableArray devices) {
    }

    public abstract void requestAd();

    public abstract void showAd(final Promise promise);

    public abstract void isReady(final Promise promise);
}
