package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String, // 46R6+X2 The Rocks, New South Wales
    @SerializedName("global_code")
    val globalCode: String // 4RRH46R6+X2
)