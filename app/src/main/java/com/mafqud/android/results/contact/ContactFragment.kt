package com.mafqud.android.results.contact

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
import com.mafqud.android.results.caseDetails.CaseDetailsFragmentArgs
import com.mafqud.android.results.caseDetails.CaseDetailsScreen
import com.mafqud.android.results.cases.ResultsCasesFragmentArgs
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.openDialer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : BaseFragment() {


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
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            title = stringResource(id = R.string.contact_another)
                        )
                    }, content = {
                        ContactScreen(openDialer = { number ->
                            requireContext().openDialer(number)
                        }, onContactFailed = {
                            findNavController().navigate(R.id.action_contactFragment_to_contactFailedFragment)
                        }, onContactSuccess = {

                        })
                    })
                }
            }
        }
    }

}