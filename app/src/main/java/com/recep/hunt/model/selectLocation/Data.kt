package com.recep.hunt.model.selectLocation

import java.sql.Timestamp

data class Data (
    val location_id: String,
    val location_name: String,
    val user_id : Int,
    val status :String,
    val created_at : String,
    val updated_at :String ,
    val id : Int
)