package com.mafqud.android.util.camerax

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.AspectRatio.RATIO_16_9
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import com.mafqud.android.R
import com.mafqud.android.util.other.Logger
import com.mafqud.android.util.fileManager.getCurrentDateMilliSec
import com.mafqud.android.util.fileManager.getFileWithName
import com.mafqud.android.util.fileManager.getOutputAppSpecificDirectory


object CameraXUtil {
    private var savedUri: Uri? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var preview: Preview? = null

    // TODO here you can change camera type (front OR back)
// DEFAULT_FRONT_CAMERA or DEFAULT_BACK_CAMERA
//  Select front camera as a default
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraControl: Camera? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    lateinit var cameraExecutor: ExecutorService
    private var flashState = 0
    lateinit var viewFinder: PreviewView


    fun AppCompatActivity.openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        outputDirectory = this.getOutputAppSpecificDirectory()

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            initPreview()

            initImageCapture()

            initImageAnalyzer()

            bindAgain()

            setupZoomAndTapToFocus()

        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupZoomAndTapToFocus() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = cameraControl?.cameraInfo
                    ?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                cameraControl?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(viewFinder.context, listener)

        viewFinder.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                val factory = viewFinder.meteringPointFactory
                val point = factory.createPoint(event.x, event.y)
                val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                    .setAutoCancelDuration(5, TimeUnit.SECONDS)
                    .build()
                cameraControl?.cameraControl?.startFocusAndMetering(action)
            }
            true
        }
    }


    private fun initPreview() {
        preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
    }

    private fun initImageAnalyzer() {
        imageAnalyzer = ImageAnalysis.Builder()
            .build()
            /*.also {
                *//*  it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                      //Log.d(TAG, "Average luminosity: $luma")
                  })*//*
            }*/

    }

    private fun AppCompatActivity.bindAgain() {
        try {
            // Unbind use cases before rebinding
            cameraProvider?.unbindAll()

            // Bind use cases to camera
            cameraControl = cameraProvider?.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )

        } catch (exc: Exception) {
            Log.e("binding_failed", exc.localizedMessage.toString())
        }
    }

    private fun initImageCapture() {
        imageCapture = ImageCapture.Builder()
            .setFlashMode(
                if (flashState == 0) FLASH_MODE_OFF else FLASH_MODE_ON
            )
            // TODO control the taken image options
            //.setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            //.setTargetResolution(Size(1200, 2400))
            .setTargetAspectRatio(RATIO_16_9)
            .build()
    }


    fun closeFlashIfOpened() {
        controlFlash(false)
    }

    fun setupFlash(camera_flash: ImageView) {
        cameraControl?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                if (flashState == 0) {
                    //on
                    flashState = 1
                    controlFlash(true)
                    camera_flash.setImageResource(R.drawable.ic_flash_on)
                } else {
                    //off
                    flashState = 0
                    controlFlash(false)
                    camera_flash.setImageResource(R.drawable.ic_flash_off)
                }
                imageCapture?.flashMode = if (flashState == 0) FLASH_MODE_OFF else FLASH_MODE_ON
            }
        }
    }

    private fun controlFlash(isOn: Boolean) {
        cameraControl?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(isOn)
            }
        }
    }

    fun AppCompatActivity.setupCameraType(camera_flash: ImageView) {
        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        }
        closeFlashIfHasNoFlashUnit(camera_flash)
        openCamera()
    }

    fun closeFlashIfHasNoFlashUnit(camera_flash: ImageView) {
        cameraControl?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                //off
                it.cameraControl.enableTorch(false)
                flashState = 0
                camera_flash.setImageResource(R.drawable.ic_flash_off)
            }
        }
    }

    fun Activity.takePhoto(onUriReturned: (imageUri: String) -> Unit) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = getFileWithName(outputDirectory, getCurrentDateMilliSec())

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Snackbar.make(
                        viewFinder,
                        getString(R.string.error_take_photo),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()

                    Logger.e("Photo_failed", exc.localizedMessage.toString())
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    onUriReturned(savedUri.toString())
                    val msg = "Photo capture succeeded: $savedUri"
                }
            })
    }
}