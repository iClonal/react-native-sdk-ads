package io.github.ichonal.sdk.ads.mopub;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import io.github.ichonal.sdk.ads.RNSdkAdsConstant;
import io.github.ichonal.sdk.ads.RNSdkAdsRewardedModule;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.Set;

public class RNSdkMoPubRewardedModule extends RNSdkAdsRewardedModule implements MoPubRewardedVideoListener {
    public static final String TAG = "[RNSdkMoPubRewarded]";

    String mAdUnitId;

    public RNSdkMoPubRewardedModule(ReactApplicationContext reactContext) {
        super(reactContext);
        try {
            Context ctx = reactContext.getApplicationContext();

            PackageManager pm = ctx.getPackageManager();
            String pkgName = ctx.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitID = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkMoPubConstant.META_KEY_REWARDED_ID);
            if (adUnitID == null) {
                adUnitID = RNSdkMoPubConstant.TEST_REWARDED_ADUNIT_ID;
            }
            this.mAdUnitId = adUnitID;
        } catch (PackageManager.NameNotFoundException e) {
            this.mAdUnitId = RNSdkMoPubConstant.TEST_REWARDED_ADUNIT_ID;
        }
        MoPubRewardedVideos.setRewardedVideoListener(this);
    }


    @Override
    public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
        sendEvent(RNSdkAdsConstant.RewardedEvent.LOADED, null);
    }

    @Override
    public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        String errorString = "ERROR_UNKNOWN";
        String errorMessage = "Unknown error";
        switch (errorCode) {
            case NO_FILL: {
                errorString = "ERROR_CODE_NO_FILL";
                errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                break;
            }
            default: {

            }
        }
        WritableMap event = Arguments.createMap();
        event.putString("message", errorMessage);
        sendEvent(RNSdkAdsConstant.RewardedEvent.FAILED_TO_LOAD, event);
    }

    @Override
    public void onRewardedVideoStarted(@NonNull String adUnitId) {
        sendEvent(RNSdkAdsConstant.RewardedEvent.OPENED, null);
    }

    @Override
    public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {

    }

    @Override
    public void onRewardedVideoClicked(@NonNull String adUnitId) {
        sendEvent(RNSdkAdsConstant.RewardedEvent.LEFT_APPLICATION, null);
    }

    @Override
    public void onRewardedVideoClosed(@NonNull String adUnitId) {
        sendEvent(RNSdkAdsConstant.RewardedEvent.CLOSED, null);
    }

    @Override
    public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
        WritableMap result = Arguments.createMap();

        result.putInt("amount", reward.getAmount());
        result.putString("type", reward.getLabel());

        sendEvent(RNSdkAdsConstant.RewardedEvent.REWARDED, result);
    }


    @Override
    @ReactMethod
    public void requestAd() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Activity ctx = RNSdkMoPubRewardedModule.this.getCurrentActivity();
                if (ctx == null) {
                    return;
                }

                if (!MoPubRewardedVideos.hasRewardedVideo(mAdUnitId)) {
                    MoPubRewardedVideos.loadRewardedVideo(mAdUnitId);
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
                if (MoPubRewardedVideos.hasRewardedVideo(mAdUnitId)) {
                    MoPubRewardedVideos.showRewardedVideo(mAdUnitId);
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
                promise.resolve(MoPubRewardedVideos.hasRewardedVideo(mAdUnitId));
            }
        });

    }
}
