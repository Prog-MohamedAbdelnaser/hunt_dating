package com.recep.hunt.model.notification

data class Data(
    val created_at: String,
    val from_user_id: Int,
    val id: Int,
    val message: String,
    val status: String,
    val to_user_id: Int,
    val updated_at: String,
    val user: User,
    val user_name: String,
    val user_profile_pic: String
)