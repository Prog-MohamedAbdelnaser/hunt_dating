package com.recep.hunt.swipe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.recep.hunt.model.usersList.BasicInfo
import com.recep.hunt.model.usersList.Data
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SwipeUserModel(val id:Int,
                          val locationName:String,
                          val firstName:String,
                          val age:Int,
                          val title:String,
                          val detail:String?,
                          val totalMatching: Float,
                          val totalMeeting : Int,
                          val is_online:String,
                          val for_date:String,
                          val for_bussiness:String?,
                          val for_friendship:String,
                          val images:ArrayList<String>?,
                          val basicInfo: BasicInfo?
) : Parcelable