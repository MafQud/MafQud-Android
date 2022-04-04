package com.mafqud.android.home.model


data class CasesDataResponse(
    val id: Int = 0,
    val data: List<Data> = emptyList()
) {
    data class Data(
        val id: Int = 0,
        val message: String = "",
    )
}

