package com.recep.hunt.matchs.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recep.hunt.api.ApiClient
import com.recep.hunt.domain.entities.BeginHuntLocation
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import com.recep.hunt.domain.usecases.BeginHuntUseCases
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.model.randomQuestion.QuestionData
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchQuestionsViewModel(private val beginHuntUseCases: BeginHuntUseCases): ViewModel() {

    private val disposables=CompositeDisposable()

    private val getQuestionMutableLiveData =MutableLiveData<CommonState<Response<RandomQuestionResponse>>>()
    val getQuestionLiveData:LiveData<CommonState<Response<RandomQuestionResponse>>> = getQuestionMutableLiveData

    private val sendHuntLocationMutableLiveData =MutableLiveData<CommonState<BeginHuntLocation>>()
    val sendHuntLocationLiveData:LiveData<CommonState<BeginHuntLocation>> = sendHuntLocationMutableLiveData


    fun getQuestions(context: Context) {
        val call =
            ApiClient.getClient.getRandomQuestion(SharedPrefrenceManager.getUserToken(context))

        call.enqueue(object : Callback<RandomQuestionResponse> {
            override fun onFailure(call: Call<RandomQuestionResponse>, t: Throwable) {
                getQuestionMutableLiveData.value=CommonState.Error(t)
            }

            override fun onResponse(call: Call<RandomQuestionResponse>, response: Response<RandomQuestionResponse>) {
                Log.i("MatchQuestionsViewModel","onResponse ${response.body().toString()}")
                getQuestionMutableLiveData.value=CommonState.Success(response)
            }

        })
    }

    fun sendHuntLocation(beginHuntLocationParams: BeginHuntLocationParams){
        disposables.add(beginHuntUseCases.execute(beginHuntLocationParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { sendHuntLocationMutableLiveData.value=CommonState.LoadingShow }
            .doFinally { sendHuntLocationMutableLiveData.value=CommonState.LoadingFinished  }
            .subscribe({sendHuntLocationMutableLiveData.value=CommonState.Success(it)},{
                sendHuntLocationMutableLiveData.value=CommonState.Error(it)
            }))
    }
}