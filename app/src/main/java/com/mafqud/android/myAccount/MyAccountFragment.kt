package com.mafqud.android.myAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.mafqud.android.R
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.UserPayload
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyAccountFragment : Fragment() {

    private lateinit var userName: MutableState<String>

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var userPayload: UserPayload? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            readUserData()
        }
    }

    private suspend fun readUserData() {
        userPayload = dataStoreManager.readUserData()

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
                        TitledAppBar(
                            title = stringResource(id = R.string.my_account),
                            onIconClicked = {
                                findNavController().popBackStack()
                            }
                        )
                    }, content = {
                        userName = remember {
                            mutableStateOf("")
                        }
                        val address = remember {
                            mutableStateOf("")
                        }
                        val phone = remember {
                            mutableStateOf("")
                        }
                        loadUserData(userName, address, phone)
                        AccountScreen(
                            userName = userName.value,
                            address = address.value,
                            phone = phone.value,
                            onEditClicked = {
                                findNavController().navigate(R.id.action_myAccountFragment_to_myAccountEditFragment)
                            }, onEditInfoClicked = {
                                //TODO profile data
                                //findNavController().navigate(R.id.action_myAccountFragment_to_myAccountEditInfoFragment)
                            })
                    })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenCreated {
            readUserData()
            if (::userName.isInitialized) {
                userName.value = userPayload?.name ?: ""
            }
        }
    }

    private fun loadUserData(
        userName: MutableState<String>,
        address: MutableState<String>,
        phone: MutableState<String>
    ) {
        lifecycleScope.launchWhenCreated {
            if (userPayload == null)
                readUserData()

            userName.value = userPayload?.name ?: ""
            address.value = userPayload?.address ?: ""
            phone.value = userPayload?.phone ?: ""
        }

    }

}