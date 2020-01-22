package com.recep.hunt.domain.entities

import com.google.gson.annotations.SerializedName

data class BeginHuntLocation(

	@field:SerializedName("schedule")
	val schedule: Schedule? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("place")
	val place: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("match_user_id")
	val matchUserId: Int? = null
)