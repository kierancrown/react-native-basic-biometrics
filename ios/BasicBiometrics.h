
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNBasicBiometricsSpec.h"

@interface BasicBiometrics : NSObject <NativeBasicBiometricsSpec>
#else
#import <React/RCTBridgeModule.h>

@interface BasicBiometrics : NSObject <RCTBridgeModule>
#endif

@end
