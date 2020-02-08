package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class UpdateHuntBeginParams(

	@field:SerializedName("hunt_id")
	var huntId: Int,

	@field:SerializedName("is_meet")
	val isMeet: String,

	@field:SerializedName("is_extra_time")
	val isExtraTime: String,

	@field:SerializedName("extra_time")
	val extraTime: String
)