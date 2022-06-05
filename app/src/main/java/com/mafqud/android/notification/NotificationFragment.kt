package com.mafqud.android.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val viewModel: NotificationViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var userNationalId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        if (activity is HomeActivity) {
            activity.homeBarVisibility(isVisible = false)
        }
       runBlocking {
           userNationalId = dataStoreManager.getNationalId()
       }
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
                    Scaffold(topBar = {
                        TitledAppBar(
                            title = stringResource(id = R.string.notification),
                            onIconClicked = {
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

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(NotificationIntent.GetAllData)
        }
    }

    @Composable
    private fun ListenToChanges() {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateValue.isRefreshing),
            onRefresh = {
                refreshDataIntent()
            },
        ) {

            CircleLoading(stateValue.isLoading)

            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorUi(onTryBtnClicked = {
                    refreshDataIntent()
                }, requireView())
            }
            NotificationScreen(
                data = stateValue.notifications
            ) { notificationType, id ->
                sendMarkNotificationAsReadIntent(id)
                when (notificationType) {
                    is NotificationClickAction.Details -> {}
                    is NotificationClickAction.Failed -> openFailureFragment(notificationType.caseModel)
                    NotificationClickAction.None -> {}
                    is NotificationClickAction.Success -> openSuccessFragment(notificationType.caseModel)
                }
            }
        }
    }

    private fun sendMarkNotificationAsReadIntent(id: Int?) {
        id?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.intentChannel.send(NotificationIntent.MarkNotificationAsRead(id))
            }
        }
    }

    private fun openFailureFragment(
        caseModel: CaseModel,
    ) {
        val action =
            NotificationFragmentDirections.actionNotificationFragmentToFailedFragment()
        action.caseModel = caseModel
        findNavController().navigate(action)

    }

    private fun openSuccessFragment(
        caseModel: CaseModel,
    ) {
        /**
         * if user national id in not empty open results screen .
         */
        if (userNationalId.isNotEmpty()) {
            val actionToResults =
                NotificationFragmentDirections.actionNotificationFragmentToResultsCasesFragment()
            actionToResults.caseModel = caseModel
            findNavController().navigate(actionToResults)

        } else {
            val actionToSuccess =
                NotificationFragmentDirections.actionNotificationFragmentToSuccessLostFragment()
            actionToSuccess.caseModel = caseModel
            findNavController().navigate(actionToSuccess)
        }

    }


    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(NotificationIntent.RefreshData)
        }
    }

}