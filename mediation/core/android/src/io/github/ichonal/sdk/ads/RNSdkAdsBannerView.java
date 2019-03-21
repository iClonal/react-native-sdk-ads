package io.github.ichonal.sdk.ads;

import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

public abstract class RNSdkAdsBannerView extends ReactViewGroup {
    public RNSdkAdsBannerView(final Context context) {
        super(context);
    }

    protected void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext ctx = (ReactContext) getContext();
        ctx.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), name, event);
    }

    public abstract void loadBanner();

    public void setTestDevices(String[] devices) {
    }

    public abstract void setAdSize(String sizeType);

    protected abstract void sendOnSizeChangeEvent();
}
