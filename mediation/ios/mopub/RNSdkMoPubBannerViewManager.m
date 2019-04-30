#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>
#else
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTEventDispatcher.h"
#endif

#import "RNSdkAdsConstant.h"
#import "RNSdkMoPubConstant.h"
#import "RNSdkMoPubBannerView.h"
#import "RNSdkMoPubBannerViewManager.h"

@implementation RNSdkMoPubBannerViewManager

RCT_EXPORT_MODULE(RNSdkAdsBannerView)

- (UIView *)view {
    return [RNSdkMoPubBannerView new];
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_METHOD(loadBanner:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNSdkMoPubBannerView *> *viewRegistry) {
        RNSdkMoPubBannerView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNSdkMoPubBannerView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNGADBannerView, got: %@", view);
        } else {
            [view loadBanner];
        }
    }];
}

RCT_EXPORT_VIEW_PROPERTY(testDevices, NSArray)
RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLoaded, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdFailedToLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdOpened, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClosed, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLeftApplication, RCTBubblingEventBlock)

RCT_CUSTOM_VIEW_PROPERTY(adSize, NSString, RNSdkMoPubBannerView) {
    //TODO:
}

RCT_CUSTOM_VIEW_PROPERTY(adUnitId, NSString, RNSdkMoPubBannerView) {
    //TODO:
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
             @"simulatorId": @"" //FIXME:
             };
}

@end
