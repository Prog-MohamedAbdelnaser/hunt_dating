package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MakeUserOnline(
    @Expose
    @SerializedName("is_online")
    val is_online: Boolean
)