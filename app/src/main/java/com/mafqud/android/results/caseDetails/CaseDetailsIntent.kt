package com.mafqud.android.results.caseDetails

import com.mafqud.android.home.CasesType


sealed class CaseDetailsIntent {
    data class GetCase(val caseId: Int) : CaseDetailsIntent()
    data class Refresh(val caseId: Int) : CaseDetailsIntent()
}