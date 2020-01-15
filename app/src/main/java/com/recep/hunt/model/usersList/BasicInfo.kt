package com.recep.hunt.model.usersList

import android.os.Parcel
import android.os.Parcelable

data class BasicInfo  (
    val about : String,
    val job_title : String,
    val company : String,
    val hometown : String,
    val school : String,
    val height : String,
    val gym : String,
    val education_level : String,
    val drink : String,
    val smoke : String,
    val pets : String,
    val kids : String,
    val zodiac : String,
    val politics : String,
    val religion : String,
    val vote : String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(about)
        parcel.writeString(job_title)
        parcel.writeString(company)
        parcel.writeString(hometown)
        parcel.writeString(school)
        parcel.writeString(height)
        parcel.writeString(gym)
        parcel.writeString(education_level)
        parcel.writeString(drink)
        parcel.writeString(smoke)
        parcel.writeString(pets)
        parcel.writeString(kids)
        parcel.writeString(zodiac)
        parcel.writeString(politics)
        parcel.writeString(religion)
        parcel.writeString(vote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasicInfo> {
        override fun createFromParcel(parcel: Parcel): BasicInfo {
            return BasicInfo(parcel)
        }

        override fun newArray(size: Int): Array<BasicInfo?> {
            return arrayOfNulls(size)
        }
    }
}