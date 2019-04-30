package io.github.ichonal.sdk.ads.mopub;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.Map;

import io.github.ichonal.sdk.ads.R;
import io.github.ichonal.sdk.ads.RNSdkAdsBannerView;
import io.github.ichonal.sdk.ads.RNSdkAdsBannerViewManager;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant;

class RNSdkMoPubBannerView extends MoPubView implements MoPubView.BannerAdListener {

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

    //@Override
    public void loadBanner() {
        this.loadAd();
    }

    //@Override
    public void setAdSize(String sizeType) {

    }

    //@Override
    protected void sendOnSizeChangeEvent() {
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


public class RNSdkMoPubBannerViewManager extends SimpleViewManager<RNSdkMoPubBannerView> {
    public static final String REACT_CLASS = "RNSdkAdsBannerView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected RNSdkMoPubBannerView createViewInstance(ThemedReactContext reactContext) {
        RNSdkMoPubBannerView view = new RNSdkMoPubBannerView(reactContext);
        return view;
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                RNSdkAdsConstant.BannerEvent.SIZE_CHANGE,
                RNSdkAdsConstant.BannerEvent.LOADED,
                RNSdkAdsConstant.BannerEvent.FAILED_TO_LOAD,
                RNSdkAdsConstant.BannerEvent.OPENED,
                RNSdkAdsConstant.BannerEvent.CLOSED,
                RNSdkAdsConstant.BannerEvent.LEFT_APPLICATION
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(RNSdkAdsConstant.BannerCommandKey.LOAD_BANNER, RNSdkAdsConstant.BannerCommand.LOAD_BANNER);
    }

    @Override
    public void receiveCommand(RNSdkMoPubBannerView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
        switch (commandId) {
            case RNSdkAdsConstant.BannerCommand.LOAD_BANNER: {
                root.loadBanner();
                break;
            }
            default: {

            }
        }
    }

    @ReactProp(name = RNSdkAdsConstant.BannerProp.AD_SIZE)
    public void setPropAdSize(final RNSdkMoPubBannerView view, final String sizeType) {
        view.setAdSize(sizeType);
    }

    @ReactProp(name = RNSdkAdsConstant.BannerProp.TEST_DEVICES)
    public void setPropTestDevices(final RNSdkMoPubBannerView view, final ReadableArray devices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)devices;
        ArrayList<Object> list = nativeArray.toArrayList();
        //view.setTestDevices(list.toArray(new String[list.size()]));
    }
}
