package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportUser(
    @Expose
    @SerializedName("reported_user")
    val reported_user: String,

    @Expose
    @SerializedName("reported_reason")
    val reported_reason: String
)