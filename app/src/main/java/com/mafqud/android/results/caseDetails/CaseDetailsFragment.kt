package com.mafqud.android.results.caseDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.notification.NotificationType
import com.mafqud.android.results.cases.ResultsCasesFragmentArgs
import com.mafqud.android.results.cases.ResultsCasesFragmentDirections
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseDetailsFragment : BaseFragment() {

    private val args: CaseDetailsFragmentArgs by navArgs()

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
                    val appbarTitle = when (args.notificationType) {
                        NotificationType.SUCCESS_FINDING_LOST ->
                            stringResource(id = R.string.title_losts_data)
                        NotificationType.SUCCESS_FINDING_FOUND ->
                            stringResource(id = R.string.title_founds_data)
                        else -> {
                            ""
                        }
                    }
                    Scaffold(topBar = {
                        TitledAppBar(
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            title = appbarTitle
                        )
                    }, content = {
                        CaseDetailsScreen(onContact = {
                            val actionToContact =
                                CaseDetailsFragmentDirections.actionCaseDetailsFragmentToContactFragment()
                            findNavController().navigate(actionToContact)
                        })
                    })
                }
            }
        }
    }

}