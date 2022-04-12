package com.mafqud.android.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.FlipCameraAndroid
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material.icons.sharp.PhotoLibrary
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.ui.compose.CameraAppBar
import com.mafqud.android.ui.theme.IconUi
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class CameraFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // @ref https://developer.android.com/jetpack/compose/interop/interop-apis#composition-strategy
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                MafQudTheme {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        topBar = {
                            CameraAppBar(
                                onBackClicked = {
                                    findNavController().popBackStack()
                                },
                                onDoneClicked = {

                                }
                            )
                        },
                        content = {
                            CameraScreen(scaffoldState)
                        },
                        scaffoldState = scaffoldState,
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                    )

                }
            }
        }
    }

    @Composable
    private fun CameraScreen(scaffoldState: ScaffoldState) {
        val scope = rememberCoroutineScope()
        CameraView(onImageCaptured = { uri, fromGallery ->
            Log.d("TAG", "Image Uri Captured from Camera View")
            //Todo : use the uri as needed
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(uri.path.toString())
            }
        }, onError = { imageCaptureException ->
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("An error occurred while trying to take a picture")
            }
        })
    }


    @Composable
    fun CameraView(
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {

        val context = LocalContext.current
        var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
        val imageCapture: ImageCapture = remember {
            ImageCapture.Builder().build()
        }
        val galleryLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) onImageCaptured(uri, true)
        }

        CameraPreviewView(
            imageCapture,
            lensFacing
        ) { cameraUIAction ->
            when (cameraUIAction) {
                is CameraUIAction.OnCameraClick -> {
                    imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
                }
                is CameraUIAction.OnSwitchCameraClick -> {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                }
                is CameraUIAction.OnGalleryViewClick -> {
                    if (true == context.getOutputDirectory().listFiles()?.isNotEmpty()) {
                        galleryLauncher.launch("image/*")
                    }
                }
            }
        }
    }


    @SuppressLint("RestrictedApi")
    @Composable
    private fun CameraPreviewView(
        imageCapture: ImageCapture,
        lensFacing: Int = CameraSelector.LENS_FACING_BACK,
        cameraUIAction: (CameraUIAction) -> Unit
    ) {

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        val previewView = remember { PreviewView(context) }
        LaunchedEffect(lensFacing) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize()) {

            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Bottom
            ) {
                CameraControls(cameraUIAction)
            }

        }
    }

    suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this))
            }
        }

    sealed class CameraUIAction {
        object OnCameraClick : CameraUIAction()
        object OnGalleryViewClick : CameraUIAction()
        object OnSwitchCameraClick : CameraUIAction()
    }

    @Composable
    fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CameraControl(
                Icons.Sharp.FlipCameraAndroid,
                R.string.phones,
                modifier = Modifier.size(40.dp),
                onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
            )

            CameraControl(
                Icons.Sharp.Lens,
                R.string.phones,
                modifier = Modifier
                    .size(64.dp)
                    .padding(1.dp)
                    .border(1.dp, Color.White, CircleShape),
                onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
            )

            CameraControl(
                Icons.Sharp.PhotoLibrary,
                R.string.phones,
                modifier = Modifier.size(40.dp),
                onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) }
            )

        }
    }


    @Composable
    fun CameraControl(
        imageVector: ImageVector,
        contentDescId: Int,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {


        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            IconUi(
                imageVector,
                contentDescription = stringResource(id = contentDescId),
                modifier = modifier,
                tint = Color.White
            )
        }

    }

}