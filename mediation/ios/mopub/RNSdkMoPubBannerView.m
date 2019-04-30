#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/UIView+React.h>
#import <React/RCTLog.h>
#else
#import "RCTBridgeModule.h"
#import "UIView+React.h"
#import "RCTLog.h"
#endif

#import "RNSdkAdsConstant.h"
#import "RNSdkMoPubConstant.h"
#import "RNSdkMoPubBannerView.h"

@implementation RNSdkMoPubBannerView {
    MPAdView *_bannerView;
}

- (void) dealloc {
    _bannerView.delegate = nil;
}

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        NSString *adUnitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_MOPUB_BANNER_ID];
        if (adUnitId != nil) {
            _adUnitId = adUnitId;
        } else {
            _adUnitId = TEST_BANNER_ADUNIT_ID;
        }
        
        super.backgroundColor = [UIColor clearColor];
        _bannerView = [[MPAdView alloc] initWithAdUnitId:_adUnitId size:MOPUB_BANNER_SIZE];
        _bannerView.delegate = self;
        _bannerView.frame = CGRectMake((self.bounds.size.width - MOPUB_BANNER_SIZE.width) / 2,
                                       (self.bounds.size.height - MOPUB_BANNER_SIZE.height) / 2,
                                       MOPUB_BANNER_SIZE.width, MOPUB_BANNER_SIZE.height);
        [self addSubview:_bannerView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (_bannerView != nil) {
        _bannerView.frame = CGRectMake((self.bounds.size.width - MOPUB_BANNER_SIZE.width) / 2,
                                       (self.bounds.size.height - MOPUB_BANNER_SIZE.height) / 2,
                                       MOPUB_BANNER_SIZE.width, MOPUB_BANNER_SIZE.height);
    }
}

- (void)loadBanner {
    if (_bannerView != nil) {
        [_bannerView loadAd];
    }
}

# pragma mark MPAdViewDelegate

- (UIViewController *)viewControllerForPresentingModalView {
    UIWindow *keyWin = [[UIApplication sharedApplication] keyWindow];
    return [keyWin rootViewController];
}

- (void)adViewDidLoadAd:(MPAdView *)view {
    CGSize size = [view adContentViewSize];
    CGFloat centeredX = (self.bounds.size.width - size.width) / 2;
    CGFloat bottomAlignedY = self.bounds.size.height - size.height;
    view.frame = CGRectMake(centeredX, bottomAlignedY, size.width, size.height);
    if (self.onAdLoaded) {
        self.onAdLoaded(@{});
    }
    if (self.onSizeChange) {
        self.onSizeChange(@{
                            @"width": @(size.width),
                            @"height": @(size.height)
                            });
    }
}

- (void)adViewDidFailToLoadAd:(MPAdView *)view {
    if (self.onAdFailedToLoad) {
        self.onAdFailedToLoad(@{});
    }
}

- (void)willPresentModalViewForAd:(MPAdView *)view {
    if (self.onAdOpened) {
        self.onAdOpened(@{});
    }
}

- (void)didDismissModalViewForAd:(MPAdView *)view {
    if (self.onAdClosed) {
        self.onAdClosed(@{});
    }
}

- (void)willLeaveApplicationFromAd:(MPAdView *)view {
    if (self.onAdLeftApplication) {
        self.onAdLeftApplication(@{});
    }
}

@end
