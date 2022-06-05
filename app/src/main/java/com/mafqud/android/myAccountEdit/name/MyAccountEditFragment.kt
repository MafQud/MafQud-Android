package com.mafqud.android.myAccountEdit.name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountEditFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestUserName()
    }

    private fun requestUserName() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(AccountIntent.CurrentName)
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
                            title = stringResource(id = R.string.my_account),
                            onIconClicked = {
                                findNavController().popBackStack()
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
        // ui state
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value


        if (stateValue.isSuccess) {
            val message = stringResource(id = R.string.name_updated)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        stateValue.networkError?.ShowNetworkErrorSnakeBar(
            scaffoldState = scaffoldState
        )

        EditAccountScreen(
            currentName = stateValue.userName,
            onSaveClicked = {
                updateNameIntent(it)
            })
        LoadingDialog(stateValue.isLoading)
    }

    private fun updateNameIntent(it: String) {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(AccountIntent.ChangeName(userName = it))
        }
    }

}