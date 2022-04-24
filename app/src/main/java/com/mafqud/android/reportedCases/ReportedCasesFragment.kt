package com.mafqud.android.reportedCases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.navigation.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.EmptyCasesState
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportedCasesFragment : BaseFragment() {

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
                           title = stringResource(id = R.string.reported_people),
                            onIconClicked = {
                                findNavController().navigateUp()
                            }
                        )
                    }, content = {
                        BoxUi(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            EmptyCasesState()
                        }
                    })

                }
            }
        }
    }

}