package com.recep.hunt.swipe.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recep.hunt.domain.entities.PushNotificationSingleUserParams
import com.recep.hunt.domain.usecases.PushNotificationSingleUserUseCases
import com.recep.hunt.features.common.CommonState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SwipeViewModel(private val pushNotificationSingleUserUseCases: PushNotificationSingleUserUseCases): ViewModel(){

    private val disposables= CompositeDisposable()

    private val sendPushNotificationSingleUserMutableLiveData = MutableLiveData<CommonState<Any>>()
    val sendPushNotificationSingleUserLiveData: LiveData<CommonState<Any>> = sendPushNotificationSingleUserMutableLiveData

    fun sendPushNotificationSingleUser(pushNotificationSingleUserParams: PushNotificationSingleUserParams){
        disposables.add(pushNotificationSingleUserUseCases.execute(pushNotificationSingleUserParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { sendPushNotificationSingleUserMutableLiveData.value=CommonState.LoadingShow }
            .doFinally { sendPushNotificationSingleUserMutableLiveData.value=CommonState.LoadingFinished  }
            .subscribe({sendPushNotificationSingleUserMutableLiveData.value=CommonState.Success(it)},{
                sendPushNotificationSingleUserMutableLiveData.value=CommonState.Error(it)
            }))
    }

}