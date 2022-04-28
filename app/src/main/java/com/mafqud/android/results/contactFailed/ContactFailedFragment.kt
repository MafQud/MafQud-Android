package com.mafqud.android.results.contactFailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.notification.NotificationType
import com.mafqud.android.results.caseDetails.CaseDetailsFragmentArgs
import com.mafqud.android.results.caseDetails.CaseDetailsScreen
import com.mafqud.android.results.cases.ResultsCasesFragmentArgs
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.openDialer
import dagger.hilt.android.AndroidEntryPoint


class ContactFailedFragment : BaseFragment() {


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
                    Scaffold(content = {
                        ContactFailedScreen()
                    })
                }
            }
        }
    }

    @Composable
    @Preview
    fun ContactFailedScreen() {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.onPrimary
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ColumnUi(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageUi(
                    painter = painterResource(id = R.drawable.ic_bell),
                    modifier = Modifier.size(265.dp, 220.dp)
                )

                TextUi(
                    text = stringResource(id = R.string.will_send_notification),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}