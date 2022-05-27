package com.mafqud.android.results.states.success


sealed class SuccessResultsIntent {
    data class AddNationalId(val nationalId: String) : SuccessResultsIntent()
    object ClearState : SuccessResultsIntent()
}