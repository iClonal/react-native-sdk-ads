package io.github.ichonal.sdk.ads;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

public class RNSdkAdMobRewardedModule extends RNSdkAdsRewardedModule implements RewardedVideoAdListener {
    public static final String TAG = "[RNSdkAdMobRewarded]";

    RewardedVideoAd mRewardedVideoAd;
    String adUnitID;
    String[] testDevices;

    public RNSdkAdMobRewardedModule(ReactApplicationContext reactContext) {
        super(reactContext);

        try {
            PackageManager pm = reactContext.getPackageManager();
            String pkgName = reactContext.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitID = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkAdMobConstant.META_KEY_REWARDED_ID);
            if (adUnitID == null) {
                adUnitID = RNSdkAdMobConstant.TEST_REWARDED_ADUNIT_ID;
            }
            this.adUnitID = adUnitID;
        } catch (PackageManager.NameNotFoundException e) {
            //TODO: WARNING
            this.adUnitID = RNSdkAdMobConstant.TEST_REWARDED_ADUNIT_ID;
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        WritableMap reward = Arguments.createMap();

        reward.putInt("amount", rewardItem.getAmount());
        reward.putString("type", rewardItem.getType());

        sendEvent(RNSdkAdsConstant.RewardedEvent.REWARDED, reward);
    }

    @Override
    public void onRewardedVideoCompleted() {
        //TODO:
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        sendEvent(RNSdkAdsConstant.RewardedEvent.LOADED, null);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        sendEvent(RNSdkAdsConstant.RewardedEvent.OPENED, null);
    }

    @Override
    public void onRewardedVideoStarted() {
        sendEvent(RNSdkAdsConstant.RewardedEvent.STARTED, null);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        sendEvent(RNSdkAdsConstant.RewardedEvent.CLOSED, null);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        sendEvent(RNSdkAdsConstant.RewardedEvent.LEFT_APPLICATION, null);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
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
        sendEvent(RNSdkAdsConstant.RewardedEvent.FAILED_TO_LOAD, event);
    }

    public void setAdUnitID(String adUnitID) {
        this.adUnitID = adUnitID;
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
            public void run() {
                Context ctx = RNSdkAdMobRewardedModule.this.getCurrentActivity();
                if (ctx == null) {
                    ctx = RNSdkAdMobRewardedModule.this.getReactApplicationContext();
                }
                RNSdkAdMobRewardedModule.this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(ctx);

                RNSdkAdMobRewardedModule.this.mRewardedVideoAd.setRewardedVideoAdListener(RNSdkAdMobRewardedModule.this);

                if (!mRewardedVideoAd.isLoaded()) {
                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            adRequestBuilder.addTestDevice(testDevices[i]);
                        }
                    }

                    AdRequest adRequest = adRequestBuilder.build();
                    mRewardedVideoAd.loadAd(adUnitID, adRequest);
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
                if ((mRewardedVideoAd != null) && mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
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
                promise.resolve((mRewardedVideoAd != null) && mRewardedVideoAd.isLoaded());
            }
        });
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        Log.d(TAG, activity.getClass().getSimpleName() + " Resume");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);
        Log.d(TAG, activity.getClass().getSimpleName() + " Pause");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        Log.d(TAG, activity.getClass().getSimpleName() + " Destroy");
    }
}
