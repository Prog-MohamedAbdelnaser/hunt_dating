package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class UpdateHuntBeginParams(

	@field:SerializedName("hunt_id")
	var huntId: Int? = null,

	@field:SerializedName("is_meet")
	val isMeet: String? = null,

	@field:SerializedName("is_extra_time")
	val isExtraTime: String? = null,

	@field:SerializedName("extra_time")
	val extraTime: String? = null
)