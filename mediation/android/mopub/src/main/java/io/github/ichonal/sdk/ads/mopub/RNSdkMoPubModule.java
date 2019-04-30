package io.github.ichonal.sdk.ads.mopub;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;

import io.github.ichonal.sdk.ads.BuildConfig;

public class RNSdkMoPubModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RNSdkMoPub";

    public RNSdkMoPubModule(final ReactApplicationContext reactContext) {
        super(reactContext);
//
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                SdkConfiguration sdkConf = new SdkConfiguration.Builder(RNSdkMoPubConstant.TEST_REWARDED_ADUNIT_ID)
//                        .withLogLevel(BuildConfig.DEBUG ? MoPubLog.LogLevel.DEBUG : MoPubLog.LogLevel.NONE)
//                        .build();
//
//                MoPub.initializeSdk(getCurrentActivity(), sdkConf, new SdkInitializationListener() {
//                    @Override
//                    public void onInitializationFinished() {
//                        Log.d("[RNSdkMoPub]", "onInitializationFinished: xxxxxxx");
//                    }
//                });
//            }
//        });


    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }
}
