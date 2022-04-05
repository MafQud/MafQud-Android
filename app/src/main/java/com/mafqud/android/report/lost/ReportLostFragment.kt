package com.mafqud.android.report.lost

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
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

    private val PICKED_IMAGE_SIZE = "pick_size"


    init {
        PermissionManager.from(
            this, READ_FILES__PERMISSIONS,
            PermissionAccessType.READ

        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val size = savedInstanceState?.getInt(PICKED_IMAGE_SIZE, 0) ?: 0
        Log.i("onCreateView: ", size.toString())
        //pickedImagesUris = mutableStateOf(size)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Log.i("onCreateView: pickedImagesUris.value ", pickedImagesUris.value.toString())
        //outState.putInt(PICKED_IMAGE_SIZE, pickedImagesUris.value)
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
        val pickedImages = remember {
            mutableStateListOf<Uri>()
        }
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                val imagesUri = it
                val mImageUri = imagesUri.toList()
                pickedImages.addAll(mImageUri)
                //pickedImagesUris.value = pickedImages.size
                //Log.i("pickedImages.size: ", pickedImagesUris.value.toString())
            }
        LostScreen(
            pickedImages = pickedImages,
            //mPickedImagesUris = pickedImagesUris,
            openGalleryClicked = {
                openGallery(galleryLauncher)
            },
        )

    }


    private fun openGallery(galleryLauncher: ActivityResultLauncher<String>) {
        PermissionManager.requestPermission(onDenied = {
            /* requireContext().showToast(getString(R.string.permission_read_denied))*/
        }, onGranted = {
            requireActivity().openGalleryForMulti(galleryLauncher)
        })

    }
}