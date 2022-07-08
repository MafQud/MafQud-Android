package com.mafqud.android.mapper

import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class CaseContact(
    val id: Int? = -1,
    val caseType: CaseType? = CaseType.NONE,
    val name: String? = "",
    val phone: String? = "",
    val address: String? = "",
) : Serializable