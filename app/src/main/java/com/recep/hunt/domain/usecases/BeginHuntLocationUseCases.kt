package com.recep.hunt.domain.usecases

import com.locationpicker.sample.domain.usecases.UseCase
import com.recep.hunt.data.repositories.BeginHuntRepositories
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import com.recep.hunt.domain.entities.UpdateHuntBeginParams
import io.reactivex.Single

class BeginHuntLocationUseCases(private val beginHuntRepositories: BeginHuntRepositories) :UseCase<BeginHuntLocationParams,Single<BeginHuntLocation>>{
    override fun execute(param: BeginHuntLocationParams?): Single<BeginHuntLocation> {
        return beginHuntRepositories.callSendBeginHuntLocationAPI(param!!)
    }

    fun executeUpdateHuntBegin(param: UpdateHuntBeginParams?): Single<Any> {
        return beginHuntRepositories.callUpdateHuntBeginAPI(param!!)
    }
}