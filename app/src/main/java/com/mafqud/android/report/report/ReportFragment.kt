package com.mafqud.android.report.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.ui.compose.HomeAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : BaseFragment() {

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
                        HomeAppBar(
                            userName = "",
                            onNotificationClicked = {
                                findNavController().navigate(R.id.action_notification)
                            }
                        )
                    }, content = {
                        ReportScreen(onReportLost = {
                            findNavController().navigate(R.id.action_reportFragment_to_report_graph)
                        }, onReportFound = {
                            val graph = findNavController().graph.findNode(R.id.report_graph)
                            if (graph is NavGraph) {
                                graph.setStartDestination(R.id.reportFoundFragment)
                                findNavController().navigate(R.id.action_reportFragment_to_report_graph)
                            }
                        })
                    })

                }
            }
        }
    }

}