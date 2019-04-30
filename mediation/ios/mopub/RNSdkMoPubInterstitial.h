#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#else
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"
#endif

#import "MPInterstitialAdController.h"

@interface RNSdkMoPubInterstitial : RCTEventEmitter <MPInterstitialAdControllerDelegate>

@end
