package com.mafqud.android.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.data.LogoutHelper
import com.mafqud.android.reportedCases.ReportedCasesIntent
import com.mafqud.android.reportedCases.ReportedCasesLimitedViewModel
import com.mafqud.android.reportedCases.ReportedCasesScreen
import com.mafqud.android.reportedCases.ReportedCasesViewState
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private val viewModel: ReportedCasesLimitedViewModel by viewModels()


    @Inject
    lateinit var logoutHelper: LogoutHelper

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
                    val state = viewModel.stateChannel.collectAsState()
                    val stateValue = state.value
                    DisplayUserStateResult(stateValue)
                    MoreScreen(
                        cases = stateValue.cases,
                        onReportedClicked = {
                            findNavController().navigate(R.id.action_moreFragment_to_reportedCasesFragment)

                        }, onAccountClicked = {
                            findNavController().navigate(R.id.action_moreFragment_to_myAccountFragment)

                        }, onSettingClicked = {
                            findNavController().navigate(R.id.action_moreFragment_to_settingFragment)

                        }, onHelpClicked = {
                            findNavController().navigate(R.id.action_moreFragment_to_helpFragment)

                        }, onPhonesClicked = {
                            findNavController().navigate(R.id.action_moreFragment_to_phonesFragment)

                        }, onLogoutClicked = {
                            logOut()

                        }, onFoundCase = {
                            sendFinishCaseIntent(it.id)
                        }, onArchiveCase = {
                            sendArchiveCaseIntent(it.id)
                        })
                }
            }
        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            logoutHelper.logOutUser(requireActivity())
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
            viewModel.intentChannel.send(ReportedCasesIntent.GetReportedCasesLimited)
        }
    }

}