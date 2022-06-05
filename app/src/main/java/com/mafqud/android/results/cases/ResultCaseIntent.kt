package com.mafqud.android.results.cases


sealed class ResultCaseIntent {
    data class GetMatchesCases(val caseId: Int) : ResultCaseIntent()
    data class Refresh(val caseId: Int) : ResultCaseIntent()
}