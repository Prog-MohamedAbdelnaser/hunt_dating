package com.recep.hunt.swipe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.recep.hunt.model.usersList.Data

@SuppressLint("ParcelCreator")
class SwipeUserModel(val id:Int, val locationName:String, val firstName:String, val age:Int, val title:String, val detail:String?, val images:ArrayList<String>?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(ClassLoader::class.java.classLoader) as ArrayList<String>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(locationName)
        parcel.writeString(firstName)
        parcel.writeInt(age)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SwipeUserModel> {
        override fun createFromParcel(parcel: Parcel): SwipeUserModel {
            return SwipeUserModel(parcel)
        }

        override fun newArray(size: Int): Array<SwipeUserModel?> {
            return arrayOfNulls(size)
        }
    }

}