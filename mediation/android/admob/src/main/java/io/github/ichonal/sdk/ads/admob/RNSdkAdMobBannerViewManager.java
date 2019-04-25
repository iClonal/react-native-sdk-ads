package io.github.ichonal.sdk.ads.admob;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import io.github.ichonal.sdk.ads.RNSdkAdsBannerView;
import io.github.ichonal.sdk.ads.RNSdkAdsBannerViewManager;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant;
import io.github.ichonal.sdk.ads.RNSdkAdsConstant.BannerSizeType;

class RNSdkAdMobBannerView extends RNSdkAdsBannerView {
    protected AdView adView;

    String adUnitID;
    String[] testDevices;
    AdSize adSize;

    public RNSdkAdMobBannerView(final Context context) {
        super(context);
        try {
            PackageManager pm = context.getPackageManager();
            String pkgName = context.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String adUnitID = pInfo.metaData.getString(RNSdkAdsConstant.META_KEY_PREFIX + RNSdkAdMobConstant.META_KEY_BANNER_ID);
            if (adUnitID == null) {
                adUnitID = RNSdkAdMobConstant.TEST_BANNER_ADUNIT_ID;
            }
            this.adUnitID = adUnitID;
        } catch (PackageManager.NameNotFoundException e) {
            this.adUnitID = RNSdkAdMobConstant.TEST_BANNER_ADUNIT_ID;
        }
        this.createAdView();
        this.adView.setAdUnitId(this.adUnitID);
    }

    private void createAdView() {
        if (this.adView != null) this.adView.destroy();

        final Context context = getContext();
        this.adView = new AdView(context);
        this.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int width = adView.getAdSize().getWidthInPixels(context);
                int height = adView.getAdSize().getHeightInPixels(context);
                int left = adView.getLeft();
                int top = adView.getTop();
                adView.measure(width, height);
                adView.layout(left, top, left + width, top + height);
                sendOnSizeChangeEvent();
                sendEvent(RNSdkAdsConstant.BannerEvent.LOADED, null);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorMessage = "Unknown error";
                switch (errorCode) {
                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        errorMessage = "Internal error, an invalid response was received from the ad server.";
                        break;
                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
                        errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
                        break;
                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
                        errorMessage = "The ad request was unsuccessful due to network connectivity.";
                        break;
                    case AdRequest.ERROR_CODE_NO_FILL:
                        errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                        break;
                }
                WritableMap event = Arguments.createMap();
                WritableMap error = Arguments.createMap();
                error.putString("message", errorMessage);
                event.putMap("error", error);
                sendEvent(RNSdkAdsConstant.BannerEvent.FAILED_TO_LOAD, event);
            }

            @Override
            public void onAdOpened() {
                sendEvent(RNSdkAdsConstant.BannerEvent.OPENED, null);
            }

            @Override
            public void onAdClosed() {
                sendEvent(RNSdkAdsConstant.BannerEvent.CLOSED, null);
            }

            @Override
            public void onAdLeftApplication() {
                sendEvent(RNSdkAdsConstant.BannerEvent.LEFT_APPLICATION, null);
            }
        });
        this.addView(this.adView);
    }

    @Override
    protected void sendOnSizeChangeEvent() {
        int width;
        int height;
        ReactContext reactContext = (ReactContext) getContext();
        WritableMap event = Arguments.createMap();
        AdSize adSize = this.adView.getAdSize();
        if (this.adSize == AdSize.SMART_BANNER) {
            width = (int) PixelUtil.toDIPFromPixel(adSize.getWidthInPixels(reactContext));
            height = (int) PixelUtil.toDIPFromPixel(adSize.getHeightInPixels(reactContext));
        } else {
            width = adSize.getWidth();
            height = adSize.getHeight();
        }
        event.putDouble("width", width);
        event.putDouble("height", height);
        sendEvent(RNSdkAdsConstant.BannerEvent.SIZE_CHANGE, event);
    }

    @Override
    public void loadBanner() {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        if (testDevices != null) {
            for (int i = 0; i < testDevices.length; i++) {
                adRequestBuilder.addTestDevice(testDevices[i]);
            }
        }
        AdRequest adRequest = adRequestBuilder.build();
        this.adView.loadAd(adRequest);
    }

    @Override
    public void setTestDevices(String[] testDevices) {
        this.testDevices = testDevices;
    }

    @Override
    public void setAdSize(String sizeType) {
        AdSize size = AdSize.BANNER;
        switch (sizeType) {
            case BannerSizeType.BANNER: {
                size = AdSize.BANNER;
                break;
            }
            case BannerSizeType.LARGE_BANNER: {
                size = AdSize.LARGE_BANNER;
                break;
            }
            case BannerSizeType.FULL_BANNER: {
                size = AdSize.FULL_BANNER;
                break;
            }
            case BannerSizeType.MEDIUM_RECTANGLE: {
                size = AdSize.MEDIUM_RECTANGLE;
                break;
            }
            case BannerSizeType.LEADER_BOARD: {
                size = AdSize.LEADERBOARD;
                break;
            }
            case BannerSizeType.SMART_BANNER: {
                size = AdSize.SMART_BANNER;
                break;
            }
            case BannerSizeType.SMART_LANDSCAPE: {
                size = AdSize.SMART_BANNER;
                break;
            }
            case BannerSizeType.SMART_PORTRAIT: {
                size = AdSize.SMART_BANNER;
                break;
            }
            default: {
                size = AdSize.BANNER;
            }
        }

        this.adSize = size;
        this.adView.setAdSize(size);
    }
}

public class RNSdkAdMobBannerViewManager extends RNSdkAdsBannerViewManager<RNSdkAdMobBannerView> {
    @Override
    protected RNSdkAdMobBannerView createViewInstance(ThemedReactContext reactContext) {
        RNSdkAdMobBannerView view = new RNSdkAdMobBannerView(reactContext);
        return view;
    }
}
