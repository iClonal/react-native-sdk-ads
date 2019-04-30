#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

#import "MPRewardedVideoReward.h"

#import "RNSdkAdsConstant.h"
#import "RNSdkMoPubConstant.h"
#import "RNSdkMoPubRewarded.h"

@implementation RNSdkMoPubRewarded {
    NSString *_adUnitID;
    NSArray *_testDevices;
//    RCTPromiseResolveBlock _requestAdResolve;
//    RCTPromiseRejectBlock _requestAdReject;
    BOOL hasListeners;
    BOOL requesting;
}

RCT_EXPORT_MODULE(RNSdkAdsRewarded)

- (id) init {
    if (self == [super init]) {
        NSString *adUnitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_MOPUB_REWARDED_ID];
        if (adUnitId != nil) {
            _adUnitID = adUnitId;
        } else {
            _adUnitID = TEST_REWARDED_ADUNIT_ID;
        }
        requesting = NO;
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
             RewardedEventAdLoaded,
             RewardedEventAdFailedToLoad,
             RewardedEventAdOpened,
             RewardedEventVideoStarted,
             RewardedEventRewarded,
             RewardedEventAdClosed,
             RewardedEventAdLeftApplication ];
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
             @"simulatorId": @"" //FIXME:
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

#pragma mark export methods

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
    BOOL ready = [MPRewardedVideo hasAdAvailableForAdUnitID:_adUnitID];
    if (!!ready) return;
    
    if (requesting) return;
    requesting = YES;
    
    [MPRewardedVideo setDelegate:self forAdUnitId:_adUnitID];
    [MPRewardedVideo loadRewardedVideoAdWithAdUnitID:_adUnitID withMediationSettings:nil];
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    BOOL ready = [MPRewardedVideo hasAdAvailableForAdUnitID:_adUnitID];
    if (!!ready) {
        UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
        UIViewController *rootViewController = [keyWindow rootViewController];
        MPRewardedVideoReward *reward = nil;
        NSArray *availableRewards = [MPRewardedVideo availableRewardsForAdUnitID:_adUnitID];
        if (availableRewards.count > 0) {
            reward = [availableRewards objectAtIndex:0];
        }
        [MPRewardedVideo presentRewardedVideoAdForAdUnitID:_adUnitID
                                        fromViewController:rootViewController
                                                withReward:reward
                                                customData:nil];
        resolve(nil);
    }
    else {
        reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    BOOL ready = [MPRewardedVideo hasAdAvailableForAdUnitID:_adUnitID];
    resolve(@[[NSNumber numberWithBool:ready]]);
}

#pragma mark MPRewardedVideoDelegate

- (void)rewardedVideoAdDidLoadForAdUnitID:(NSString *)adUnitID {
    requesting = NO;
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdLoaded body:nil];
    }
}

- (void)rewardedVideoAdDidFailToLoadForAdUnitID:(NSString *)adUnitID error:(NSError *)error {
    requesting = NO;
    if (hasListeners) {
        NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_FAILED_TO_LOAD", error.localizedDescription, error);
        [self sendEventWithName:RewardedEventAdFailedToLoad body:jsError];
    }
}

- (void)rewardedVideoAdDidExpireForAdUnitID:(NSString *)adUnitID {
    
}

- (void)rewardedVideoAdDidFailToPlayForAdUnitID:(NSString *)adUnitID error:(NSError *)error {
    
}

- (void)rewardedVideoAdWillAppearForAdUnitID:(NSString *)adUnitID {
    
}

- (void)rewardedVideoAdDidAppearForAdUnitID:(NSString *)adUnitID {
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdOpened body:nil];
    }
}

- (void)rewardedVideoAdWillDisappearForAdUnitID:(NSString *)adUnitID {
    
}

- (void)rewardedVideoAdDidDisappearForAdUnitID:(NSString *)adUnitID {
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdClosed body:nil];
    }
}

- (void)rewardedVideoAdDidReceiveTapEventForAdUnitID:(NSString *)adUnitID {
    
}

- (void)rewardedVideoAdWillLeaveApplicationForAdUnitID:(NSString *)adUnitID {
    if (hasListeners) {
        [self sendEventWithName:RewardedEventAdLeftApplication body:nil];
    }
}

- (void)rewardedVideoAdShouldRewardForAdUnitID:(NSString *)adUnitID reward:(MPRewardedVideoReward *)reward {
    if (hasListeners) {
        NSString *type = @"Unknown";
        NSNumber *amount = [NSNumber numberWithInt:1];
        if (reward != nil) {
            type = reward.currencyType;
            amount = reward.amount;
        }
        [self sendEventWithName:RewardedEventRewarded body:@{@"type": type, @"amount": amount}];
    }
}

@end
