package com.mafqud.android.results.states.failed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.*
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
import com.mafqud.android.ui.compose.IconType
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.HttpErrorType
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FailedFragment : BaseFragment() {


    private val args : FailedFragmentArgs by navArgs()
    private val viewModel: FailedViewModel by viewModels()

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
                        FailedScreen(args.caseModel) {
                            publishCaseIntent()
                        }
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


        if (stateValue.isSuccess) {
            val message = stringResource(id = R.string.puclished)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_pop_out_of_game)
        }

        if (stateValue.networkError != null) {
            // try to get error if == HttpErrorType.BadRequest
            /** close current screen
             *  else:
             *  display error message
             */
            when (stateValue.networkError) {
                is Result.NetworkError.Generic -> {
                    if (stateValue.networkError.type == HttpErrorType.BadRequest) {
                        //close current screen
                        val message = stringResource(id = R.string.puclished_before)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_pop_out_of_game)

                    } else {
                        stateValue.networkError.ShowNetworkErrorSnakeBar(
                            scaffoldState = scaffoldState
                        )
                    }
                }
                else -> {
                    stateValue.networkError.ShowNetworkErrorSnakeBar(
                        scaffoldState = scaffoldState
                    )
                }
            }

        }

        LoadingDialog(stateValue.isLoading)

    }


    private fun publishCaseIntent() {
        args.caseModel?.caseId?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.intentChannel.send(FailedIntent.Publish(it))
            }
        }
    }

}