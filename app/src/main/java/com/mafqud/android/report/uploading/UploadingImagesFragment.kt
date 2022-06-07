package com.mafqud.android.report.uploading

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.R
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.report.ReportActivity
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadingImagesFragment : BaseFragment() {


    private val viewModel: UploadingViewModel by viewModels()
    private val args: UploadingImagesFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadCaseWithImages()
    }

    private fun uploadCaseWithImages() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(
                UploadingIntent.UploadCaseWithImages(
                    createCaseBody = args.caseBody,
                    imagesUrisPicked = args.imagesUrisPicked.map {
                        it.toUri()
                    }
                )
            )
        }
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
                            onIconClicked = {
                                handleConfirmButton()
                            },
                            backgroundColor = MaterialTheme.colors.onPrimary
                        )
                    }, content = {
                        ListenToChanges()
                        BackHandler {
                            handleConfirmButton()
                        }
                    })
                }
            }
        }
    }

    @Composable
    private fun ListenToChanges() {
        val state = viewModel.stateChannel.collectAsState().value
        UploadingScreen(state, onTryUploadingImages = {
            tryUploadingImages()
        }, onTryUploadingCase = {
            tryUploadingCase()

        }, onConfirm = {
            handleConfirmButton()
        })
    }

    private fun handleConfirmButton() {
        when (activity) {
            is HomeActivity -> {
                findNavController().navigate(R.id.action_pop_out_of_report_graph)
            }
            is ReportActivity -> {
                startActivity(
                    Intent(
                        requireContext(),
                        HomeActivity::class.java
                    )
                )
                requireActivity().finish()
            }
        }
    }

    private fun tryUploadingCase() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(UploadingIntent.UploadCaseOnly)
        }
    }

    private fun tryUploadingImages() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(UploadingIntent.UploadImagesOnly)
        }
    }

}