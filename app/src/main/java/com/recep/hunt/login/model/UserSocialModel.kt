package com.recep.hunt.login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */
@Parcelize
data class UserSocialModel(val userId:String,
                           val userImage:String,
                           val userName:String,
                           val userEmail:String
                           ):Parcelable