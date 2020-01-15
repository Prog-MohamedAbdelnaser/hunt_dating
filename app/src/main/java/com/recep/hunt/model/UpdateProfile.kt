package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateProfile(
    @Expose
    @SerializedName("about")
    val about: String,

    @Expose
    @SerializedName("job_title")
    val job_title: String,

    @Expose
    @SerializedName("company")
    val company: String,

    @Expose
    @SerializedName("hometown")
    val hometown: String,

    @Expose
    @SerializedName("school")
    val school: String,

    @Expose
    @SerializedName("height")
    val height: String,

    @Expose
    @SerializedName("gym")
    val gym: String,

    @Expose
    @SerializedName("education_level")
    val education_level: String,

    @Expose
    @SerializedName("drink")
    val drink: String,

    @Expose
    @SerializedName("smoke")
    val smoke: String,


    @Expose
    @SerializedName("pets")
    val pets: String,


    @Expose
    @SerializedName("kids")
    val kids: String,


    @Expose
    @SerializedName("zodiac")
    val zodiac: String,

    @Expose
    @SerializedName("politics")
    val politics: String,

    @Expose
    @SerializedName("religion")
    val religion: String,

    @Expose
    @SerializedName("vote")
    val vote: String,

    @Expose
    @SerializedName("gender")
    val gender: String
)