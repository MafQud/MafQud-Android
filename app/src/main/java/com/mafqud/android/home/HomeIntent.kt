package com.mafqud.android.home


sealed class HomeIntent {
    data class GetCases(val casesType: CasesType) : HomeIntent()
    object Refresh : HomeIntent()
}