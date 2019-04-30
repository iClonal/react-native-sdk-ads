package io.github.ichonal.sdk.ads;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

import io.github.ichonal.sdk.ads.mopub.RNSdkMoPubBannerViewManager;
import io.github.ichonal.sdk.ads.mopub.RNSdkMoPubInterstitialModule;
import io.github.ichonal.sdk.ads.mopub.RNSdkMoPubModule;
import io.github.ichonal.sdk.ads.mopub.RNSdkMoPubRewardedModule;

public class RNSdkAdsPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new RNSdkMoPubModule(reactContext),
                new RNSdkMoPubInterstitialModule(reactContext),
                new RNSdkMoPubRewardedModule(reactContext)
        );
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new RNSdkMoPubBannerViewManager()
        );
    }
}
