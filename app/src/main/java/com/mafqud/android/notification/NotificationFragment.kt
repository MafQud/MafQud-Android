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
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.results.states.FailureType
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        if (activity is HomeActivity) {
            activity.homeBarVisibility(isVisible = false)
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
                data = stateValue.notifications,
                onNotificationClicked = { notificationData, notificationType ->
                    when (notificationType) {
                        NotificationType.SUCCESS_FINDING_LOST,
                        NotificationType.SUCCESS_FINDING_FOUND -> openSuccessFragment(
                            notificationData,
                            notificationType
                        )
                        NotificationType.FAILED_LOST,
                        NotificationType.FAILED_FOUND,
                        NotificationType.OTHER,
                        -> openFailureFragment(
                            notificationData,
                            notificationType
                        )
                    }
                })
        }
    }

    private fun openFailureFragment(
        notificationData: NotificationResponse.Data,
        notificationType: NotificationType
    ) {
        when (notificationType) {
            NotificationType.FAILED_LOST -> {
                val action =
                    NotificationFragmentDirections.actionNotificationFragmentToFailedFragment(
                        FailureType.LOST
                    )
                findNavController().navigate(action)
            }
            NotificationType.FAILED_FOUND -> {
                val action =
                    NotificationFragmentDirections.actionNotificationFragmentToFailedFragment(
                        FailureType.FOUND
                    )
                findNavController().navigate(action)
            }
            NotificationType.OTHER -> {
                findNavController().navigate(R.id.action_notificationFragment_to_failedFragment)
            }
            else -> {
                findNavController().navigate(R.id.action_notificationFragment_to_failedFragment)
            }
        }

    }

    private fun openSuccessFragment(
        notificationData: NotificationResponse.Data,
        notificationType: NotificationType
    ) {
        val actionToSuccess =
            NotificationFragmentDirections.actionNotificationFragmentToSuccessLostFragment(
                notificationType
            )
        findNavController().navigate(actionToSuccess)
    }


    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(NotificationIntent.RefreshData)
        }
    }

}