package com.mafqud.android.map

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.mafqud.android.R
import com.mafqud.android.home.*
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.toCorrectImageUrl
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.bitmap.downloadBitmapFromUrl
import com.mafqud.android.util.bitmap.getMarkerBitmapFromView


val singapore = LatLng(30.053333, 31.233334)
val singapore2 = LatLng(30.033333, 31.133334)
val singapore3 = LatLng(30.083333, 31.033334)

@Composable
fun MapScreen(
    cases: CasesDataResponse?,
    mapUiType: MapUiType,
    onGrantPermissions: () -> Unit,
    onOpenGPS: () -> Unit,
) {

    when (mapUiType) {
        MapUiType.REQUIRE_PERMISSIONS -> {
            PermissionsView(onGrantPermissions)
        }
        MapUiType.OPEN_GPS -> {
            GPSView(onOpenGPS)
        }
        MapUiType.DISPLAY_CASES -> {
            DisplayMapView(cases)
        }
        MapUiType.NONE -> {

        }

    }
}

@Composable
fun GPSView(onOpenGPS: () -> Unit) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        ColumnUi(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconUi(
                imageVector = Icons.Outlined.ShareLocation,
                modifier = Modifier
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            TextUi(
                text = stringResource(id = R.string.open_gps),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            ButtonAuth(title = stringResource(id = R.string.gps), onClick = {
                onOpenGPS()
            })
        }
    }
}

@Composable
fun PermissionsView(onGrantPermissions: () -> Unit) {

    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        ColumnUi(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageUi(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.map_view),
            )

            TextUi(
                text = stringResource(id = R.string.map_pemissions),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            ButtonAuth(title = stringResource(id = R.string.permission), onClick = {
                onGrantPermissions()
            })
        }
    }
}

@Composable
fun DisplayMapView(cases: CasesDataResponse?) {
    ColumnUi {
        BoxUi(Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
            ColumnUi {
                MapSearchUi({

                })
                SpacerUi(modifier = Modifier.height(8.dp))
            }
        }
        MapUi(cases)
    }

}

@Composable
fun MapSearchUi(
    onTapClicked: (CasesTabType) -> Unit = {},
    selectedTapState: CasesTabType = CasesTabType.ALL
) {
    BoxUi(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SearchUi(onTapClicked, selectedTapState)
    }
}

@Composable
private fun SearchUi(onTapClicked: (CasesTabType) -> Unit, selectedTapState: CasesTabType) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // TODO add other search ui maps
        TapsUi(onTapClicked, selectedTapState)
    }
}

@Composable
private fun TapsUi(onTapClicked: (CasesTabType) -> Unit, selectedTapState: CasesTabType) {
    RowUi {
        val selectedItem = remember {
            mutableStateOf(selectedTapState)
        }

        val activeTextColor = MaterialTheme.colorScheme.onSecondary
        val activeBackgroundColor = MaterialTheme.colorScheme.primary


        val disableTextColor = MaterialTheme.colorScheme.primary
        val disableBackgroundColor = MaterialTheme.colorScheme.onSecondary

        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .background(if (selectedItem.value == CasesTabType.ALL) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.ALL) {
                        selectedItem.value = CasesTabType.ALL
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.all),
                color = if (selectedItem.value == CasesTabType.ALL) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RectangleShape
                )
                .background(if (selectedItem.value == CasesTabType.MISSING) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.MISSING) {
                        selectedItem.value = CasesTabType.MISSING
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.lost),
                color = if (selectedItem.value == CasesTabType.MISSING) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selectedItem.value == CasesTabType.FOUND) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.FOUND) {
                        selectedItem.value = CasesTabType.FOUND
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.found),
                color = if (selectedItem.value == CasesTabType.FOUND) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
fun MapUi(cases: CasesDataResponse?) {
    var isMapLoaded by remember { mutableStateOf(false) }
    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 11f)
    }
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoaded = true
            },
        ) {
            cases?.cases?.map {
                // TODO fix random
                val random = listOf(
                    singapore,
                    singapore2,
                    singapore3
                )
                val bitmapState: MutableState<Bitmap?> = remember {
                    mutableStateOf(null)
                }
                // adding a marker on map with image from  drawable
                context.downloadBitmapFromUrl(it.thumbnail.toCorrectImageUrl()) { bitmap ->
                    bitmapState.value = bitmap
                }
                if (bitmapState.value != null) {
                    Marker(
                        position = random.random(),
                        title = it.name,
                        onInfoWindowClick = {
                            Log.i("onInfoWindowClick", "yes")
                        }, icon = drawMapPin(it, bitmapState.value!!)
                    )
                }
            }
            /* Marker(position = singapore, title = "Marker 1", onInfoWindowClick = {
                 Log.i("onInfoWindowClick", "yes")
             }, icon = drawMapPin())

             Marker(position = singapore2, title = "Marker 2", icon = drawMapPin())
             Marker(position = singapore3, title = "Marker 3", icon = drawMapPin())*/

        }
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun drawMapPin(case: CasesDataResponse.Case, bitmap: Bitmap): BitmapDescriptor {
    val context = LocalContext.current
    return BitmapDescriptorFactory.fromBitmap(context.getMarkerBitmapFromView(case, bitmap))

}
