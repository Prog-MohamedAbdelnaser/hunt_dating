package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class PushNotificationSingleUserParams(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)