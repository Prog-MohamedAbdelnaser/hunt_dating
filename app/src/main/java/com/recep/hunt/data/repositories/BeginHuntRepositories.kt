package com.recep.hunt.data.repositories

import com.recep.hunt.api.ApiInterface
import com.recep.hunt.domain.entities.PushNotificationSingleUserParams
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import com.recep.hunt.domain.entities.UpdateHuntBeginParams
import io.reactivex.Single

class BeginHuntRepositories(private val apiInterface: ApiInterface,private val token:String) {

    fun callSendBeginHuntLocationAPI(beginHuntLocationParams: BeginHuntLocationParams):Single<BeginHuntLocation>{
        return apiInterface.sendUserHuntBeginLocation(beginHuntLocationParams).map { res->res.payload }
    }

    fun callPushNotificationSingleUsernAPI(params: PushNotificationSingleUserParams):Single<Any>{
        return apiInterface.pushNotificationSingleUser(params).map { res->res.payload }
    }


    fun callUpdateHuntBeginAPI(updateHuntBeginParams: UpdateHuntBeginParams):Single<Any>{
        return apiInterface.updateHuntBegin(updateHuntBeginParams).map { res->res.payload }
    }

}