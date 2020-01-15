package com.recep.hunt.model.UserProfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    val about: String,
    val job_title: String,
    val company: String,
    val hometown: String,
    val school: String,
    val height:String,
    val gym:String,
    val education_level:String,
    val drink:String,
    val smoke:String,
    val pets:String,
    val kids:String,
    val zodiac:String,
    val politics:String,
    val religion:String,
    val vote:String,
    val gender:String
)