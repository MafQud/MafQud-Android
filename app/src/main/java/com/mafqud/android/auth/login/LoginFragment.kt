package com.mafqud.android.auth.login

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
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBarUi
import com.mafqud.android.util.other.hideKeypad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

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
                    LoginUi()
                    ListenToChanges()
                }
            }
        }
    }

    @Composable
    private fun LoginUi() {
        LoginScreen(onBackPressed = {
            findNavController().popBackStack()
        }, onNextPressed = {
            // login after done clicked in keypad
            loginIntent(phone = it.phone, password = it.password)
        })
    }

    private fun loginIntent(
        phone: String,
        password: String,
    ) {
        requireContext().hideKeypad(requireView())
        lifecycleScope.launch {
            viewModel.intentChannel.send(
                LoginIntent.PhoneLogin(
                    phone = phone,
                    password = password
                )
            )
        }
    }

    @Composable
    private fun ListenToChanges() {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value


        LoadingDialog(stateValue.isLoading)

        if (stateValue.isSuccess && stateValue.data != null) {
            requireActivity().apply {
                openHomeActivity()
            }
        }
        if (stateValue.networkError != null) {
            stateValue.networkError.ShowNetworkErrorSnakeBarUi(
                view = requireView()
            )

        }
    }

    private fun openHomeActivity() {
        startActivity(
            Intent(
                requireContext(), HomeActivity::class.java
            )
        )
        requireActivity().finish()
    }

}