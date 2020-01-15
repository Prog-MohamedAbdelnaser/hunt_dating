package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double, // -33.857559
    @SerializedName("lng")
    val lng: Double // 151.2101217
)