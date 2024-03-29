package com.mafqud.android.report

import android.os.Bundle
import com.mafqud.android.R
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.util.other.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.gray_status))
        setContentView(R.layout.activity_report)

    }
}