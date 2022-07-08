package com.mafqud.android.homeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.mapper.CaseContact
import com.mafqud.android.notification.CaseModel
import com.mafqud.android.results.caseDetails.*
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.results.states.success.NationalIDHelper
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeDetailsFragment : BaseFragment() {

    private val args: HomeDetailsFragmentArgs by navArgs()
    private val viewModel: CaseDetailsViewModel by viewModels()

    @Inject
    lateinit var nationalIDHelper: NationalIDHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestDataIntent()
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
                    val appbarTitle = when (args.caseType) {
                        CaseType.FOUND ->
                            stringResource(id = R.string.title_losts_data)
                        CaseType.MISSING ->
                            stringResource(id = R.string.title_founds_data)
                        else -> {
                            ""
                        }
                    }
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        TitledAppBar(
                            onIconClicked = {
                                findNavController().navigateUp()
                            },
                            title = appbarTitle
                        )
                    }, content = {
                        ListenToChanges(scaffoldState)
                    })
                }
            }
        }
    }

    @Composable
    private fun ListenToChanges(scaffoldState: ScaffoldState) {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateValue.isRefreshing),
            onRefresh = {
                refreshDataIntent()
            },
        ) {

            CircleLoading(stateValue.isLoading)

            CaseDetailsScreen(stateValue.case,
                onContact = {
                    if (stateValue.case != null) {
                        openNextScreen(stateValue.case)
                    }
                }, onImageClicked = {
                    openImagePreviewer(it)
                })

            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }
        }
    }

    private fun openNextScreen(case: CaseDetailsResponse) {
        val caseContact = CaseContact(
            id = args.caseID,
            caseType = args.caseType,
            name = case.details?.name,
            phone = case.user,
            address = case.location?.getFullAddress(),
        )
        /**
         * if the user has no national id so open national Id screen to fill his national ID
         */
        if (nationalIDHelper.shouldOpenNationalIdScreen()) {
            val actionToNAtionalID =
                HomeDetailsFragmentDirections.actionHomeDetailsFragmentToNationalIdFragment(
                )
            actionToNAtionalID.caseContact = caseContact
            findNavController().navigate(actionToNAtionalID)
        }
        /**
         * if user national id in not empty open contact screen .
         */
        else {
            val actionToContact =
                HomeDetailsFragmentDirections.actionHomeDetailsFragmentToContactFragment2(
                )
            actionToContact.caseContact = caseContact
            findNavController().navigate(actionToContact)
        }
    }

    private fun openImagePreviewer(imageUrl: String?) {
        val action =
            HomeDetailsFragmentDirections.actionHomeDetailsFragmentToImageSliderFragment(imageUrl)

        findNavController().navigate(action)
    }

    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(CaseDetailsIntent.Refresh(args.caseID))
        }
    }

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(CaseDetailsIntent.GetCase(args.caseID))
        }
    }

}