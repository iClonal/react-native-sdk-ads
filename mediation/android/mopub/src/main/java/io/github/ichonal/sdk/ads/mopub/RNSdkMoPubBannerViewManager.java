package io.github.ichonal.sdk.ads.mopub;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import io.github.ichonal.sdk.ads.IRNSdkAdsBanner;
import io.github.ichonal.sdk.ads.RNSdkAdsBannerViewManager;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant;

class RNSdkMoPubBannerView extends MoPubView implements IRNSdkAdsBanner, MoPubView.BannerAdListener {

    String adUnitId;

    public RNSdkMoPubBannerView(final ThemedReactContext ctx) {
        super(ctx);
        try {
            PackageManager pm = ctx.getPackageManager();
            String pkgName = ctx.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitID = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkMoPubConstant.META_KEY_BANNER_ID);
            if (adUnitID == null) {
                adUnitID = RNSdkMoPubConstant.TEST_BANNER_ADUNIT_ID;
            }
            this.adUnitId = adUnitID;
        } catch (PackageManager.NameNotFoundException e) {
            this.adUnitId = RNSdkMoPubConstant.TEST_BANNER_ADUNIT_ID;
        }
        this.setAdUnitId(this.adUnitId);
        this.setBannerAdListener(this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        int width = Dips.asIntPixels(getAdWidth(), this.getContext());
        int height = Dips.asIntPixels(getAdHeight(), this.getContext());
        child.measure(width, height);
        child.layout(0, 0, width, height);
    }

    @Override
    public void loadBanner() {
        this.loadAd();
    }

    @Override
    public void setAdSize(String sizeType) {

    }

    @Override
    public void setTestDevices(String[] devices) {

    }

    @Override
    public void sendOnSizeChangeEvent() {
        WritableMap event = Arguments.createMap();
        int width = this.getAdWidth();
        int height = this.getAdHeight();
        event.putDouble("width", width);
        event.putDouble("height", height);
        sendEvent(RNSdkAdsConstant.BannerEvent.SIZE_CHANGE, event);
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        sendOnSizeChangeEvent();
        sendEvent(RNSdkAdsConstant.BannerEvent.LOADED, null);
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        String errorMessage = "Unknown error";
        switch (errorCode) {
            case NO_FILL: {
                errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                break;
            }
            default: {

            }
        }
        WritableMap event = Arguments.createMap();
        WritableMap error = Arguments.createMap();
        error.putString("message", errorMessage);
        event.putMap("error", error);
        sendEvent(RNSdkAdsConstant.BannerEvent.FAILED_TO_LOAD, event);

    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        sendEvent(RNSdkAdsConstant.BannerEvent.OPENED, null);
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {

    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

    }

    protected void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext ctx = (ReactContext) getContext();
        ctx.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), name, event);
    }
}


public class RNSdkMoPubBannerViewManager extends RNSdkAdsBannerViewManager<RNSdkMoPubBannerView> {
    @Override
    protected RNSdkMoPubBannerView createViewInstance(ThemedReactContext reactContext) {
        RNSdkMoPubBannerView view = new RNSdkMoPubBannerView(reactContext);
        return view;
    }
}
