package com.mafqud.android.results.states.failed


sealed class FailedIntent {
    data class Publish(val caseID: Int) : FailedIntent()
}