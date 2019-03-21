
package io.github.ichonal.sdk.ads;

class RNSdkAdsConstant {
    static final String META_KEY_PREFIX = "io.github.ichonal.sdk.ads.";

    static final class BannerSizeType {
        static final String BANNER           = "banner"; // 320 X 50
        static final String LARGE_BANNER     = "largeBanner";
        static final String MEDIUM_RECTANGLE = "mediumRectangle";
        static final String FULL_BANNER      = "fullBanner";
        static final String LEADER_BOARD     = "leaderBoard";
        static final String SMART_BANNER     = "smartBanner";
        static final String SMART_PORTRAIT   = "smartBannerPortrait";
        static final String SMART_LANDSCAPE  = "smartBannerLandscape";
    }

    static final class BannerProp {
        static final String AD_SIZE          = "adSize";
        static final String TEST_DEVICES     = "testDevices";
    }

    static final class BannerCommandKey {
        static final String LOAD_BANNER      = "loadBanner";
    }

    static final class BannerCommand {
        static final int LOAD_BANNER         = 1;
    }

    static final class BannerEvent {
        static final String LOADED           = "bannerAdLoaded";
        static final String FAILED_TO_LOAD   = "bannerAdFailedToLoad";
        static final String SIZE_CHANGE      = "bannerSizeChange";
        static final String OPENED           = "bannerAdOpened";
        static final String LEFT_APPLICATION = "bannerAdLeftApplication";
        static final String CLOSED           = "bannerAdClosed";
    }

    static final class InterstitialEvent {
        static final String LOADED           = "interstitialAdLoaded";
        static final String FAILED_TO_LOAD   = "interstitialAdFailedToLoad";
        static final String OPENED           = "interstitialAdOpened";
        static final String LEFT_APPLICATION = "interstitialAdLeftApplication";
        static final String CLOSED           = "interstitialAdClosed";
    }

    static final class RewardedEvent {
        static final String LOADED           = "rewardedVideoAdLoaded";
        static final String FAILED_TO_LOAD   = "rewardedVideoAdFailedToLoad";
        static final String OPENED           = "rewardedVideoAdOpened";
        static final String STARTED          = "rewardedVideoAdVideoStarted";
        static final String LEFT_APPLICATION = "rewardedVideoAdLeftApplication";
        static final String REWARDED         = "rewardedVideoAdRewarded";
        static final String CLOSED           = "rewardedVideoAdClosed";
    }
}