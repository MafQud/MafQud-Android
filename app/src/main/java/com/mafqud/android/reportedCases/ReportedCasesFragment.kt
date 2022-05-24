package com.mafqud.android.reportedCases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mafqud.android.home.*
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.BoxUi
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

            ReportedCasesScreen(cases = stateValue.cases)

            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }
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