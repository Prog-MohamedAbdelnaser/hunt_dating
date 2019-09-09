package com.recep.hunt.notifications

import com.recep.hunt.R


/**
 * Created by Rishabh Shukla
 * on 2019-09-07
 * Email : rishabh1450@gmail.com
 */

data class NotificationsModel(val notification:String,
                              val userName:String,
                              val userImage:Int,
                              val notificationTime:String)

object NotificationsViewModel {
    val data = ArrayList<NotificationsModel>()
    public fun getNotifications():ArrayList<NotificationsModel>{
        data.add(NotificationsModel("You and Ashley like each other!","Ashley Lulia", R.drawable.boy_casual_eyes,"5:45 PM"))
        data.add(NotificationsModel("Somebody on same location wants to say Hello!","Anonymous", R.drawable.boy_casual_eyes,"3:45 AM"))
        data.add(NotificationsModel("You and Shirely like each other!","Shirely Hayes", R.drawable.boy_casual_eyes,"5:45 PM"))
        data.add(NotificationsModel("You and Stella like each other!","Stella Rosie", R.drawable.boy_casual_eyes,"5:45 PM"))
        data.add(NotificationsModel("You and Stella like each other!","Ashley Lulia", R.drawable.boy_casual_eyes,"5:45 PM"))
        data.add(NotificationsModel("You and Stella like each other!","Ashley Lulia", R.drawable.boy_casual_eyes,"5:45 PM"))
        return data
    }
    fun clearNotifications(){
        data.clear()
    }
}