package com.recep.hunt.data.repositories

import com.recep.hunt.api.ApiInterface
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import io.reactivex.Single

class BeginHuntRepositories(private val apiInterface: ApiInterface,private val token:String) {

    fun callSendBeginHuntLocationAPI(beginHuntLocationParams: BeginHuntLocationParams):Single<BeginHuntLocation>{
        return apiInterface.sendUserHuntBeginLocation(beginHuntLocationParams).map { res->res.payload }
    }
}