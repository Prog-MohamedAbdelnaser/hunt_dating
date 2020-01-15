package com.recep.hunt.model.notification

data class NotificationResponse(
    val data: List<List<Data>>,
    val status: Boolean
)