package com.mafqud.android.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.IconBack
import com.mafqud.android.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

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
                    LoginScreen()
                }
            }
        }
    }

    @Composable
    private fun LoginScreen() {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp)

        ) {

            // back arrow
            BoxUi(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                IconBack(onClick = {

                })
            }


            ColumnUi(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //image
                SpacerUi(modifier = Modifier.height(90.dp))

                ImageUi(
                    painter = painterResource(id = R.drawable.ic_login),
                    modifier = Modifier.size(256.dp, 240.dp)
                )

                TextUi(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(id = R.string.login_title),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall
                )

                TextUi(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.insert_phone_num),
                    style = MaterialTheme.typography.labelLarge

                )

                TextFieldUi(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    value = "code.value",
                    onValueChange = {
                        "code.value = it"
                    },
                    singleLine = true,
                    leadingIcon = {
                        RowUi(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            TextUi(
                                modifier = Modifier.padding(4.dp),
                                text = stringResource(id = R.string.egypt_code),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelLarge
                            )
                            SpacerUi(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp)
                                    .padding(vertical = 12.dp)
                                    .background(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
                SpacerUi(modifier = Modifier.height(52.dp))
                ButtonAuth(title = stringResource(id = R.string.next), onClick = {

                })
            }
        }
    }

}