package com.recep.hunt.swipe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
class SwipeUserModel(val title:String, val detail:String?, val images:ArrayList<String>?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(ClassLoader::class.java.classLoader) as ArrayList<String>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
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