package com.mafqud.android.reportedCases


sealed class ReportedCasesIntent {
    object GetReportedCases : ReportedCasesIntent()
    object Refresh : ReportedCasesIntent()
}