package com.recep.hunt.profile.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Rishabh Shukla
 * on 2019-08-30
 * Email : rishabh1450@gmail.com
 */

@Entity(tableName = "user")
data class User(
    val firstName:String?,
    val lastName:String?,

    val email:String?,
    val gender:String?,

    @ColumnInfo(name = "user_dob")
    val dateOfBirth:String?,
    val userImage:String?,
    val lookingFor:String,
    val interestedIn:String

){
    @PrimaryKey(autoGenerate = true)
    var userId:Int = 0

}

