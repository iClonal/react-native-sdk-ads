
package io.github.ichonal.sdk.ads;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

import io.github.ichonal.sdk.ads.admob.RNSdkAdMobBannerViewManager;
import io.github.ichonal.sdk.ads.admob.RNSdkAdMobInterstitialModule;
import io.github.ichonal.sdk.ads.admob.RNSdkAdMobModule;
import io.github.ichonal.sdk.ads.admob.RNSdkAdMobRewardedModule;

public class RNSdkAdsPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new RNSdkAdMobModule(reactContext),
                new RNSdkAdMobInterstitialModule(reactContext),
                new RNSdkAdMobRewardedModule(reactContext)
        );
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
            new RNSdkAdMobBannerViewManager()
        );
    }
}