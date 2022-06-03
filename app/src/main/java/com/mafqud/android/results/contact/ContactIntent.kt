package com.mafqud.android.results.contact


sealed class ContactIntent {
    data class Done(val caseID: Int) : ContactIntent()
    data class Failed(val caseID: Int) : ContactIntent()
}