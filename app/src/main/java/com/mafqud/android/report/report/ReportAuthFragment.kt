package com.mafqud.android.report.report

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportAuthFragment : BaseFragment() {

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
                    ReportAuthScreen(onReportLost = {
                        val graph = findNavController().graph.findNode(R.id.report_graph)
                        if (graph is NavGraph) {
                            graph.setStartDestination(R.id.reportLostFragment)
                            findNavController().navigate(R.id.action_reportAuthFragment_to_report_graph)
                        }
                    }, onReportFound = {
                        val graph = findNavController().graph.findNode(R.id.report_graph)
                        if (graph is NavGraph) {
                            graph.setStartDestination(R.id.reportFoundFragment)
                            findNavController().navigate(R.id.action_reportAuthFragment_to_report_graph)
                        }

                    }, onOpenApp = {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    })
                }
            }
        }
    }

}