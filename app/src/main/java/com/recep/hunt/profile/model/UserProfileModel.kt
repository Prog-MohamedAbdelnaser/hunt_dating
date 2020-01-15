package com.recep.hunt.profile.model

data class UserProfileModel(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val email_verified_at: String,
    val mobile_no:String,
    val country_code:String,
    val gender:String,
    val dob:String,
    val profile_pic:String,
    val referal_code:String,
    val country:String,
    val start_date:String,
    val lat:String,
    val lang:String,
    val for_date:String,
    val for_friendship:String ,
    val created_at:String ,
    val updated_at:String ,
    val is_notification_email:String ,
    val is_notification_push:String

)