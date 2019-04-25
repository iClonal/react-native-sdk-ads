
package io.github.ichonal.sdk.ads;

public class RNSdkAdsConstant {
    public static final String META_KEY_PREFIX = "io.github.ichonal.sdk.ads.";

    public static final class BannerSizeType {
        public static final String BANNER           = "banner"; // 320 X 50
        public static final String LARGE_BANNER     = "largeBanner";
        public static final String MEDIUM_RECTANGLE = "mediumRectangle";
        public static final String FULL_BANNER      = "fullBanner";
        public static final String LEADER_BOARD     = "leaderBoard";
        public static final String SMART_BANNER     = "smartBanner";
        public static final String SMART_PORTRAIT   = "smartBannerPortrait";
        public static final String SMART_LANDSCAPE  = "smartBannerLandscape";
    }

    public static final class BannerProp {
        public static final String AD_SIZE          = "adSize";
        public static final String TEST_DEVICES     = "testDevices";
    }

    public static final class BannerCommandKey {
        public static final String LOAD_BANNER      = "loadBanner";
    }

    public static final class BannerCommand {
        public static final int LOAD_BANNER         = 1;
    }

    public static final class BannerEvent {
        public static final String LOADED           = "onAdLoaded";
        public static final String FAILED_TO_LOAD   = "onAdFailedToLoad";
        public static final String SIZE_CHANGE      = "onSizeChange";
        public static final String OPENED           = "onAdOpened";
        public static final String LEFT_APPLICATION = "onAdLeftApplication";
        public static final String CLOSED           = "onAdClosed";
    }

    public static final class InterstitialEvent {
        public static final String LOADED           = "interstitialAdLoaded";
        public static final String FAILED_TO_LOAD   = "interstitialAdFailedToLoad";
        public static final String OPENED           = "interstitialAdOpened";
        public static final String LEFT_APPLICATION = "interstitialAdLeftApplication";
        public static final String CLOSED           = "interstitialAdClosed";
    }

    public static final class RewardedEvent {
        public static final String LOADED           = "rewardedVideoAdLoaded";
        public static final String FAILED_TO_LOAD   = "rewardedVideoAdFailedToLoad";
        public static final String OPENED           = "rewardedVideoAdOpened";
        public static final String STARTED          = "rewardedVideoAdVideoStarted";
        public static final String LEFT_APPLICATION = "rewardedVideoAdLeftApplication";
        public static final String REWARDED         = "rewardedVideoAdRewarded";
        public static final String CLOSED           = "rewardedVideoAdClosed";
    }
}