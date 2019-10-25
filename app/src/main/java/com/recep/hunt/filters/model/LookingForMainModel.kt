package com.recep.hunt.filters.model

import android.content.Context
import android.content.SharedPreferences
import com.recep.hunt.R
import com.recep.hunt.application.MyApplication
import com.recep.hunt.constants.Constants
import kotlin.collections.ArrayList


/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

data class LookingForDataModel(val title:String,
                               val unSelectedImage:Int,
                               val selectedImage:Int,
                               val interestedIn:String?,
                               val value:String)
class LookingForMainModel{
    companion object{
        private val lookingForStorage = "looking_For_Storage"
        private lateinit var storage : SharedPreferences
        fun getInstance(): LookingForMainModel {
            storage = MyApplication.instance!!.getSharedPreferences(
                lookingForStorage,Context.MODE_PRIVATE)
            return LookingForMainModel()
        }

    }
    fun getData():ArrayList<LookingForDataModel>{
        val items = ArrayList<LookingForDataModel>()
        items.add(
            LookingForDataModel(
                "Date",
                R.drawable.ic_heart,
                R.drawable.ic_date_white,
                Constants.FEMALE,
                Constants.male
            )
        )
        items.add(
            LookingForDataModel(
                "Business",
                R.drawable.ic_buisness_icon,
                R.drawable.ic_buisness_white,
                Constants.BOTH,
                Constants.female
            )
        )
        items.add(
            LookingForDataModel(
                "Friendship",
                R.drawable.friendship_icon,
                R.drawable.friendship_white,
                Constants.MALE,
                Constants.both
            )
        )
        return items
    }


}