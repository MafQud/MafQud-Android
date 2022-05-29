package com.mafqud.android.map

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.bitmap.getMarkerBitmapFromView


val singapore = LatLng(30.053333, 31.233334)
val singapore2 = LatLng(30.033333, 31.133334)
val singapore3 = LatLng(30.083333, 31.033334)

@Composable
fun MapScreen() {
    ColumnUi {
        BoxUi(Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
            ColumnUi {
                MapSearchUi({

                })
                SpacerUi(modifier = Modifier.height(8.dp))
            }
        }
        MapUi()
    }
}

@Composable
fun MapSearchUi(
    onTapClicked: (CasesType) -> Unit = {},
    selectedTapState: CasesType = CasesType.ALL
) {
    BoxUi(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SearchUi(onTapClicked, selectedTapState)
    }
}

@Composable
private fun SearchUi(onTapClicked: (CasesType) -> Unit, selectedTapState: CasesType) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // TODO add other search ui maps
        TapsUi(onTapClicked, selectedTapState)
    }
}

@Composable
private fun TapsUi(onTapClicked: (CasesType) -> Unit, selectedTapState: CasesType) {
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
                .background(if (selectedItem.value == CasesType.ALL) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.ALL) {
                        selectedItem.value = CasesType.ALL
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.all),
                color = if (selectedItem.value == CasesType.ALL) activeTextColor else disableTextColor,
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
                .background(if (selectedItem.value == CasesType.MISSING) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.MISSING) {
                        selectedItem.value = CasesType.MISSING
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.lost),
                color = if (selectedItem.value == CasesType.MISSING) activeTextColor else disableTextColor,
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
                .background(if (selectedItem.value == CasesType.FOUND) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.FOUND) {
                        selectedItem.value = CasesType.FOUND
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.found),
                color = if (selectedItem.value == CasesType.FOUND) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}



@Composable
fun MapUi() {
    var isMapLoaded by remember { mutableStateOf(false) }
    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 11f)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoaded = true
            },
        ) {
            Marker(position = singapore, title = "Marker 1", onInfoWindowClick = {
                Log.i("onInfoWindowClick", "yes")
            }, icon = drawMapPin())

            Marker(position = singapore2, title = "Marker 2", icon = drawMapPin())
            Marker(position = singapore3, title = "Marker 3", icon = drawMapPin())

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
fun drawMapPin(): BitmapDescriptor {
    val context = LocalContext.current
    return BitmapDescriptorFactory.fromBitmap(context.getMarkerBitmapFromView())

}
