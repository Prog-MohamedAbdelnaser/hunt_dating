package com.recep.hunt.model.notification

data class NotificationResponse(
    val `data`: Any,
    val message: String,
    val status: Int
)