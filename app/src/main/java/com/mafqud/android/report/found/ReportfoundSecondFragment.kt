package com.mafqud.android.report.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.report.lost.ReportIntent
import com.mafqud.android.report.lost.ReportLostSecondFragmentArgs
import com.mafqud.android.report.lost.ReportLostSecondFragmentDirections
import com.mafqud.android.report.lost.ReportViewModel
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportfoundSecondFragment : BaseFragment() {


    private val viewModel: ReportViewModel by viewModels()
    private val args: ReportfoundSecondFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestGovsList()
    }

    private fun requestGovsList() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportIntent.GetGovs)
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
                    val isDialogOpened = remember {
                        mutableStateOf(false)
                    }
                    Scaffold(
                        topBar = {
                            TitledAppBar(
                                title = stringResource(id = R.string.report_found),
                                onIconClicked = {
                                    isDialogOpened.value = true
                                }
                            )
                        },
                        content = {
                            ListenToChanges(isDialogOpened)
                        },
                    )
                    BackHandler {
                        isDialogOpened.value = true
                    }
                }

            }
        }
    }


    @Composable
    private fun ListenToChanges(isDialogOpened: MutableState<Boolean>) {
        val state = viewModel.stateChannel.collectAsState().value

        requireActivity().FoundScreenTwo(govs = state.govs,
            cities = state.cities,
            onGovSelected = {
                requestCities(it)
            }, onBack = {
                findNavController().popBackStack()

            }, isDialogOpened = isDialogOpened,
            onNext = { caseBody ->
                // open uploading screen fragment
                uploadCase(caseBody)
            }
        )
    }

    private fun uploadCase(it: CreateCaseBody) {
        val actonToUploading =
            ReportfoundSecondFragmentDirections.actionReportfoundSecondFragmentToUploadingImagesFragment(
                args.imagesUrisPicked,
                it.apply {
                    // set the found case home location
                    location = CreateCaseBody.Location(
                        gov = args.givID.toString(),
                        city = args.cityID.toString(),
                    )
                }
            )
        findNavController().navigate(actonToUploading)
    }

    private fun requestCities(it: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportIntent.GetCities(it))
        }
    }
}