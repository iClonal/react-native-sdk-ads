
#import "RNSdkAds.h"
#import <GoogleMobileAds/GoogleMobileAds.h>

@implementation RNSdkAds

+ (void) initSdk {
    [[GADMobileAds sharedInstance] startWithCompletionHandler:nil];
}

@end
