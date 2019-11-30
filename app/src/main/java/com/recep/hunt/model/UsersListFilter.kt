package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UsersListFilter(
    @Expose
    @SerializedName("location_id")
    val location_id: String,

    @Expose
    @SerializedName("age")
    val age: String,

    @Expose
    @SerializedName("date")
    val for_date: String,

    @Expose
    @SerializedName("bussiness")
    val for_business: String,

    @Expose
    @SerializedName("friendship")
    val for_friendship: String

)