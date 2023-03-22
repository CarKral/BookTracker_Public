package com.example.book_tracker.features.feature_search.presentation.util.barcode_scanner

//
//class YourImageAnalyzer : ImageAnalysis.Analyzer {
//    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
//        0 -> FirebaseVisionImageMetadata.ROTATION_0
//        90 -> FirebaseVisionImageMetadata.ROTATION_90
//        180 -> FirebaseVisionImageMetadata.ROTATION_180
//        270 -> FirebaseVisionImageMetadata.ROTATION_270
//        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
//    }
//
//    override fun analyze(imageProxy: ImageProxy) {
//        val mediaImage = imageProxy.image
//        val imageRotation = degreesToFirebaseRotation(degrees)
//        if (mediaImage != null) {
//            val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
//            // Pass image to an ML Kit Vision API
//            // ...
//        }
//    }
//
//}
