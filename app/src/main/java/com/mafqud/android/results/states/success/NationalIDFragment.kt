package com.mafqud.android.results.states.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.name
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
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.mapper.CaseContact
import com.mafqud.android.notification.CaseModel
import com.mafqud.android.ui.compose.IconType
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.AndroidEntryPoint
import com.mafqud.android.R

@AndroidEntryPoint
class NationalIDFragment : BaseFragment() {

    private val args: NationalIDFragmentArgs by navArgs()
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

        SuccessScreen(args.caseContact, stateValue.nationalID) { id ->
            // try send national ID
            sendUserInfoIntent(id)
        }
        if (stateValue.isSuccess) {
            clearState()
            openNextScreen()
        }

        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBar(
                scaffoldState = scaffoldState
            )
        }

        LoadingDialog(stateValue.isLoading)

    }

    private fun openNextScreen() {
        val previousFragmentId = findNavController().previousBackStackEntry?.destination?.id

        if (previousFragmentId == R.id.notificationFragment) {
            displayMatchesCases()

        } else if (previousFragmentId == R.id.homeDetailsFragment) {
            openContactScreen()
        }

    }

    private fun openContactScreen() {
        val actionToPublish =
            NationalIDFragmentDirections.actionNationalIdFragmentToContactFragment2(

            )
        actionToPublish.caseContact = args.caseContact
        findNavController().navigate(actionToPublish)
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
            NationalIDFragmentDirections.actionNationalIdToResultsCasesFragment(

            )
        actionToPublish.caseModel = CaseModel(
            caseId = args.caseContact?.id ?: -1,
            caseType = args.caseContact?.caseType ?: CaseType.NONE
        )
        findNavController().navigate(actionToPublish)
    }

}