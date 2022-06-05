package com.mafqud.android.home


sealed class HomeIntent {
    data class GetCases(val casesTabType: CasesTabType) : HomeIntent()
    data class GetCasesByAge(val ageRange: AgeRange) : HomeIntent()
    object Refresh : HomeIntent()
}