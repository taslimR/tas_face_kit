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
class TasFaceKitPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private var background_image: Bitmap? = null


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "tas_face_kit")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "findFaces") {
            val path = call.argument<String>("path")
            var face_count = 0
            var confidentFaceCount = 0
            val bmOptions = BitmapFactory.Options()
            bmOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val bitmap = BitmapFactory.decodeFile(path, bmOptions)
//     background_image = convert(bitmap, Bitmap.Config.RGB_565)
            bitmap?.let {
                Log.d("AAP", "Inside bitmap")
//           it.config = Bitmap.Config.RGB_565
                val face_detector = FaceDetector(
                        it.getWidth(), it.getHeight(),
                        MAX_FACES
                )
              var faces: Array<FaceDetector.Face?> = arrayOfNulls(MAX_FACES)
                // The bitmap must be in 565 format (for now).
                face_count = face_detector.findFaces(bitmap, faces)
                Log.d("Face_Detection", "Face Count: $face_count")
                for (i in 0 until face_count) {
                    if (faces[i]!!.confidence() > 0.5) {
                        confidentFaceCount++
                    }
                }
            }
            Log.d("confidentFaceCount", "Count: $confidentFaceCount")
            result.success(confidentFaceCount)
        } else {
            result.notImplemented()
        }
    }

//  private fun convert(bitmap: Bitmap, config: Bitmap.Config): Bitmap? {
//    val min = if (width < height) width else height
//    val convertedBitmap = Bitmap.createBitmap(min, min, config)
//    return convertedBitmap
//  }

    companion object {
        private const val MAX_FACES = 2
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
