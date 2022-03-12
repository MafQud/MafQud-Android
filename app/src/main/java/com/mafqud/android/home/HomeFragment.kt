package com.mafqud.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.mafqud.android.ui.compose.IconNotification
import com.mafqud.android.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

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
                    ColumnUi(
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.surface
                            )
                            .padding(16.dp)
                    ) {
                        HeadUi()
                        SearchUi()
                        BodyUi(Modifier.weight(1f))
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchUi() {

    }

    @Composable
    private fun BodyUi(weight: Modifier) {

    }

    @Composable
    private fun HeadUi() {
        RowUi(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            RowUi(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                BoxUi(
                    Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    TextUi(
                        text = "M",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                TextUi(
                    text = "Marwa Kamel",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }


            IconNotification {

            }


        }
    }

}