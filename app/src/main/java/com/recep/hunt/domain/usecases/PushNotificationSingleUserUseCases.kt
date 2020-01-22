package com.recep.hunt.domain.usecases

import com.locationpicker.sample.domain.usecases.UseCase
import com.recep.hunt.data.repositories.BeginHuntRepositories
import com.recep.hunt.domain.entities.PushNotificationSingleUserParams
import io.reactivex.Single

class PushNotificationSingleUserUseCases(private val beginHuntRepositories: BeginHuntRepositories) :UseCase<PushNotificationSingleUserParams,Single<Any>>{
    override fun execute(param: PushNotificationSingleUserParams?): Single<Any> {
        return beginHuntRepositories.callPushNotificationSingleUsernAPI(param!!)
    }


}