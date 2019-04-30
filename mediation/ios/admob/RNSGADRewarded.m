#import "RNSGADRewarded.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

#import "RNSdkAdsConstant.h"
#import "RNSGADCommon.h"

@implementation RNSGADRewarded
{
    NSString *_adUnitID;
    NSArray *_testDevices;
    RCTPromiseResolveBlock _requestAdResolve;
    RCTPromiseRejectBlock _requestAdReject;
    BOOL hasListeners;
    BOOL requesting;
}

- (id)init
{
    if (self == [super init]) {
        NSString* unitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_ADMOB_REWARDED_ID];
        if (!!unitId) {
            _adUnitID = unitId;
        } else {
            _adUnitID = TEST_REWARDED_ADUNIT_ID;
        }

        requesting = NO;
    }
    return self;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

RCT_EXPORT_MODULE(RNSdkAdsRewarded);

- (NSArray<NSString *> *)supportedEvents
{
    return @[
             RewardedEventAdLoaded,
             RewardedEventAdFailedToLoad,
             RewardedEventAdOpened,
             RewardedEventVideoStarted,
             RewardedEventRewarded,
             RewardedEventAdClosed,
             RewardedEventAdLeftApplication ];
}

#pragma mark exported methods

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
    _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices)
{
    _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd) //:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    //_requestAdResolve = resolve;
    //_requestAdReject = reject;
    BOOL ready = [[GADRewardBasedVideoAd sharedInstance] isReady];
    if (!!ready) return;
    
    if (requesting) return;
    requesting = YES;
    
    [GADRewardBasedVideoAd sharedInstance].delegate = self;
    GADRequest *request = [GADRequest request];
    request.testDevices = _testDevices;
    [[GADRewardBasedVideoAd sharedInstance] loadRequest:request
                                           withAdUnitID:_adUnitID];
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if ([[GADRewardBasedVideoAd sharedInstance] isReady]) {
        UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
        UIViewController *rootViewController = [keyWindow rootViewController];
        [[GADRewardBasedVideoAd sharedInstance] presentFromRootViewController:rootViewController];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    BOOL ready = [[GADRewardBasedVideoAd sharedInstance] isReady];
    resolve(@[[NSNumber numberWithBool:ready]]);
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
             @"simulatorId": kGADSimulatorID
             };
}

- (void)startObserving
{
    hasListeners = YES;
}

- (void)stopObserving
{
    hasListeners = NO;
}

#pragma mark GADRewardBasedVideoAdDelegate

- (void)rewardBasedVideoAd:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
   didRewardUserWithReward:(GADAdReward *)reward {
    if (hasListeners) {
        [self sendEventWithName:RewardedEventRewarded body:@{@"type": reward.type, @"amount": reward.amount}];
    }
}

- (void)rewardBasedVideoAdDidReceiveAd:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
{
    requesting = NO;
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdLoaded body:nil];
    }
    //_requestAdResolve(nil);
}

- (void)rewardBasedVideoAdDidOpen:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
{
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdOpened body:nil];
    }
}

- (void)rewardBasedVideoAdDidStartPlaying:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
{
    if (hasListeners) {
        [self sendEventWithName:RewardedEventVideoStarted body:nil];
    }
}

- (void)rewardBasedVideoAdDidClose:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
{
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdClosed body:nil];
    }
}

- (void)rewardBasedVideoAdWillLeaveApplication:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
{
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdLeftApplication body:nil];
    }
}

- (void)rewardBasedVideoAd:(__unused GADRewardBasedVideoAd *)rewardBasedVideoAd
    didFailToLoadWithError:(NSError *)error
{
    requesting = NO;
    if (hasListeners) {
        NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_FAILED_TO_LOAD", error.localizedDescription, error);
        [self sendEventWithName:RewardedEventAdFailedToLoad body:jsError];
    }
    //_requestAdReject(@"E_AD_FAILED_TO_LOAD", error.localizedDescription, error);
}

@end

