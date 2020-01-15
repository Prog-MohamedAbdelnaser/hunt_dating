package com.recep.hunt.profile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/**
 * Created by Rishabh Shukla
 * on 2019-09-05
 * Email : rishabh1450@gmail.com
 */

@Entity(tableName = "ice_breaker_questions")
data class IceBreakerModel(
    val question:String,
    val date:String,
    val option1:String,
    val option2:String,
    val option3:String)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

