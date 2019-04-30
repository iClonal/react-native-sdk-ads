package io.github.ichonal.sdk.ads.mopub;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import io.github.ichonal.sdk.ads.RNSdkAdsConstant;
import io.github.ichonal.sdk.ads.RNSdkAdsInterstitialModule;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class RNSdkMoPubInterstitialModule extends RNSdkAdsInterstitialModule implements MoPubInterstitial.InterstitialAdListener {

    MoPubInterstitial mInterstitialAd;
    boolean mAdLoaded = false;
    boolean mAdLoading = false;

    public RNSdkMoPubInterstitialModule(ReactApplicationContext reactContext) {
        super(reactContext);

        createInterstitialAd();
    }

    void createInterstitialAd() {
        if (mInterstitialAd == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Activity currActivity = getCurrentActivity();
                    if (currActivity != null) {

                        String adUnitId;
                        try {
                            Context ctx = getReactApplicationContext();
                            PackageManager pm = ctx.getPackageManager();
                            String pkgName = ctx.getPackageName();
                            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
                            adUnitId = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkMoPubConstant.META_KEY_INTERSTITIAL_ID);
                        } catch (PackageManager.NameNotFoundException e) {
                            //TODO: WARNING
                            adUnitId = RNSdkMoPubConstant.TEST_INTERSTITIAL_ADUNIT_ID;
                        }

                        // mInterstitialAd = new MoPubInterstitial(currActivity, adUnitId);
                        mInterstitialAd = new MoPubInterstitial(currActivity, RNSdkMoPubConstant.TEST_INTERSTITIAL_ADUNIT_ID);
                        mInterstitialAd.setInterstitialAdListener(RNSdkMoPubInterstitialModule.this);
                    }
                }
            });
        }
    }

    boolean isAdLoaded() {
        return mAdLoaded;
    }

    boolean isAdLoading() {
        return mAdLoading;
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        mAdLoaded = true;
        mAdLoading = false;
        sendEvent(RNSdkAdsConstant.InterstitialEvent.LOADED, null);
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        mAdLoaded = false;
        mAdLoading = false;
        String errorString = "ERROR_UNKNOWN";
        String errorMessage = "Unknown error";
        switch (errorCode) {
            case NO_FILL:
                errorString = "ERROR_CODE_NO_FILL";
                errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                break;
            default: {

            }
        }
        WritableMap event = Arguments.createMap();
        //WritableMap error = Arguments.createMap();
        event.putString("message", errorMessage);
        sendEvent(RNSdkAdsConstant.InterstitialEvent.FAILED_TO_LOAD, event);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        mAdLoaded = false;
        sendEvent(RNSdkAdsConstant.InterstitialEvent.OPENED, null);
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        mAdLoaded = false;
        sendEvent(RNSdkAdsConstant.InterstitialEvent.LEFT_APPLICATION, null);
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        mAdLoaded = false;

        sendEvent(RNSdkAdsConstant.InterstitialEvent.CLOSED, null);
        requestAd();
    }

    @Override
    @ReactMethod
    public void requestAd() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd == null) {
                    createInterstitialAd();
                } else {
                    if (mInterstitialAd.isReady() || isAdLoaded() || isAdLoading()) {
                        return;
                    }
                    mInterstitialAd.load();
                    mAdLoading = true;
                }
            }
        });
    }

    @Override
    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isReady()) {
                    mInterstitialAd.show();
                    promise.resolve(null);
                } else {
                    promise.reject("E_AD_NOT_READY", "Ad is not ready.");
                }
            }
        });
    }

    @Override
    @ReactMethod
    public void isReady(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                promise.resolve(mInterstitialAd.isReady());
            }
        });
    }
}
