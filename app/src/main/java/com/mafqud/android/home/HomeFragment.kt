package com.mafqud.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.HomeAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {


    private val viewModel: HomeViewModel by viewModels()

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
                    val userName = remember {
                        mutableStateOf("")
                    }
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        HomeAppBar(
                            userName = userName.value,
                            onNotificationClicked = {
                                findNavController().navigate(R.id.actionToNotificationGraph)
                            }
                        )
                    }, content = {
                        ListenToChanges(userName, scaffoldState)
                    })
                }
            }
        }
    }

    @Composable
    private fun ListenToChanges(userName: MutableState<String>, scaffoldState: ScaffoldState) {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateValue.isRefreshing),
            onRefresh = {
                refreshDataIntent()
            },
        ) {

            if (stateValue.userName != null) {
                userName.value = stateValue.userName
            }
            CircleLoading(stateValue.isLoading)

            HomeScreen(cases = stateValue.cases,
                onTapClicked = {
                    requestCasesIntent(it)
                })

            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }
        }
    }

    private fun requestCasesIntent(it: CasesType) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(HomeIntent.GetCases(it))
        }
    }

    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(HomeIntent.Refresh)
        }
    }

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(HomeIntent.GetCases(CasesType.ALL))
        }
    }

}