package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public data class LoginModel(
    @Expose
    @SerializedName("mobile_no")
    val mobileNumber: String,
    @Expose
    @SerializedName("country_code")
    val country_code: String,
    @Expose
    @SerializedName("is_verify")
    val is_verify: Int

)