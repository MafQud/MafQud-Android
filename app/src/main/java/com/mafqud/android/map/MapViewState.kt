package com.mafqud.android.map

import android.location.Location
import android.util.Range
import androidx.annotation.Keep
import androidx.paging.PagingData
import com.mafqud.android.home.CasesTabType
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.locations.MyGov
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow

@Keep
data class AgeRange(
    val start: Int,
    val end: Int,
)

@Keep
enum class MapUiType {
    REQUIRE_PERMISSIONS,
    OPEN_GPS,
    DISPLAY_CASES,
    NONE
}

data class MapViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val userName: String? = null,
    val networkError: Result.NetworkError? = null,
    val casesTabType: CasesTabType = CasesTabType.ALL,
    val cases: CasesDataResponse? = null,
    val mapUiType: MapUiType = MapUiType.NONE,
    val location: Location? = null,
)