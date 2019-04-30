#import "RNSdkAds.h"

#import "MoPub.h"
#import "RNSdkAdsConstant.h"
#import "RNSdkMoPubConstant.h"

@implementation RNSdkAds

+ (void) initSdk {
    NSString *adUnitId = [[NSBundle mainBundle] objectForInfoDictionaryKey:INFOPLIST_KEY_MOPUB_BANNER_ID];
    if (adUnitId == nil) {
        adUnitId = TEST_BANNER_ADUNIT_ID;
    }
    MPMoPubConfiguration *sdkConfig = [[MPMoPubConfiguration alloc] initWithAdUnitIdForAppInitialization:adUnitId];
    
    //sdkConfig.globalMediationSettings = @[];
#ifdef DEBUG
    sdkConfig.loggingLevel = MPBLogLevelInfo;
#endif
    sdkConfig.allowLegitimateInterest = YES;
    //sdkConfig.additionalNetworks(NSArray of class names);
    //sdkConfig.mediatedNetworkConfigurations(NSMutableDictionary of network configuration);
    
    [[MoPub sharedInstance] initializeSdkWithConfiguration:sdkConfig completion:^{
        NSLog(@"SDK initialization complete");
        // SDK initialization complete. Ready to make ad requests.
    }];
    
}

@end
