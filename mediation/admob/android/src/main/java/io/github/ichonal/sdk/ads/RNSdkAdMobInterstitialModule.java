package io.github.ichonal.sdk.ads;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class RNSdkAdMobInterstitialModule extends RNSdkAdsInterstitialModule {
    InterstitialAd mInterstitialAd;
    String[] testDevices;

    public RNSdkAdMobInterstitialModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mInterstitialAd = new InterstitialAd(reactContext);

        try {
            PackageManager pm = reactContext.getPackageManager();
            String pkgName = reactContext.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitId = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkAdMobConstant.META_KEY_INTERSTITIAL_ID);
            if (adUnitId == null) {
                adUnitId = RNSdkAdMobConstant.TEST_INTERSTITIAL_ADUNIT_ID;
            }
            mInterstitialAd.setAdUnitId(adUnitId);
        } catch (PackageManager.NameNotFoundException e) {
            //TODO: WARNING
            mInterstitialAd.setAdUnitId(RNSdkAdMobConstant.TEST_INTERSTITIAL_ADUNIT_ID);
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                sendEvent(RNSdkAdsConstant.InterstitialEvent.CLOSED, null);
                RNSdkAdMobInterstitialModule.this.requestAd();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorString = "ERROR_UNKNOWN";
                String errorMessage = "Unknown error";
                switch (errorCode) {
                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        errorString = "ERROR_CODE_INTERNAL_ERROR";
                        errorMessage = "Internal error, an invalid response was received from the ad server.";
                        break;
                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
                        errorString = "ERROR_CODE_INVALID_REQUEST";
                        errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
                        break;
                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
                        errorString = "ERROR_CODE_NETWORK_ERROR";
                        errorMessage = "The ad request was unsuccessful due to network connectivity.";
                        break;
                    case AdRequest.ERROR_CODE_NO_FILL:
                        errorString = "ERROR_CODE_NO_FILL";
                        errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                        break;
                }
                WritableMap event = Arguments.createMap();
                WritableMap error = Arguments.createMap();
                event.putString("message", errorMessage);
                sendEvent(RNSdkAdsConstant.InterstitialEvent.FAILED_TO_LOAD, event);
            }
            @Override
            public void onAdLeftApplication() {
                sendEvent(RNSdkAdsConstant.InterstitialEvent.LEFT_APPLICATION, null);
            }
            @Override
            public void onAdLoaded() {
                sendEvent(RNSdkAdsConstant.InterstitialEvent.LOADED, null);
            }
            @Override
            public void onAdOpened() {
                sendEvent(RNSdkAdsConstant.InterstitialEvent.OPENED, null);
            }
        });
    }

    @Override
    @ReactMethod
    public void setTestDevices(ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        this.testDevices = list.toArray(new String[list.size()]);
    }

    @Override
    @ReactMethod
    public void requestAd() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mInterstitialAd.isLoaded() || mInterstitialAd.isLoading()) {
                    return;
                } else {
                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            adRequestBuilder.addTestDevice(testDevices[i]);
                        }
                    }
                    AdRequest adRequest = adRequestBuilder.build();
                    mInterstitialAd.loadAd(adRequest);
                }
            }
        });
    }

    @Override
    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mInterstitialAd.isLoaded()) {
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
            public void run () {
                promise.resolve(mInterstitialAd.isLoaded());
            }
        });
    }
}
