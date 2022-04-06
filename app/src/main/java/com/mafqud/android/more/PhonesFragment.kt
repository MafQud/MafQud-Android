package com.mafqud.android.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.openDialer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhonesFragment : Fragment() {

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
                    Scaffold(topBar = {
                        TitledAppBar(
                            title = stringResource(id = R.string.phones),
                            onBackClicked = {
                                findNavController().popBackStack()
                            }
                        )
                    }, content = {
                        PhonesScreen(onItemClicked = {
                            // open dialer activity of android system
                            requireActivity().openDialer(it)
                        })
                    })
                }
            }
        }
    }

    @Composable
    @Preview
    private fun PhonesScreen(onItemClicked: (String) -> Unit = {}) {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            ColumnUi(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextUi(
                    text = stringResource(id = R.string.press_call),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                PhoneItem(stringResource(id = R.string.police), "122", onItemClicked)
                PhoneItem(stringResource(id = R.string.ambulance), "123", onItemClicked)
                PhoneItem(stringResource(id = R.string.hotline_pepole), "15044", onItemClicked)
                PhoneItem(stringResource(id = R.string.hotline_druges), "3041948", onItemClicked)
                PhoneItem(stringResource(id = R.string.rapid_intervention), "16439", onItemClicked)
                PhoneItem(stringResource(id = R.string.baby_help), "16000", onItemClicked)
            }
        }
    }

    @Composable
    private fun PhoneItem(stringResource: String, phone: String, onItemClicked: (String) -> Unit) {
        BoxUi(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.08f
                    )
                )
                .clickable {
                    onItemClicked(phone)
                }
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            RowUi(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextUi(
                    text = stringResource,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    text = phone,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }

}