package com.mafqud.android.map

import android.location.Location
import com.mafqud.android.home.CasesTabType


sealed class MapIntent {
    data class GetCases(val casesTabType: CasesTabType) : MapIntent()
    data class SetMapType(val mapUiType: MapUiType) : MapIntent()
    data class SaveLocation(val location: Location) : MapIntent()
    object Refresh : MapIntent()
}