package com.recep.hunt.swipe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.recep.hunt.model.usersList.BasicInfo
import com.recep.hunt.model.usersList.Data
import kotlinx.android.parcel.Parcelize

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(ClassLoader::class.java.classLoader) as ArrayList<String>,
        parcel.readParcelable(BasicInfo::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(locationName)
        parcel.writeString(firstName)
        parcel.writeInt(age)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeFloat(totalMatching)
        parcel.writeInt(totalMeeting)
        parcel.writeString(is_online)
        parcel.writeString(for_date)
        parcel.writeString(for_bussiness)
        parcel.writeString(for_friendship)
        parcel.writeList(images)
        parcel.writeParcelable(basicInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

//    companion object CREATOR : Parcelable.Creator<SwipeUserModel> {
//        override fun createFromParcel(parcel: Parcel): SwipeUserModel {
//            return SwipeUserModel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<SwipeUserModel?> {
//            return arrayOfNulls(size)
//        }
//    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<SwipeUserModel> {
            override fun createFromParcel(parcel: Parcel) = SwipeUserModel(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SwipeUserModel>(size)
        }
    }
}