package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteUser(
    @Expose
    @SerializedName("message")
    val message: String
)