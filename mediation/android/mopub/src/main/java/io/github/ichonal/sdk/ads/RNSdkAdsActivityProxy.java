package io.github.ichonal.sdk.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;

import io.github.ichonal.sdk.ads.mopub.RNSdkMoPubConstant;

public class RNSdkAdsActivityProxy {

    public RNSdkAdsActivityProxy() {
    }

    public void destroy() {
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        SdkConfiguration sdkConf = new SdkConfiguration.Builder(RNSdkMoPubConstant.TEST_REWARDED_ADUNIT_ID)
                .withLogLevel(BuildConfig.DEBUG ? MoPubLog.LogLevel.DEBUG : MoPubLog.LogLevel.NONE)
                .build();

        MoPub.initializeSdk(activity, sdkConf, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d("[RNSdkMoPub]", "onInitializationFinished: xxxxxxx");
            }
        });
    }

    public void onActivityStarted(Activity activity) {

    }

    public void onActivityResumed(Activity activity) {

    }

    public void onActivityPaused(Activity activity) {

    }

    public void onActivityStopped(Activity activity) {

    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    public void onActivityDestroyed(Activity activity) {

    }
}
