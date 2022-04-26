package com.mafqud.android.report.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportfoundSecondFragment : BaseFragment() {


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
                    val isDialogOpened = remember {
                        mutableStateOf(false)
                    }
                    Scaffold(
                        topBar = {
                            TitledAppBar(
                                title = stringResource(id = R.string.report_found),
                                onIconClicked = {
                                    isDialogOpened.value = true
                                }
                            )
                        },
                        content = {
                            requireActivity().FoundScreenTwo(onBack = {
                                findNavController().popBackStack()

                            }, isDialogOpened)
                        },
                    )
                    BackHandler {
                        isDialogOpened.value = true
                    }
                }

            }
        }
    }
}