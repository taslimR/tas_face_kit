package com.example.tas_face_kit

import android.graphics.*
import android.graphics.Color.convert
import android.media.FaceDetector
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** TasFaceKitPlugin */
class TasFaceKitPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private var background_image: Bitmap? = null
  private lateinit var faces: Array<FaceDetector.Face?>
  private var face_count = 0

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "tas_face_kit")
    channel.setMethodCallHandler(this)
  }

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
      if (call.method == "findFaces") {
        val path = call.argument<String>("path")

        val bmOptions = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(path, bmOptions)
     background_image = convert(bitmap, bitmap.width, bitmap.height, Bitmap.Config.RGB_565)
        bitmap?.let {
        Log.d("AAP", "Inside bitmap")
           it.config = Bitmap.Config.RGB_565
        val face_detector = FaceDetector(
                it.getWidth(), it.getHeight(),
                MAX_FACES
        )
        faces = arrayOfNulls(MAX_FACES)
        // The bitmap must be in 565 format (for now).
        face_count = face_detector.findFaces(background_image, faces)
        Log.d("Face_Detection", "Face Count: $face_count")
      }

      result.success(face_count)
    } else {
      result.notImplemented()
    }
  }

  private fun convert(bitmap: Bitmap, width: Int, height: Int, config: Bitmap.Config): Bitmap? {
    val min = if (width < height) width else height
    val convertedBitmap = Bitmap.createBitmap(min, min, config)
    return convertedBitmap
  }

  companion object {
    private const val MAX_FACES = 10
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
