package com.mafqud.android.report.found

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.report.lost.ReportIntent
import com.mafqud.android.report.lost.ReportViewModel
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.openGalleryForMulti
import com.mafqud.android.util.permission.CAMERA_READ_FILES__PERMISSIONS
import com.mafqud.android.util.permission.PermissionAccessType
import com.mafqud.android.util.permission.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportFoundFragment : BaseFragment() {

    private val viewModel: ReportViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    var bottomSheetState: ModalBottomSheetState? = null
    var bottomSheetScope: CoroutineScope? = null

    init {
        PermissionManager.from(
            this, CAMERA_READ_FILES__PERMISSIONS,
            PermissionAccessType.CAMERA

        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideHomeBar()
        return ComposeView(requireContext()).apply {
            // @ref https://developer.android.com/jetpack/compose/interop/interop-apis#composition-strategy
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                MafQudTheme {
                    bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                    bottomSheetScope = rememberCoroutineScope()
                    val color =
                        androidx.compose.material3.MaterialTheme.colorScheme.onTertiaryContainer.copy(
                            0.2f
                        )
                    ModalBottomSheetLayout(
                        sheetShape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .background(color)
                            .padding(bottom = if (bottomSheetState!!.isVisible) 12.dp else 0.dp),
                        scrimColor = color,
                        sheetState = bottomSheetState!!,
                        sheetContent = {
                            val galleryLauncher =
                                rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                                    val imagesUri = it
                                    val mImageUri = imagesUri.toList()
                                    sendImagesIntent(mImageUri)
                                }

                            //sheetContent
                            BoxUi() {
                                FilterContent(
                                    bottomSheetScope,
                                    bottomSheetState,
                                    onCameraClicked = {
                                        openCamera()
                                    },
                                    onGalleryClicked = {
                                        openGallery(galleryLauncher)
                                    })
                            }
                        },
                    ) {
                        MyScaffold()
                    }
                }
            }
            customBackStackButton()
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun customBackStackButton() {
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    handleBack()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun handleBack() {
        bottomSheetState?.let {
            if (it.isVisible) {
                bottomSheetScope?.launch {
                    it.hide()
                }
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun openCamera() {
        findNavController().navigate(R.id.action_reportFoundFragment_to_cameraFragment)
        val currentActivity = activity
        if (currentActivity is HomeActivity) {
            currentActivity.bottomBarNavigationVisibility(isVisible = false)
        }
    }

    override fun onResume() {
        super.onResume()
        val currentActivity = activity
        if (currentActivity is HomeActivity) {
            currentActivity.bottomBarNavigationVisibility(isVisible = true)
        }
    }

    @Composable
    private fun MyScaffold() {
        Scaffold(topBar = {
            TitledAppBar(
                title = stringResource(id = R.string.report_found),
                onBackClicked = {
                    handleBack()
                }
            )
        }, content = {
            ListenToChanges()
        })
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ListenToChanges() {
        val mState = viewModel.stateChannel.collectAsState().value

        FoundScreen(
            pickedImages = mState.imagesUri,
            openGalleryClicked = {
                // open bottom sheet
                PermissionManager.requestPermission(onDenied = {
                    /* requireContext().showToast(getString(R.string.permission_read_denied))*/
                }, onGranted = {
                    bottomSheetScope?.launch {
                        bottomSheetState?.show()
                    }
                })

            }, onNextClicked = {
                findNavController().navigate(R.id.action_reportFoundFragment_to_reportfoundSecondFragment)
            }, onCloseClicked = {
                removeImageUri(it)
            }
        )

    }

    private fun removeImageUri(uri: Uri) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportIntent.RemoveImageUri(uri))
        }
    }

    private fun sendImagesIntent(mImageUri: List<Uri>) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportIntent.SaveImagesUri(mImageUri))
        }
    }


    private fun openGallery(galleryLauncher: ActivityResultLauncher<String>) {
        requireActivity().openGalleryForMulti(galleryLauncher)
    }

}