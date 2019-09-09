package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class NearByRestaurantsModel(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("results")
    val nearByRestaurantsModelResults: ArrayList<NearByRestaurantsModelResults>,
    @SerializedName("status")
    val status: String // OK
)