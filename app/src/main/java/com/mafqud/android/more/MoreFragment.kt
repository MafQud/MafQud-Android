package com.mafqud.android.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mafqud.android.R
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.di.MyServiceInterceptor
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {


    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor

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
                    MoreScreen(onReportedClicked = {

                    }, onAccountClicked = {
                        findNavController().navigate(R.id.action_moreFragment_to_myAccountFragment)

                    }, onSettingClicked = {
                        findNavController().navigate(R.id.action_moreFragment_to_settingFragment)

                    }, onHelpClicked = {
                        findNavController().navigate(R.id.action_moreFragment_to_helpFragment)

                    }, onPhonesClicked = {
                        findNavController().navigate(R.id.action_moreFragment_to_phonesFragment)

                    }, onLogoutClicked = {
                        logOutCurrentUser()

                    })
                }
            }
        }
    }

    private fun logOutCurrentUser() {
        lifecycleScope.launch {
            // clear the current user token from interceptor
            myServiceInterceptor.setSessionToken("")
            // clear user data
            dataStoreManager.clearDataStore()
            // restart application
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

}