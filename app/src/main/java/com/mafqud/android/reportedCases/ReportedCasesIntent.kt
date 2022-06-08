package com.mafqud.android.reportedCases


sealed class ReportedCasesIntent {
    object GetReportedCases : ReportedCasesIntent()
    object GetReportedCasesLimited : ReportedCasesIntent()
    object Refresh : ReportedCasesIntent()
    data class FinishCase(val caseId: Int) : ReportedCasesIntent()
    data class ArchiveCase(val caseId: Int) : ReportedCasesIntent()
}