package com.recep.hunt.model.SubscriptinPlans

import java.sql.Timestamp

data class Data (
    val plan_name: String,
    val plan_id: String,
    val plan_description:String,
    val plan_amount : String,
    val plan_month : String,
    val id : Int ,
    val status :String ,
    val created_at :Timestamp ,
    val updated_at :Timestamp
)