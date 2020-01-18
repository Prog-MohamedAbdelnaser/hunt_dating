package com.recep.hunt.matchs.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recep.hunt.api.ApiClient
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.model.randomQuestion.QuestionData
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchQuestionsViewModel(): ViewModel() {

    private val getQuestionMutableLiveData =MutableLiveData<CommonState<Response<RandomQuestionResponse>>>()
    val getQuestionLiveData:LiveData<CommonState<Response<RandomQuestionResponse>>> = getQuestionMutableLiveData

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
}