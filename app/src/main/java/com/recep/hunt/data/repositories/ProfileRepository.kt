package com.recep.hunt.data.repositories

import android.util.Log
import com.recep.hunt.api.ApiInterface
import com.recep.hunt.data.sources.local.AppPreference
import com.recep.hunt.domain.entities.EntitiesConstants
import com.recep.hunt.domain.entities.EntitiesConstants.USER_INFO_MODULE
import com.recep.hunt.model.UserProfile.Data
import com.recep.hunt.model.UserProfile.ImageModel
import com.recep.hunt.model.UserProfile.ImagesListModel
import io.reactivex.Single

class ProfileRepository(private  val apiInterface: ApiInterface, private val  appPreference: AppPreference) {

    fun getUserInfo():Data{
        return appPreference.getObject(EntitiesConstants.USER_INFO_MODULE,Data::class.java)!!
    }

    fun getUserImages():ArrayList<ImageModel>{
        return getUserInfo().user_profile_image
    }

    fun saveUserInfo(data: Data){
        appPreference.saveObject(EntitiesConstants.USER_INFO_MODULE,data)
    }
}