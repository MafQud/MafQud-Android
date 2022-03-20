package com.mafqud.android.map

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.mafqud.android.home.HeadSearchUi
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.SpacerUi
import com.mafqud.android.util.bitmap.getMarkerBitmapFromView


val singapore = LatLng(30.053333, 31.233334)
val singapore2 = LatLng(30.033333, 31.133334)
val singapore3 = LatLng(30.083333, 31.033334)

@Composable
fun MapScreen() {
    ColumnUi {
        BoxUi(Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
            ColumnUi {
                HeadSearchUi()
                SpacerUi(modifier = Modifier.height(8.dp))
            }
        }
        MapUi()
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
