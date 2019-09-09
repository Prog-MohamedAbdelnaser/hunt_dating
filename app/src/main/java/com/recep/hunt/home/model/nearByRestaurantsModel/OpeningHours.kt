package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean // false
)