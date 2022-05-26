package com.mafqud.android.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.di.MyServiceInterceptor
import com.mafqud.android.reportedCases.ReportedCasesIntent
import com.mafqud.android.reportedCases.ReportedCasesLimitedViewModel
import com.mafqud.android.reportedCases.ReportedCasesScreen
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private val viewModel: ReportedCasesLimitedViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor


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
                            logOutCurrentUser()

                        })
                }
            }
        }
    }


    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportedCasesIntent.Refresh)
        }
    }


    private fun logOutCurrentUser() {
        lifecycleScope.launch {
            // clear the current user token from interceptor
            myServiceInterceptor.setSessionToken("")
            // clear user data
            dataStoreManager.clearDataStore()
            // restart application
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(ReportedCasesIntent.GetReportedCasesLimited)
        }
    }

}