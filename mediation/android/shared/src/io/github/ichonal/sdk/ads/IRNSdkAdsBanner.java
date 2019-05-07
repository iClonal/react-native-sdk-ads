package io.github.ichonal.sdk.ads;

public interface IRNSdkAdsBanner {
    void loadBanner();
    void setTestDevices(String[] devices);
    void setAdSize(String sizeType);
    void sendOnSizeChangeEvent();
}
