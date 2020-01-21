package com.recep.hunt.data.repositories

import com.recep.hunt.api.ApiInterface
import com.recep.hunt.domain.entities.PushNotificationSingleUserParams
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import io.reactivex.Single

class PushNotificationSingleUserRepositories(private val apiInterface: ApiInterface) {

    fun callPushNotificationSingleUsernAPI(params: PushNotificationSingleUserParams):Single<Any>{
        return apiInterface.pushNotificationSingleUser(params).map { res->res.payload }
    }
}