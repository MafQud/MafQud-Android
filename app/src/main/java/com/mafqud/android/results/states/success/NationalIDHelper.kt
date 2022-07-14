package com.mafqud.android.results.states.success

import com.mafqud.android.data.DataStoreManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NationalIDHelper @Inject constructor() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private fun getUserNationalID(): String {
        return runBlocking {
            return@runBlocking dataStoreManager.getNationalId()
        }
    }

    fun shouldOpenNationalIdScreen() = true


}