package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateTicket(
    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("image")
    val image: String,


    @Expose
    @SerializedName("audio")
    val audio: String


)