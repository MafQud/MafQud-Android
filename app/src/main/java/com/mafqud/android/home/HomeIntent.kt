package com.mafqud.android.home


sealed class HomeIntent {
    data class GetCases(val casesTabType: CasesTabType) : HomeIntent()
    object Refresh : HomeIntent()
}