package com.recep.hunt.model.createTicket

import java.sql.Timestamp

data class Data(
    val user_id: Int,
    val name: String,
    val ticket_id:String ,
    val updated_at : Timestamp ,
    val created_at : Timestamp ,
    val id : Int
)