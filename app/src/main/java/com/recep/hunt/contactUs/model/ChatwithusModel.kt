package com.recep.hunt.contactUs.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rishabh Shukla
 * on 2019-09-08
 * Email : rishabh1450@gmail.com
 */
@Parcelize
data class ChatwithusModel(val userImage:Int,
                           val userName:String,
                           val userMessage:String,
                           val messageTime:String):Parcelable