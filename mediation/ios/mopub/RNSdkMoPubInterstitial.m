#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

#import "RNSdkAdsConstant.h"
#import "RNSdkMoPubConstant.h"
#import "RNSdkMoPubInterstitial.h"

@implementation RNSdkMoPubInterstitial {
    MPInterstitialAdController *_interstitial;
    NSString *_adUnitID;
    NSArray *_testDevices;
    //RCTPromiseResolveBlock _requestAdResolve;
    //RCTPromiseRejectBlock _requestAdReject;
    BOOL hasListeners;
}

RCT_EXPORT_MODULE(RNSdkAdsInterstitial);

- (id)init {
    if (self == [super init]) {
        NSString *adUnitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_MOPUB_INTERSTITIAL_ID];
        if (adUnitId != nil) {
            _adUnitID = adUnitId;
        } else {
            _adUnitID = TEST_INTERSTITIAL_ADUNIT_ID;
        }
    }
    return self;
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[
             InterstitialEventAdLoaded,
             InterstitialEventAdFailedToLoad,
             InterstitialEventAdOpened,
             InterstitialEventAdFailedToOpen,
             InterstitialEventAdClosed,
             InterstitialEventAdLeftApplication ];
}

- (NSDictionary<NSString *,id> *)constantsToExport {
    return @{
             @"simulatorId": @"" //FIXME:
             };
}

- (void)startObserving {
    hasListeners = YES;
}

- (void)stopObserving {
    hasListeners = NO;
}

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID) {
    _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices) {
    _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd) {
    if (_interstitial == nil || !_interstitial.ready) {
        _interstitial = [MPInterstitialAdController interstitialAdControllerForAdUnitId:_adUnitID];
        _interstitial.delegate = self;
        [_interstitial loadAd];
    }
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    if (_interstitial.ready) {
        [_interstitial showFromViewController:[UIApplication sharedApplication].keyWindow.rootViewController];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(@[[NSNumber numberWithBool:_interstitial.ready]]);
}

#pragma mark MPInterstitalAdControllerDelegate

- (void)interstitialDidLoadAd:(MPInterstitialAdController *)interstitial {
    if (hasListeners) {
        [self sendEventWithName:InterstitialEventAdLoaded body:nil];
    }
}

- (void)interstitialDidFailToLoadAd:(MPInterstitialAdController *)interstitial {
    if (hasListeners) {
        [self sendEventWithName:InterstitialEventAdFailedToLoad body:nil];
    }
}

- (void)interstitialDidFailToLoadAd:(MPInterstitialAdController *)interstitial withError:(NSError *)error {
    if (hasListeners) {
        NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
        [self sendEventWithName:InterstitialEventAdFailedToLoad body:jsError];
    }
}

- (void)interstitialWillAppear:(MPInterstitialAdController *)interstitial {

}

- (void)interstitialDidAppear:(MPInterstitialAdController *)interstitial {
    if (hasListeners) {
        [self sendEventWithName:InterstitialEventAdOpened body:nil];
    }
}

- (void)interstitialWillDisappear:(MPInterstitialAdController *)interstitial {
    
}

- (void)interstitialDidDisappear:(MPInterstitialAdController *)interstitial {
    if (hasListeners) {
        [self sendEventWithName:InterstitialEventAdClosed body:nil];
    }
}

- (void)interstitialDidExpire:(MPInterstitialAdController *)interstitial {
    
}

- (void)interstitialDidReceiveTapEvent:(MPInterstitialAdController *)interstitial {
    if (hasListeners) {
        [self sendEventWithName:InterstitialEventAdLeftApplication body:nil];
    }
}

@end
