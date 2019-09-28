package com.recep.hunt.inviteFriend.model

import android.graphics.Bitmap


/**
 * Created by Rishabh Shukla
 * on 2019-09-19
 * Email : rishabh1450@gmail.com
 */

data class ContactHistoryModel(
    val userId: String,
    val userName: String,
    val userNumber: String,
    val userImg: Bitmap?
)