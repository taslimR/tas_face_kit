import 'dart:async';

import 'package:flutter/services.dart';

class TasFaceKit {
  static const MethodChannel _channel = const MethodChannel('tas_face_kit');

  static Future<int> detectFaces(String imagePath) async {
    final int version =
        await _channel.invokeMethod('findFaces', {"path": imagePath});
    return version;
  }
}
