package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NearestLocation(
    @Expose
    @SerializedName("lat")
    val lat: Double,

    @Expose
    @SerializedName("lang")
    val lang: Double


)