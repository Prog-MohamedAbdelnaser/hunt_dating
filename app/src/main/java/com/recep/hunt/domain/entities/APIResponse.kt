package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName
import com.recep.hunt.domain.entities.EntitiesConstants.MESSAGE
import com.recep.hunt.domain.entities.EntitiesConstants.PAYLOAD
import com.recep.hunt.domain.entities.EntitiesConstants.STATUS

private const val SUCCESS_STATUS = "success"

private const val FAILD_STATUS ="faild"

data class APIResponse<P>(
    @SerializedName(STATUS) var status: String,
    @SerializedName(MESSAGE) var message: String,
    @SerializedName(PAYLOAD) var payload: P?) {

    fun isSuccessful(): Boolean {
        return status == SUCCESS_STATUS
    }

}