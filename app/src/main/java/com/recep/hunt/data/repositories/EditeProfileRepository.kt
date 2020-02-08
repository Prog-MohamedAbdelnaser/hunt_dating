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

class EditeProfileRepository(private  val apiInterface: ApiInterface,private val  appPreference: AppPreference) {

    fun getUserInfo():Data{
        return appPreference.getObject(EntitiesConstants.USER_INFO_MODULE,Data::class.java)!!
    }

    fun getUserImages():ArrayList<ImageModel>{
        return getUserInfo().user_profile_image
    }


    fun addImage(imageModel: ImageModel,imagePostion:Int){
        val updatedUserInfo =getUserInfo()
        Log.i("addImage","Image ${imageModel?.image}")

        updatedUserInfo.user_profile_image.add(imageModel)

        if ( updatedUserInfo.user_profile_image.getOrNull(imagePostion) !=null){
            updatedUserInfo.user_profile_image.set(imagePostion,imageModel)
        }else{
            updatedUserInfo.user_profile_image.add(imageModel)

        }
        updateUserInfor(updatedUserInfo)



    }

    fun updateImage(imageModel: ImageModel,imagePostion:Int){
        val updatedUserInfo =getUserInfo()
        updatedUserInfo.user_profile_image
      var  image =  updatedUserInfo.user_profile_image.find { it.id==imageModel.id }.apply {
            this?.image=imageModel.image
        }

        Log.i("updateImage","Image ${image?.image}")

        updateUserInfor(updatedUserInfo)

    }



    fun updateUserInfor(data: Data){
        appPreference.saveObject(USER_INFO_MODULE,data)
    }

    fun removeUserImage(imageModel: ImageModel):Single<ImageModel>{
        Log.i("removeUserImage","removeUserImage ${imageModel}")

        return   apiInterface.deleteImageFromUserImages(imageModel.id.toString()).doOnSuccess {
         removeImageFromPrefrence(imageModel)
     }.map { imageModel/* Log.i("removeUserImage","removeUserImage ${res}") */} }

    private fun removeImageFromPrefrence(imageModel: ImageModel){

        val updatedUserInfo =getUserInfo()

        updatedUserInfo.user_profile_image.apply { this.remove(imageModel) }

        updateUserInfor(updatedUserInfo)
    }


}