package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class BeginHuntLocationParams(@SerializedName("match_user_id")val matchUserId:Int, @SerializedName("location") val location:String,@SerializedName("place") val place:String)