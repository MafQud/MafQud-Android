package com.mafqud.android.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : Fragment() {

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
                    IntroScreen()
                }
            }
        }
    }

    @Composable
    @Preview
    private fun IntroScreen() {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)

        ) {
            ColumnUi(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ImageUi(
                    painter = painterResource(id = R.drawable.ic_intro_robot),
                    modifier = Modifier.size(250.dp, 300.dp)
                )

                TextUi(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(id = R.string.welcome_mafqud),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.headlineMedium

                )
                TextUi(
                    text = stringResource(id = R.string.mafqud_help),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.titleMedium


                )
                //login
                ButtonAuth(title = stringResource(id = R.string.login),
                    textColor = MaterialTheme.colorScheme.tertiary,
                    backgroundColor = MaterialTheme.colorScheme.background, onClick = {
                        findNavController().navigate(R.id.action_introFragment_to_loginFragment)

                    })

                //sign up
                ButtonAuth(title = stringResource(id = R.string.sign_up),
                    textColor = MaterialTheme.colorScheme.background,
                    backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer, onClick = {
                        findNavController().navigate(R.id.action_introFragment_to_registerFragment)
                    })
            }

            ImageUi(
                painter = painterResource(id = R.drawable.ic_mafqud_logo),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }

}