package com.mafqud.android.results.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsCasesFragment : BaseFragment() {

    private val args: ResultsCasesFragmentArgs by navArgs()
    private val viewModel: ResultCasesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestDataIntent()
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
                    val appbarTitle = when (args.caseModel?.caseType) {
                        CaseType.MISSING ->
                            stringResource(id = R.string.title_losts)
                        CaseType.FOUND ->
                            stringResource(id = R.string.title_founds)
                        else -> {
                            ""
                        }
                    }
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        TitledAppBar(
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            title = appbarTitle
                        )
                    }, content = {
                        ListenToChanges(scaffoldState)
                    })
                }
            }
        }
    }


    @Composable
    private fun ListenToChanges(scaffoldState: ScaffoldState) {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateValue.isRefreshing),
            onRefresh = {
                refreshDataIntent()
            },
        ) {

            CircleLoading(stateValue.isLoading)

            ResultsCasesScreen(
                cases = stateValue.cases,
                caseModel = args.caseModel, onNotFoundButton = {
                    openPublishCaseScreen()
                }) {
                openCaseDetails(it)
            }


            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }
        }
    }

    private fun openCaseDetails(caseMatch: CasesMatchesResponse.CaseMatch) {
        val actionToCaseDetails =
            ResultsCasesFragmentDirections.actionResultsCasesFragmentToCaseDetailsFragment(
            )
        actionToCaseDetails.caseModel = args.caseModel
        actionToCaseDetails.caseMatch = caseMatch
        findNavController().navigate(actionToCaseDetails)
    }

    private fun openPublishCaseScreen() {
        val actionToPublish =
            ResultsCasesFragmentDirections.actionResultsCasesFragmentToPublishCaseFragment(
            )
        actionToPublish.caseModel = args.caseModel
        findNavController().navigate(actionToPublish)
    }

    private fun refreshDataIntent() {
        args.caseModel?.caseId?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.intentChannel.send(ResultCaseIntent.Refresh(it))
            }
        }
    }

    private fun requestDataIntent() {
        args.caseModel?.caseId?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.intentChannel.send(ResultCaseIntent.GetMatchesCases(it))
            }
        }

    }

}