package com.mafqud.android.map

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import com.mafqud.android.R
import com.mafqud.android.base.fragment.BaseFragment
import com.mafqud.android.home.CasesTabType
import com.mafqud.android.ui.compose.HomeAppBar
import com.mafqud.android.ui.compose.LoadingDialog
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.isGooglePlayServiceEnabled
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MapFragment : BaseFragment(), LocationListener {

    private val viewModel: MapViewModel by viewModels()

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()

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
                    val userName = remember {
                        mutableStateOf("")
                    }
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState, topBar = {
                        HomeAppBar(
                            userName = "userName.value",
                            onNotificationClicked = {
                                findNavController().navigate(R.id.actionToNotificationGraph)
                            }
                        )
                    }, content = {
                        ListenToChanges(userName, scaffoldState)
                    })

                }
            }
        }
    }

    @Composable
    private fun getRequiredPermissions() = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    @Composable
    private fun ListenToChanges(userName: MutableState<String>, scaffoldState: ScaffoldState) {
        val state = viewModel.stateChannel.collectAsState()
        val stateValue = state.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateValue.isRefreshing),
            onRefresh = {
                refreshDataIntent()
            },
        ) {
            if (stateValue.networkError != null) {
                stateValue.networkError.ShowNetworkErrorSnakeBar(scaffoldState)
            }

            if (stateValue.userName != null) {
                userName.value = stateValue.userName
            }

            if (requireActivity().isGooglePlayServiceEnabled()) {
                val permissions = getRequiredPermissions()
                val settingResultRequest = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult()
                ) { activityResult ->
                    if (activityResult.resultCode == RESULT_OK) {
                        getUserLocation()
                        LogMe.d("appDebug", "Accepted")
                    } else {
                        LogMe.d("appDebug", "Denied")
                    }
                }
                MapScreen(
                    stateValue.cases,
                    stateValue.mapUiType,
                    onGrantPermissions = {
                        if (permissions.allPermissionsGranted) {
                            changeMapUiIntent(MapUiType.OPEN_GPS)
                        } else {
                            // request permissions
                            requestRequiredPermissions(permissions)
                        }
                    }) {
                    openGPS(settingResultRequest)
                }
            }
            //LoadingDialog(stateValue.isLoading)
        }
    }

    private fun openGPS(settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        checkLocationSetting(
            context = requireContext(),
            onDisabled = { intentSenderRequest ->
                settingResultRequest.launch(intentSenderRequest)
            },
            onEnabled = {
                getUserLocation()
            }
        )
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val locationManager =
            requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        val isGPSEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGPSEnabled || isNetworkEnabled) {
            val locationNetwork = getLocationFromNetwork(locationManager)
            if (locationNetwork != null) {
                sendLocationIntent(locationNetwork)
            } else {
                val locationGPS = getLocationFromGPS(locationManager)
                if (locationGPS != null) {
                    sendLocationIntent(locationGPS)
                } else {
                    requireContext().showToast("location null")
                }
            }
        } else {
            requireContext().showToast(getString(R.string.open_gps))
        }
    }

    private fun sendLocationIntent(location: Location) {
        viewModel.intentChannel.trySend(MapIntent.SaveLocation(location))
    }

    @SuppressLint("MissingPermission")
    private fun getLocationFromGPS(locationManager: LocationManager): Location? {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
        )
        return locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLocationFromNetwork(locationManager: LocationManager): Location? {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
        )
        return locationManager
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestRequiredPermissions(multiplePermissionsState: MultiplePermissionsState) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    private fun refreshDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(MapIntent.Refresh)
        }
    }

    private fun requestDataIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentChannel.send(MapIntent.GetCases(CasesTabType.ALL))
        }
    }

    private fun changeMapUiIntent(mapUiType: MapUiType) {
        viewModel.intentChannel.trySend(MapIntent.SetMapType(mapUiType))
    }

    // call this function on button click
    private fun checkLocationSetting(
        context: Context,
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: () -> Unit
    ) {

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = PRIORITY_HIGH_ACCURACY
        }

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        gpsSettingTask.addOnSuccessListener { onEnabled() }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // ignore here
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        sendLocationIntent(location)
    }

}