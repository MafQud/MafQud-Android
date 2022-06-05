package com.mafqud.android.myAccountEdit.name



sealed class AccountIntent {
    data class ChangeName(val userName: String) : AccountIntent()
    object CurrentName : AccountIntent()
}