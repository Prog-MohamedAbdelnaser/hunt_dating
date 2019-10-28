package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.DownloadProgressListener
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.RegistrationModule.RegistrationResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_referral_code.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.find

import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SetupProfileReferralCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_referral_code)
        AndroidNetworking.initialize(getApplicationContext());
        init()
    }

    private fun init() {
        tvSkip.setOnClickListener {
            SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity,"")
            registerUser()
        }
        ivBack.onClick {
            onBackPressed()
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        check_code_btn.setOnClickListener {

            if(edtReferelCode.text.toString().equals("huntwelcome"))
            {
                SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity,edtReferelCode.text.toString())
                registerUser()
            }
            else
            {
                showTryAgainAlert()
            }


        }
    }

    private fun registerUser() {

        val filePath=Environment.getExternalStorageDirectory().absolutePath
        val fileName="abc.jpg"
        var file=File(filePath,fileName)
        if(file.exists())
            file.delete()

        AndroidNetworking.download(SharedPrefrenceManager.getProfileImg(this),filePath,fileName)
                 .setTag("downloadTest")
                 .setPriority(Priority.MEDIUM)
                 .build().setDownloadProgressListener(object : DownloadProgressListener{
                override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
                }
            }).
                startDownload(object :DownloadListener{
                override fun onDownloadComplete() {

//                    val filePart = MultipartBody.Part.createFormData("pics", file!!.getName(), RequestBody.create(
//                        MediaType.parse("image/*"), file));
//
//                    val registrationModel=Registration(
//                        SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity),
//                        "8212356789",
//                        SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getUserGender(this@SetupProfileReferralCodeActivity),
//                        "1993-11-11",
//                        filePart,
//                        "abc@abc.com",
//                        SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity),
//                        "",
//                        SharedPrefrenceManager.getLookingForDate(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getLookingForBusniess(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getLookingForFriendship(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getFacebookId(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getFacebookLoginToken(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getInstagramLoginToken(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getGoogleId(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getGoogleLoginToken(this@SetupProfileReferralCodeActivity),
//                        SharedPrefrenceManager.getRefrenceCode(this@SetupProfileReferralCodeActivity)
//                    )


                    val builder = MultipartBody.Builder()
                    builder.setType(MultipartBody.FORM)
                    builder.addFormDataPart("first_name", SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("last_name", SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("mobile_no", "9909860786")
                    builder.addFormDataPart("country_code", SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("gender", "male")
                    builder.addFormDataPart("dob", "1993-11-11")
//                    builder.addFormDataPart("profile_pic", "")
                    builder.addFormDataPart("email", "abc@abc.com")
                    builder.addFormDataPart("country", SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("lat", SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("lang", SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity))
//                    builder.addFormDataPart("device_type", "android")
//                    builder.addFormDataPart("device_id", SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("for_date", SharedPrefrenceManager.getLookingForDate(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("for_bussiness", SharedPrefrenceManager.getLookingForBusniess(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("for_friendship", SharedPrefrenceManager.getLookingForFriendship(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("fb_id", SharedPrefrenceManager.getFacebookId(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("fb_token", SharedPrefrenceManager.getFacebookLoginToken(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("insta_id", SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("insta_token", SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("google_id", SharedPrefrenceManager.getGoogleId(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("google_token", SharedPrefrenceManager.getGoogleLoginToken(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart("reference_code", SharedPrefrenceManager.getRefrenceCode(this@SetupProfileReferralCodeActivity))

                    if(file!=null && file.exists()) {
                        builder.addFormDataPart(
                            "profile_pic",
                            file.name,
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        )
                    }

                    val call = ApiClient.getClient.createUser(builder.build())

                    call.enqueue(object : Callback<RegistrationResponse> {
                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                            Toast.makeText(this@SetupProfileReferralCodeActivity,"Somthing want wrong,Please Try ageain",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>

                        ) {
                            if(response.code() == 200){
                                val mJsonObject =  JSONObject(response.body().toString())
                                val mJsonObjectData= mJsonObject.optJSONObject("data")

                                if(mJsonObjectData!=null){
                                    val mJsonObjectUser = mJsonObjectData.optJSONObject("user")
                                    val token = mJsonObjectData.optString("token")
                                    if(!TextUtils.isEmpty(token)){
                                        //do token related code and also store user json
                                        SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity,edtReferelCode.text.toString())
                                        launchActivity<SetupProfileCompletedActivity> {  }
                                    }
                                }

                            }else{
                                val data = response.errorBody()?.string()
                                val mJsonObject =  JSONObject(data)
                                val mJsonObjectMessage = mJsonObject.optString("message")
                                Toast.makeText(this@SetupProfileReferralCodeActivity,mJsonObjectMessage,Toast.LENGTH_LONG).show()
                            }
                        }
                    })

                }

                override fun onError(anError: ANError?) {
                    Log.d("error",anError?.message)
                }


            })




    }

    private fun showTryAgainAlert(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.try_again_dailog_layout, null)
        val dialog = Dialog(this@SetupProfileReferralCodeActivity)

        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)


        textView.text =  "your given referral code is not correct,\n please enter a valid code."

        val sendOtpAgainBtn = dialog.find<Button>(R.id.try_again_btn)

        sendOtpAgainBtn.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()

    }






}
