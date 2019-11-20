package com.recep.hunt.model.notification

data class Data(
    val created_at: String,
    val from: String,
    val id: Int,
    val message: String,
    val status: String,
    val to: String,
    val updated_at: String
)