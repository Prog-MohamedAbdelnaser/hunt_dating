package com.recep.hunt.inviteFriend.model

import com.recep.hunt.R


/**
 * Created by Rishabh Shukla
 * on 2019-09-19
 * Email : rishabh1450@gmail.com
 */

enum class InviteHistoryStatus{
    PENDING,
    APPROVED
}
data class InviteHistoryModel(val userName:String,
                              val userImage:Int = R.drawable.boy_casual_eyes,
                              val status:InviteHistoryStatus)