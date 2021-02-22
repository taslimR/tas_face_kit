#import "TasFaceKitPlugin.h"
#if __has_include(<tas_face_kit/tas_face_kit-Swift.h>)
#import <tas_face_kit/tas_face_kit-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "tas_face_kit-Swift.h"
#endif

@implementation TasFaceKitPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftTasFaceKitPlugin registerWithRegistrar:registrar];
}
@end
