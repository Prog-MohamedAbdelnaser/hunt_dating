package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class Schedule(

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("hunt_id")
	val huntId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)