package io.github.ichonal.sdk.ads;

import java.util.ArrayList;
import java.util.Map;

import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import io.github.ichonal.sdk.ads.RNSdkAdsConstant.BannerCommandKey;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant.BannerCommand;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant.BannerEvent;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant.BannerProp;


public abstract class RNSdkAdsBannerViewManager<T extends RNSdkAdsBannerView> extends ViewGroupManager<T> {
    public static final String REACT_CLASS = "RNSdkAdsBannerView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public void addView(T parent, View child, int index) {
        throw new RuntimeException("RNSGADBannerView cannot have subviews");
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
        return MapBuilder.of(BannerCommandKey.LOAD_BANNER, BannerCommand.LOAD_BANNER);
    }

    @Override
    public void receiveCommand(T root, int commandId, @javax.annotation.Nullable ReadableArray args) {
        switch (commandId) {
            case BannerCommand.LOAD_BANNER: {
                root.loadBanner();
                break;
            }
            default: {

            }
        }
    }

    @ReactProp(name = BannerProp.AD_SIZE)
    public void setPropAdSize(final T view, final String sizeType) {
        view.setAdSize(sizeType);
    }

    @ReactProp(name = BannerProp.TEST_DEVICES)
    public void setPropTestDevices(final T view, final ReadableArray devices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)devices;
        ArrayList<Object> list = nativeArray.toArrayList();
        view.setTestDevices(list.toArray(new String[list.size()]));
    }
}
