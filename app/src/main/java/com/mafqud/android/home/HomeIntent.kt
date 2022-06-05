package com.mafqud.android.home


sealed class HomeIntent {
    data class GetCases(val casesTabType: CasesTabType) : HomeIntent()
    data class GetCasesByAge(val ageRange: AgeRange) : HomeIntent()
    data class GetCasesByName(val name: String) : HomeIntent()
    data class GetCasesByGov(val id: Int?) : HomeIntent()
    object Refresh : HomeIntent()
}