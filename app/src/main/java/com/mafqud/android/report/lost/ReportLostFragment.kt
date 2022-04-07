package com.mafqud.android.report.lost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.openGalleryForMulti
import com.mafqud.android.util.permission.PermissionAccessType
import com.mafqud.android.util.permission.PermissionManager
import com.mafqud.android.util.permission.READ_FILES__PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportLostFragment : BaseFragment() {


    private val viewModel: ReportViewModel by viewModels()


    init {
        PermissionManager.from(
            this, READ_FILES__PERMISSIONS,
            PermissionAccessType.READ

        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    Scaffold(topBar = {
                        TitledAppBar(
                            title = stringResource(id = R.string.report_lost),
                            onBackClicked = {
                                findNavController().popBackStack()
                            }
                        )
                    }, content = {
                        ListenToChanges()
                    })
                }
            }
        }
    }

    @Composable
    private fun ListenToChanges() {
        val state = viewModel.stateChannel.collectAsState().value
        /*val pickedImages = remember {
            mutableStateListOf<Uri>()
        }*/
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                val imagesUri = it
                val mImageUri = imagesUri.toList()
                sendImagesIntent(mImageUri)
                //pickedImages.addAll(mImageUri)
            }
        LostScreen(
            pickedImages = state.imagesUri,
            //mPickedImagesUris = pickedImagesUris,
            openGalleryClicked = {
                openGallery(galleryLauncher)
            }, onNextClicked = {
                findNavController().navigate(R.id.action_reportLostFragment_to_reportLostSecondFragment)
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
        PermissionManager.requestPermission(onDenied = {
            /* requireContext().showToast(getString(R.string.permission_read_denied))*/
        }, onGranted = {
            requireActivity().openGalleryForMulti(galleryLauncher)
        })

    }
}