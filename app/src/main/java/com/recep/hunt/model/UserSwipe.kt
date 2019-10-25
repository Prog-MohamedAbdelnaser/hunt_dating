package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserSwipe(
    @Expose
    @SerializedName("match_user_id")
    val match_user_id: String,

    @Expose
    @SerializedName("swipe_action")
    val swipe_action: String


)