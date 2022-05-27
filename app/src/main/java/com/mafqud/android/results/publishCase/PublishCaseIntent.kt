package com.mafqud.android.results.publishCase


sealed class PublishCaseIntent {
    data class Publish(val caseID: Int) : PublishCaseIntent()
}