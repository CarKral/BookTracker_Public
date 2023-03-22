//package com.example.book_tracker.barcode_scanner
//
//import android.Manifest
//import android.content.Context
//import android.media.MediaScannerConnection
//import android.net.Uri
//import android.os.Build
//import android.webkit.MimeTypeMap
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.core.net.toFile
//import androidx.lifecycle.lifecycleScope
//import com.example.book_tracker.R
//import com.example.book_tracker.presentation.BookTrackerScreens
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionsRequired
//import com.google.accompanist.permissions.rememberMultiplePermissionsState
//import com.google.mlkit.vision.barcode.BarcodeScanning
//import com.google.mlkit.vision.common.InputImage
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import timber.log.Timber
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.concurrent.Executors
//
//private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
//private const val PHOTO_EXTENSION = ".jpg"
//
//sealed class CameraUIAction {
//    object OnCameraClick : CameraUIAction()
//    object OnGalleryViewClick : CameraUIAction()
//    object OnSwitchCameraClick : CameraUIAction()
//}
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun CameraScreen(
//    openScreen: (String) -> Unit,
//
//    ) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val previewView: PreviewView = remember { PreviewView(context) }
//    val cameraSelector: MutableState<CameraSelector> = remember {
//        mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
//    }
//    val permissionState = rememberMultiplePermissionsState(
//        permissions = mutableListOf(
//            Manifest.permission.CAMERA
//        ).apply {
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//        }
//    )
//
//
//    LaunchedEffect(Unit) {
//        permissionState.launchMultiplePermissionRequest()
//    }
//
//    PermissionsRequired(
//        multiplePermissionsState = permissionState,
//        permissionsNotGrantedContent = { /* ... */ },
//        permissionsNotAvailableContent = { /* ... */ }
//    ) {
//
//
//        CameraView(
//            onImageCaptured = { uri, fromGallery ->
//
//                lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
//
//                    Timber.e("CAMERA uri ========== ${uri.toString()}")
//                    openScreen(BookTrackerScreens.SearchScreen.route + "?uri=${uri.toString()}")
//
//                }
//
//            }, onError = { imageCaptureException ->
//
//                Timber.e("CAMERA onError ========== ${imageCaptureException.toString()}")
//
//            })
//    }
//}
//
//@Composable
//fun CameraView(
//    onImageCaptured: (Uri, Boolean) -> Unit,
//    onError: (ImageCaptureException) -> Unit
//) {
//    val context = LocalContext.current
//    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
//    val imageCapture: ImageCapture = remember {
//        ImageCapture.Builder().build()
//    }
//    val galleryLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) onImageCaptured(uri, true)
//    }
//
//    CameraPreviewView(imageCapture, lensFacing) { cameraUIAction ->
//        when (cameraUIAction) {
//            is CameraUIAction.OnCameraClick -> {
//                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
//            }
//            is CameraUIAction.OnSwitchCameraClick -> {
//                lensFacing =
//                    if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
//                    else CameraSelector.LENS_FACING_BACK
//            }
//            is CameraUIAction.OnGalleryViewClick -> {
//                if (true == context.getOutputDirectory().listFiles()?.isNotEmpty()) {
//                    galleryLauncher.launch("image/*")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CameraPreviewView(
//    imageCapture: ImageCapture,
//    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
//    cameraUIAction: (CameraUIAction) -> Unit
//) {
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    val preview = Preview.Builder().build()
//    val cameraSelector = CameraSelector.Builder()
//        .requireLensFacing(lensFacing)
//        .build()
//
//    val previewView = remember { PreviewView(context) }
//    LaunchedEffect(lensFacing) {
//        val cameraProvider = context.getCameraProvider()
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(
//            lifecycleOwner,
//            cameraSelector,
//            preview,
//            imageCapture
//        )
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        AndroidView({ previewView }, modifier = Modifier.fillMaxSize()) {
//
//        }
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth(),
////                .background(Color.Black)
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            CameraControls(cameraUIAction)
//        }
//
//    }
//
//}
//
//suspend fun Context.getCameraProvider(): ProcessCameraProvider {
//    val context = this
//
//    return withContext(Dispatchers.IO) {
//        ProcessCameraProvider.getInstance(context).also {
//            it.addListener(Runnable { }, ContextCompat.getMainExecutor(context))
//        }.get()
//    }
//}
//
//fun ImageCapture.takePicture(
//    context: Context,
//    lensFacing: Int,
//    onImageCaptured: (Uri, Boolean) -> Unit,
//    onError: (ImageCaptureException) -> Unit
//) {
//    val outputDirectory = context.getOutputDirectory()
//    // Create output file to hold the image
//    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
//    val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)
//
//    this.takePicture(
//        outputFileOptions,
//        Executors.newSingleThreadExecutor(),
//        object : ImageCapture.OnImageSavedCallback {
//
//
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
//                // If the folder selected is an external media directory, this is
//                // unnecessary but otherwise other apps will not be able to access our
//                // images unless we scan them using [MediaScannerConnection]
//                val mimeType = MimeTypeMap.getSingleton()
//                    .getMimeTypeFromExtension(savedUri.toFile().extension)
//                MediaScannerConnection.scanFile(
//                    context,
//                    arrayOf(savedUri.toFile().absolutePath),
//                    arrayOf(mimeType)
//                ) { _, uri ->
//
//                }
//                onImageCaptured(savedUri, false)
////
////                val msg = "Photo capture succeeded: $savedUri"
////                Timber.d(msg)
////                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//            }
//
//
//            override fun onError(exception: ImageCaptureException) {
//                onError(exception)
//            }
//        })
//}
//
//@Composable
//fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit) {
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Black)
//            .padding(vertical = 16.dp, horizontal = 32.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        CameraControl(
//            R.drawable.ic_baseline_flip_camera_android_24,
//            modifier = Modifier.size(32.dp),
//            onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
//        )
//
//        CameraControl(
//            R.drawable.ic_baseline_lens_24,
//            modifier = Modifier
//                .size(64.dp)
//                .padding(1.dp)
//                .border(1.dp, Color.White, CircleShape),
//            onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
//        )
//
//        CameraControl(
//            R.drawable.ic_baseline_photo_library_24,
//            modifier = Modifier.size(32.dp),
//            onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) }
//        )
//
//    }
//}
//
//@Composable
//fun CameraControl(
//    iconResource: Int,
////    contentDescId: Int,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//
//
//    IconButton(
//        onClick = onClick,
//        modifier = modifier
//    ) {
//        Icon(
//            painter = painterResource(id = iconResource),
//            contentDescription = "",
//            modifier = modifier,
//            tint = Color.White
//        )
//    }
//
//}
//
//fun getOutputFileOptions(
//    lensFacing: Int,
//    photoFile: File
//): ImageCapture.OutputFileOptions {
//
//    // Setup image capture metadata
//    val metadata = ImageCapture.Metadata().apply {
//        // Mirror image when using the front camera
//        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
//    }
//    // Create output options object which contains file + metadata
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
//        .setMetadata(metadata)
//        .build()
//
//    return outputOptions
//}
//
//fun createFile(baseFolder: File, format: String, extension: String) =
//    File(
//        baseFolder, SimpleDateFormat(format, Locale.getDefault())
//            .format(System.currentTimeMillis()) + extension
//    )
//
//
//fun Context.getOutputDirectory(): File {
//    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
//        File(it, "BookTrackerApp").apply { mkdirs() }
//    }
//    return if (mediaDir != null && mediaDir.exists())
//        mediaDir else this.filesDir
//}
//
//
//fun getISBNFromURI(context: Context, uri: String, onSuccess: (String) -> Unit) {
//    val image: InputImage
//
//    try {
//        image = InputImage.fromFilePath(context, Uri.parse(uri))
//        BarcodeScanning.getClient(options).process(image).addOnSuccessListener { barcodes ->
//            for (barcode in barcodes) {
//                Timber.i("BARCODE --------- ${barcode.rawValue}")
//
//                onSuccess(barcode.rawValue.toString())
//            }
//        }
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//}
