package com.mafqud.android.auth

import android.app.Activity
import android.content.Intent
import com.mafqud.android.report.ReportActivity


fun Activity.openReportActivity() {
    startActivity(
        Intent(
           this, ReportActivity::class.java
        )
    )
    finish()
}