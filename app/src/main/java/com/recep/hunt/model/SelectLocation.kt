package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SelectLocation(
    @Expose
    @SerializedName("location_id")
    val location_id: String,

    @Expose
    @SerializedName("location_name")
    val location_name: String
)