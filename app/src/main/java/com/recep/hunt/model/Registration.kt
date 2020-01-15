package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart

data class Registration(

    @Expose
    @SerializedName("first_name")
    val first_name: String,

    @Expose
    @SerializedName("last_name")
    val last_name: String,

    @Expose
    @SerializedName("mobile_no")
    val mobile_no: String,

    @Expose
    @SerializedName("country_code")
    val country_code: String,

    @Expose
    @SerializedName("gender")
    val gender: String,

    @Expose
    @SerializedName("dob")
    val dob: String,

    @Expose
    @SerializedName("profile_pic")
    val profile_pic: MultipartBody.Part,

    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("country")
    val country: String,

    @Expose
    @SerializedName("lat")
    val lat: String,

    @Expose
    @SerializedName("lang")
    val lang: String,

    @Expose
    @SerializedName("device_type")
    val device_type: String,

    @Expose
    @SerializedName("device_id")
    val device_id: String,

    @Expose
    @SerializedName("for_date")
    val for_date: String,

    @Expose
    @SerializedName("for_bussiness")
    val for_bussiness: String,


    @Expose
    @SerializedName("for_friendship")
    val for_friendship: String,


    @Expose
    @SerializedName("fb_id")
    val fb_id: String,


    @Expose
    @SerializedName("fb_token")
    val fb_token: String,

    @Expose
    @SerializedName("insta_id")
    val insta_id: String,


    @Expose
    @SerializedName("insta_token")
    val insta_token: String,

    @Expose
    @SerializedName("google_id")
    val google_id: String,


    @Expose
    @SerializedName("google_token")
    val google_token: String,

    @Expose
    @SerializedName("reference_code")
    val reference_code: String





    )