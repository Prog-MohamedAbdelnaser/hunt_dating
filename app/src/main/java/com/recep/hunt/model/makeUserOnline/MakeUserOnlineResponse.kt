package com.recep.hunt.model.makeUserOnline

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MakeUserOnlineResponse(
    @Expose
    @SerializedName("status")
    val status: String,

    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("questionData")
    val data:String?
)