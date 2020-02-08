package com.recep.hunt.domain.usecases

import android.util.Log
import com.locationpicker.sample.domain.usecases.UseCase
import com.recep.hunt.data.repositories.BeginHuntRepositories
import com.recep.hunt.data.repositories.EditeProfileRepository
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import com.recep.hunt.model.UserProfile.Data
import com.recep.hunt.model.UserProfile.ImageModel
import com.recep.hunt.model.UserProfile.UserInfoModel
import io.reactivex.Single

class EditProfileUseCases(private val editeProfileRepository: EditeProfileRepository) :UseCase<Any,Single<Data>>{
    override fun execute(param: Any?): Single<Data> {
        return Single.fromCallable{editeProfileRepository.getUserInfo()}
    }


   fun executeRemoveImage(param: ImageModel): Single<ImageModel> {
       Log.i("executeRemoveImage","executeRemoveImage")

       return editeProfileRepository.removeUserImage(param)
    }

    fun executeGetUserImages(): List<ImageModel> {
        return editeProfileRepository.getUserImages()
    }


}