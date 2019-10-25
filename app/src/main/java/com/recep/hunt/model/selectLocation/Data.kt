package com.recep.hunt.model.selectLocation

import java.sql.Timestamp

data class Data (
    val location_id: String,
    val location_name: String,
    val user_id : Int,
    val status :String,
    val created_at : Timestamp,
    val updated_at :Timestamp ,
    val id : Int
)