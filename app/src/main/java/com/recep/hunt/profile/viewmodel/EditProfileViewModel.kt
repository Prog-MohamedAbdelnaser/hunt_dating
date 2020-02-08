package com.recep.hunt.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recep.hunt.domain.usecases.EditProfileUseCases
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.model.UserProfile.ImageModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditProfileViewModel(private val editProfileUseCases: EditProfileUseCases):ViewModel() {


    private val disposables= CompositeDisposable()

    private val removeImageMutableLiveData = MutableLiveData<CommonState<ImageModel>>()
    val  removeImageLiveData: LiveData<CommonState<ImageModel>> = removeImageMutableLiveData



    fun removeImage(imageModel: ImageModel){
        Log.i("editProfileViewModel","editProfileViewModel")
        disposables.add(editProfileUseCases.executeRemoveImage(imageModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Log.i("editProfileViewModel","doOnSubscribe")

                removeImageMutableLiveData.value = CommonState.LoadingShow }
            .doFinally {
                Log.i("editProfileViewModel","LoadingFinished")

                removeImageMutableLiveData.value = CommonState.LoadingFinished }
            .subscribe({
                Log.i("editProfileViewModel","Success ${it}")

                removeImageMutableLiveData.value = CommonState.Success(it)
            }, {
                removeImageMutableLiveData.value = CommonState.Error(it)
            })
        )

    }
}