package com.recep.hunt.notifications

import android.content.Context
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.notification.NotificationResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Rishabh Shukla
 * on 2019-09-07
 * Email : rishabh1450@gmail.com
 */

data class NotificationsModel(val notification:String,
                              val userName:String,
                              val userImage:String,
                              val notificationTime:String)

object NotificationsViewModel {
    val data = ArrayList<NotificationsModel>()
    public fun getNotifications(context: Context):ArrayList<NotificationsModel>{

        val call = ApiClient.getClient.getNotification(SharedPrefrenceManager.getUserToken(context))

        call.enqueue(object : Callback<NotificationResponse> {
            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(context, strErrorJson)) {
                        return
                    }
                }


                var responseData=response.body()?.data
                responseData?.forEach {
                    it.forEach{
                        var model=NotificationsModel(it.message,it.user.first_name,it.user.profile_pic,it.created_at)
                        data.add(model)
                    }

                }
            }

        })

        return data
    }
    fun clearNotifications(){
        data.clear()
    }
}