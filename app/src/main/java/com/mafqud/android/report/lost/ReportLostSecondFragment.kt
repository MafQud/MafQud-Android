package com.mafqud.android.report.lost

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
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportLostSecondFragment : BaseFragment() {


    private val args: ReportLostSecondFragmentArgs by navArgs()
    private val viewModel: ReportViewModel by viewModels()


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
                    Scaffold(topBar = {
                        TitledAppBar(
                            title = stringResource(id = R.string.report_lost),
                            onIconClicked = {
                                isDialogOpened.value = true
                            }
                        )
                    }, content = {
                        ListenToChanges(isDialogOpened)
                    })
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

        requireActivity().LostScreenTwo(
            govs = state.govs,
            cities = state.cities,
            onGovSelected = {
                requestCities(it)
            },
            onNext = { caseBody ->
                // open uploading screen fragment
                uploadCase(caseBody)
            }, onBack = {
                findNavController().popBackStack()
            }, isDialogOpened = isDialogOpened
        )
    }

    private fun requestCities(it: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportIntent.GetCities(it))
        }
    }

    private fun uploadCase(it: CreateCaseBody) {
        val actonToUploading =
            ReportLostSecondFragmentDirections.actionReportLostSecondFragmentToUploadingImagesFragment(
                args.imagesUrisPicked,
                it.apply {
                    // set the lost case home location
                    location = CreateCaseBody.Location(
                        gov = args.givID.toString(),
                        city = args.cityID.toString(),
                    )
                }
            )
        findNavController().navigate(actonToUploading)
    }
}