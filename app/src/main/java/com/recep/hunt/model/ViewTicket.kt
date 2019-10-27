package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ViewTicket(
    @Expose
    @SerializedName("ticket_id")
    val ticket_id : Int
)