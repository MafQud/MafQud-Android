package com.mafqud.android.results.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
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
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.other.openDialer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : BaseFragment() {


    private val viewModel: ContactViewModel by viewModels()
    private val args: ContactFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestContactID()
    }

    private fun requestContactID() {
        if (args.caseContact?.id != null) {
            val id = args.caseContact?.id
            if (id != null) {
                lifecycleScope.launchWhenCreated {
                    viewModel
                        .intentChannel
                        .send(ContactIntent.CreateContact(id))
                }
            }
        }
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
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            title = stringResource(id = R.string.contact_another)
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

        stateValue.isContactDoneSuccess?.let { isSuccess ->
            if (isSuccess) {
                requireContext().showToast(stringResource(id = R.string.case_done))
                findNavController().navigate(
                    R.id.action_pop_out_of_graph,
                )
            }
        }

        stateValue.isContactFailedSuccess?.let {
            // TODO complete api call to notify another user
        }

        ContactScreen(
            caseContact = args.caseContact,
            openDialer = { number ->
                requireContext().openDialer(number)
            }, onContactFailed = {
                sendFailedContactIntent()
            }, onContactSuccess = {
                sendDoneContactIntent()
            })

        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBar(
                scaffoldState = scaffoldState
            )
        }

        LoadingDialog(stateValue.isLoading)

    }

    private fun sendFailedContactIntent() {
        findNavController().navigate(R.id.action_contactFragment_to_contactFailedFragment)
        args.caseContact?.let {
            lifecycleScope.launchWhenCreated {
                viewModel
                    .intentChannel
                    .send(ContactIntent.Done(it.id ?: -1))
            }
        }

    }

    private fun sendDoneContactIntent() {
        args.caseContact?.let {
            lifecycleScope.launchWhenCreated {
                viewModel
                    .intentChannel
                    .send(ContactIntent.Done(it.id ?: -1))
            }
        }

    }

}