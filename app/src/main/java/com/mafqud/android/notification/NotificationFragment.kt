package com.mafqud.android.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        if (activity is HomeActivity) {
            activity.homeBarVisibility(isVisible = false)
        }
    }
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
                    NotificationScreen()
                }
            }
        }
    }

}