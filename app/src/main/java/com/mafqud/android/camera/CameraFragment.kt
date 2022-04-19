package com.mafqud.android.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.sharp.Cameraswitch
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.mafqud.android.R
import com.mafqud.android.ui.compose.CameraAppBar
import com.mafqud.android.ui.compose.PhotoDialog
import com.mafqud.android.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class CameraFragment : Fragment() {

    lateinit var cameraExecutor: ExecutorService

    companion object {
        val REQUEST_KEY = "FragmentCamera_REQUEST_KEY"
    }

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
                    val pickedImages = remember {
                        mutableStateListOf<Uri>()
                    }

                    Scaffold(
                        topBar = {
                            CameraAppBar(
                                onBackClicked = {
                                    findNavController().popBackStack()
                                },
                                onDoneClicked = {
                                    val imagesString = pickedImages.map {
                                        it.toString()
                                    }.toTypedArray()
                                    // Step 3. Set a result
                                    setFragmentResult(
                                        REQUEST_KEY,
                                        bundleOf("data" to imagesString)
                                    )

                                    // Step 4. Go back to Fragment A
                                    findNavController().navigateUp()
                                }
                            )
                        },
                        content = {

                            val configuration = LocalConfiguration.current
                            val screenHeight = configuration.screenHeightDp.dp - 200.dp
                            val screenWidth = configuration.screenWidthDp.dp

                            BoxUi(modifier = Modifier.fillMaxSize()) {
                                CameraScreen(scaffoldState, screenHeight, screenWidth, pickedImages)
                                BoxUi(
                                    Modifier
                                        .padding(
                                            16.dp
                                        )
                                        .align(Alignment.TopEnd)
                                ) {
                                    PickedImages(pickedImages)
                                }
                            }
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
    private fun PickedImages(pickedImages: SnapshotStateList<Uri>) {
        if (pickedImages.isNotEmpty()) {
            BadgedBox(badge = {
                Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                    TextUi(
                        pickedImages.size.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }) {
                //display last picked image
                PickedPhoto(pickedImages.last())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    private fun PickedPhoto(
        imagesUri: Uri
    ) {
        CardUi(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    CircleShape
                )
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                }, elevation = 2.dp
        ) {
            ImageUi(
                painter = rememberImagePainter(imagesUri),
                modifier = Modifier.fillMaxSize()
            )
        }

    }


    @Composable
    private fun CameraScreen(
        scaffoldState: ScaffoldState,
        height: Dp,
        screenWidth: Dp,
        pickedImages: SnapshotStateList<Uri>
    ) {
        val scope = rememberCoroutineScope()
        val isOpened = remember {
            mutableStateOf(false)
        }
        val pickedImage = remember {
            mutableStateOf("".toUri())
        }

        PhotoDialog(
            isOpened = isOpened,
            height = height,
            width = screenWidth,
            imageUri = pickedImage.value,
            onConfirmClicked = {
                pickedImages.add(pickedImage.value)
            }, onCancelClicked = {
                pickedImage.value = "".toUri()
            }
        )

        if (!::cameraExecutor.isInitialized) {
            cameraExecutor = Executors.newSingleThreadExecutor()
        }

        CameraView(onImageCaptured = { uri, fromGallery ->
            pickedImage.value = uri
            isOpened.value = true

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
            ImageCapture.Builder()
                // TODO control the taken image options
                //.setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                //.setTargetResolution(Size(1200, 2400))
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
        }
        imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF


        CameraPreviewView(
            imageCapture,
            lensFacing
        ) { cameraUIAction ->
            when (cameraUIAction) {
                is CameraUIAction.OnCameraClick -> {
                    imageCapture.takePicture(
                        context,
                        lensFacing,
                        cameraExecutor,
                        onImageCaptured,
                        onError
                    )
                }
                is CameraUIAction.OnSwitchCameraClick -> {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                }
                is CameraUIAction.OnFlashClick -> {
                    imageCapture.flashMode = when (cameraUIAction.flashState) {
                        FlashState.Auto -> ImageCapture.FLASH_MODE_AUTO
                        FlashState.Off -> ImageCapture.FLASH_MODE_OFF
                        FlashState.On -> ImageCapture.FLASH_TYPE_ONE_SHOT_FLASH
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
            BoxUi(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
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
        data class OnFlashClick(val flashState: FlashState) : CameraUIAction()
        object OnSwitchCameraClick : CameraUIAction()
    }

    sealed class FlashState {
        object On : FlashState()
        object Off : FlashState()
        object Auto : FlashState()
    }

    @Composable
    @androidx.compose.ui.tooling.preview.Preview
    fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit = {}) {
        val flashState = remember {
            mutableStateOf<FlashState>(FlashState.Off)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CameraControl(
                Icons.Sharp.Cameraswitch,
                R.string.flip,
                modifier = Modifier
                    .size(50.dp)
                    .padding(2.dp),
                onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
            )

            CameraControl(
                Icons.Sharp.Lens,
                R.string.camera,
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp)
                    .border(1.dp, Color.White, CircleShape),
                onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
            )

            val currentFlashIcon = when (flashState.value) {
                FlashState.Auto -> Icons.Filled.FlashAuto
                FlashState.Off -> Icons.Filled.FlashOff
                FlashState.On -> Icons.Filled.FlashOn

            }
            CameraControl(
                currentFlashIcon,
                R.string.flash,
                modifier = Modifier
                    .size(50.dp)
                    .padding(2.dp),
                onClick = {
                    flashState.value = when (flashState.value) {
                        FlashState.Off -> FlashState.On
                        FlashState.On -> FlashState.Auto
                        FlashState.Auto -> FlashState.Off
                    }
                    cameraUIAction(
                        CameraUIAction.OnFlashClick(
                            flashState.value
                        )
                    )
                }
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


        BoxUi(
            modifier = modifier
                .clip(
                    RoundedCornerShape(50)
                )
                .clickable {
                    onClick()
                }
        ) {
            IconUi(
                imageVector,
                contentDescription = stringResource(id = contentDescId),
                modifier = modifier,
                tint = Color.White
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}