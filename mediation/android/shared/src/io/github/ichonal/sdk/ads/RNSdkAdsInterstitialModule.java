package io.github.ichonal.sdk.ads;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.annotation.Inherited;

public abstract class RNSdkAdsInterstitialModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RNSdkAdsInterstitial";

    public RNSdkAdsInterstitialModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    protected void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    public void setTestDevices(ReadableArray devices) {
    }

    public abstract void requestAd();

    public abstract void showAd(final Promise promise);

    public abstract void isReady(final Promise promise);
}