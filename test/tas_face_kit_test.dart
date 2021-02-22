import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:tas_face_kit/tas_face_kit.dart';

void main() {
  const MethodChannel channel = MethodChannel('tas_face_kit');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await TasFaceKit.platformVersion, '42');
  });
}
