package com.mafqud.android.locations

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import javax.inject.Inject

data class MyGov(
    val id: Int?,
    val name: String?
)

data class MyCity(
    val id: Int?,
    val govId: Int?,
    val name: String?
)

class LocationUseCase @Inject constructor(private val baseRepository: BaseRepository) {

    private var lang: String = BaseActivity.LangType.AR.toString()

    suspend fun getGovs(): List<MyGov>? {
        lang = baseRepository.getAppLanguage()
        val result = safeApiCall { return@safeApiCall  baseRepository.getGos() }
        return when(result) {
            is Result.NetworkError.Generic -> null
            Result.NetworkError.NoInternet -> null
            is Result.Success -> mapCurrentGovs(result.data)
        }
    }

    private fun mapCurrentGovs(data: GovResponse): List<MyGov>? {
        return data.results?.let {
            return@let it.map {
                val govName = when(lang) {
                    BaseActivity.LangType.AR.toString() -> it?.nameAr
                    BaseActivity.LangType.EN.toString() -> it?.nameEn
                    else -> {""}
                }
                return@map MyGov(id = it?.id, name = govName)
            }
        }
    }


    suspend fun getGCities(govId: Int) : List<MyCity>? {
        val result = safeApiCall { return@safeApiCall  baseRepository.getCities(govId = govId) }
        return when(result) {
            is Result.NetworkError.Generic -> null
            Result.NetworkError.NoInternet -> null
            is Result.Success -> mapCurrentCities(result.data, govId)
        }
    }

    private fun mapCurrentCities(data: CitiesResponse, govId: Int): List<MyCity>? {
        return data.results?.let {
           return@let it.map {
                val cityName = when(lang) {
                    BaseActivity.LangType.AR.toString() -> it?.nameAr
                    BaseActivity.LangType.EN.toString() -> it?.nameEn
                    else -> {""}
                }
                return@map MyCity(
                    name = cityName,
                    id = it?.id,
                    govId = govId,
                )
            }
        }
    }
}