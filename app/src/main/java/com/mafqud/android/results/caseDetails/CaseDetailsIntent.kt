package com.mafqud.android.results.caseDetails


sealed class CaseDetailsIntent {
    data class GetCase(val caseId: Int) : CaseDetailsIntent()
    data class Refresh(val caseId: Int) : CaseDetailsIntent()
}