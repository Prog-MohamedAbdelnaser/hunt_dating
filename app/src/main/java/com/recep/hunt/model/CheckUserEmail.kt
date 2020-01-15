package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckUserEmail(
    @Expose
    @SerializedName("email")
    val email: String
)
