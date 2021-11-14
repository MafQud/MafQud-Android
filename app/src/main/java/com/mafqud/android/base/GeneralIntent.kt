package com.mafqud.android.base

sealed class GeneralIntent {
    object GeneralData : GeneralIntent()
    object RefreshData : GeneralIntent()

}
