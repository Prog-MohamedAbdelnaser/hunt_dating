package com.recep.hunt.models


import com.google.gson.annotations.SerializedName

data class CountryCodeModel(
    @SerializedName("code")
    val code: String, // VI
    @SerializedName("dial_code")
    val dialCode: String?, // +1 340
    @SerializedName("name")
    val name: String // Virgin Islands, U.S.
)