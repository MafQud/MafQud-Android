package com.mafqud.android.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.ui.compose.HomeAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.isGooglePlayServiceEnabled
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var userName: MutableState<String> = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get user name from local storage
        getUserName()
    }

    private fun getUserName() {
        lifecycleScope.launchWhenCreated {
            userName.value  = dataStoreManager.getUserName()
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
                    Scaffold(topBar = {
                        HomeAppBar(
                            userName = userName.value,
                            onNotificationClicked = {
                                findNavController().navigate(R.id.actionToNotificationGraph)
                            }
                        )
                    }, content = {
                        if (requireActivity().isGooglePlayServiceEnabled()) {
                            MapScreen()
                        }
                    })

                }
            }
        }
    }

}