package com.mafqud.android.results.states.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.IconType
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessReportingFragment : BaseFragment() {

    private val args: SuccessReportingFragmentArgs by navArgs()
    private val viewModel: SuccessResultsViewModel by viewModels()

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
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        TitledAppBar(
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            backgroundColor = MaterialTheme.colors.onPrimary,
                            iconType = IconType.CLOSE
                        )
                    }, content = {
                        ListenToChanges(scaffoldState)
                    })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ListenToChanges(scaffoldState: ScaffoldState) {
        // ui state
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value



        SuccessScreen(args.caseModel, stateValue.nationalID) { id ->
            // try send national ID
            sendUserInfoIntent(id)
        }
        if (stateValue.isSuccess) {
            clearState()
            displayMatchesCases()
        }

        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBar(
                scaffoldState = scaffoldState
            )
        }

        LoadingDialog(stateValue.isLoading)

    }

    private fun sendUserInfoIntent(nationalId: String) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(
                SuccessResultsIntent.AddNationalId(
                    nationalId = nationalId,
                )
            )
        }
    }

    private fun clearState() {
        viewModel.intentChannel.trySend(
            SuccessResultsIntent.ClearState
        )
    }

    private fun displayMatchesCases() {
        val actionToPublish =
            SuccessReportingFragmentDirections.actionSuccessLostFragmentToResultsCasesFragment(
            )
        actionToPublish.caseModel = args.caseModel
        findNavController().navigate(actionToPublish)
    }

}