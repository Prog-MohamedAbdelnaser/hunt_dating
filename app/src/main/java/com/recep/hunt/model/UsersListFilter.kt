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
    @SerializedName("for_date")
    val for_date: String,

    @Expose
    @SerializedName("for_business")
    val for_business: String,

    @Expose
    @SerializedName("for_friendship")
    val for_friendship: String

)