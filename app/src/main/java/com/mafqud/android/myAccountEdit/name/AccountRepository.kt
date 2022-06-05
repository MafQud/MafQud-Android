package com.mafqud.android.myAccountEdit.name

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.myAccountEdit.name.models.ChangeNameBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class AccountRepository @Inject constructor() : BaseRepository() {

    suspend fun changeUserName(userName: String): Result<Any> {
        return safeApiCall {
            return@safeApiCall coroutineScope {
                launch {
                    val result = remoteDataManager.changeUserName(
                        userID = getCurrentUserID(),
                        ChangeNameBody(name = userName)
                    )
                    saveNewUserName(userName)
                }
            }
        }
    }

    private suspend fun saveNewUserName(userName: String) {
        setUserName(userName)
    }


    private suspend fun getCurrentUserID() = getUserID()

    suspend fun getCurrentName() = getUserName()


}