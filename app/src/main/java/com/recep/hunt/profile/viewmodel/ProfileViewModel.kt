package com.recep.hunt.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recep.hunt.api.ApiClient
import com.recep.hunt.data.repositories.EditeProfileRepository
import com.recep.hunt.data.repositories.ProfileRepository
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val profileRepository: ProfileRepository,private val token:String): ViewModel() {

    private val userProfileInfoMutableLiveDataState = MutableLiveData<CommonState<Response<UserProfileResponse>>>()
    val userProfileInfoleLiveDataState: LiveData<CommonState<Response<UserProfileResponse>>> = userProfileInfoMutableLiveDataState

     fun fetchUserProfileInfo() {

        userProfileInfoMutableLiveDataState.value=CommonState.LoadingShow
        val call = ApiClient.getClient.getUserProfile(token)

        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                userProfileInfoMutableLiveDataState.value=CommonState.LoadingFinished
            }

            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                userProfileInfoMutableLiveDataState.value=CommonState.LoadingFinished
/*
                if (!response.isSuccessful) {
                *//*    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@UserProfileActivity, strErrorJson)) {
                        return
                    }*//*
                }*/
                userProfileInfoMutableLiveDataState.value=CommonState.Success(response)
                if(response.isSuccessful) {
                    response.body()?.let { profileRepository.saveUserInfo(it.data) }
                }else{

                }

            }

        })

    }
}