package com.mafqud.android.auth

import android.os.Bundle
import com.mafqud.android.R
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.util.other.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.blue))
        setContentView(R.layout.activity_auth)

    }
}