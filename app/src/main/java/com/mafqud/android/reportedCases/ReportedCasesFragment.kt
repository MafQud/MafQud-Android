package com.mafqud.android.reportedCases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.navigation.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportedCasesFragment : BaseFragment() {

    private val viewModel: ReportedCasesViewModel by viewModels()

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
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        TitledAppBar(
                            title = stringResource(id = R.string.reported_people),
                            onIconClicked = {
                                findNavController().navigateUp()
                            }
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

            DisplayUserStateResult(stateValue)
            ReportedCasesScreen(cases = stateValue.cases,
                onFoundCase = {
                    sendFinishCaseIntent(it.id)
                }, onArchiveCase = {
                    sendArchiveCaseIntent(it.id)
                })

            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }
        }
    }

    @Composable
    private fun DisplayUserStateResult(stateValue: ReportedCasesViewState) {
        if (stateValue.isArchived != null) {
            requireContext().showToast(stringResource(id = R.string.archive_state))
        }

        if (stateValue.isFinished != null) {
            requireContext().showToast(stringResource(id = R.string.success_to_found))
        }
    }

    private fun sendArchiveCaseIntent(id: Int?) {
        id?.let {
            viewModel.intentChannel.trySend(ReportedCasesIntent.ArchiveCase(id))
        }
    }

    private fun sendFinishCaseIntent(id: Int?) {
        id?.let {
            viewModel.intentChannel.trySend(ReportedCasesIntent.FinishCase(id))
        }
    }

    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportedCasesIntent.Refresh)
        }
    }

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportedCasesIntent.GetReportedCases)
        }
    }

}