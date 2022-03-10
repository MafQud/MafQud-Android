package com.mafqud.android.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

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
                    RegisterScreen()
                }
            }
        }
    }

    @Composable
    private fun RegisterScreen() {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ColumnUi(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ColumnUi(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextUi(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(id = R.string.welcome_mafqud),
                    )
                    TextUi(
                        text = stringResource(id = R.string.mafqud_help),
                    )
                }

                ColumnUi(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextUi(
                        modifier = Modifier.padding(top = 12.dp),
                        text = stringResource(id = R.string.welcome_mafqud),
                    )
                    BoxUi(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)) {
                        ButtonUi(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50)), onClick = {

                            }) {
                            TextUi(text = stringResource(id = R.string.sign_up))

                        }
                    }
                }
            }
        }
    }

}