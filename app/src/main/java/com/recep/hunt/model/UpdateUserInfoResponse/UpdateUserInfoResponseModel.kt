package com.recep.hunt.model.UpdateUserInfoResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateUserInfoResponseModel(
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