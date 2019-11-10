package com.recep.hunt.setupProfile

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
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
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.RegistrationModule.RegistrationResponse
import com.recep.hunt.utilis.Helpers
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
import java.io.FileOutputStream

class SetupProfileReferralCodeActivity : AppCompatActivity() {

    private lateinit var dialog : KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_referral_code)
        AndroidNetworking.initialize(getApplicationContext());
        dialog = Helpers.showDialog(this,this,"Processing")

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

        dialog.show()

        if(checkWriteExternalPermission()) {


            var file: File
            val filePath = Environment.getExternalStorageDirectory().absolutePath
            val fileName = "abc.jpg"

            if (!SharedPrefrenceManager.getIsFromSocial(this)) {


                file = File(filePath, fileName)


                if (file.exists())
                    file.delete()

                AndroidNetworking.download(
                    SharedPrefrenceManager.getProfileImg(this),
                    filePath,
                    fileName
                )
                    .setTag("downloadTest")
                    .setPriority(Priority.MEDIUM)
                    .build().setDownloadProgressListener(object : DownloadProgressListener {
                        override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
                        }
                    }).startDownload(object : DownloadListener {
                        override fun onDownloadComplete() {


                            val builder = MultipartBody.Builder()
                            builder.setType(MultipartBody.FORM)
                            builder.addFormDataPart(
                                "first_name",
                                SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "last_name",
                                SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "mobile_no",
                                SharedPrefrenceManager.getUserMobileNumber(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "country_code",
                                SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity).replace("+","")
                            )
                            builder.addFormDataPart(
                                "gender",
                                SharedPrefrenceManager.getUserGender(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart("dob", "1993-11-11")
//                    builder.addFormDataPart("profile_pic", "")
                            builder.addFormDataPart(
                                "email",
                                SharedPrefrenceManager.getUserEmail(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "country",
                                SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "lat",
                                SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "lang",
                                SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity)
                            )
//                    builder.addFormDataPart("device_type", "android")
//                    builder.addFormDataPart("device_id", SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity))
                            builder.addFormDataPart(
                                "for_date",
                                SharedPrefrenceManager.getLookingForDate(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "for_bussiness",
                                SharedPrefrenceManager.getLookingForBusniess(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "for_friendship",
                                SharedPrefrenceManager.getLookingForFriendship(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "fb_id",
                                SharedPrefrenceManager.getFacebookId(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "fb_token",
                                SharedPrefrenceManager.getFacebookLoginToken(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "insta_id",
                                SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "insta_token",
                                SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "google_id",
                                SharedPrefrenceManager.getGoogleId(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "google_token",
                                SharedPrefrenceManager.getGoogleLoginToken(this@SetupProfileReferralCodeActivity)
                            )
                            builder.addFormDataPart(
                                "reference_code",
                                SharedPrefrenceManager.getRefrenceCode(this@SetupProfileReferralCodeActivity)
                            )

                            if (file != null && file.exists()) {
                                builder.addFormDataPart(
                                    "profile_pic",
                                    file.name,
                                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                                )
                            }

                            makeApiCall(builder.build())


                        }

                        override fun onError(anError: ANError?) {
                            Log.d("error", anError?.message)
                        }


                    })

            } else {
                val fileData =
                    StringToBitmap(SharedPrefrenceManager.getProfileImg(this@SetupProfileReferralCodeActivity))
                file = File(filePath, fileName)
                try {
                    val out = FileOutputStream(file);
                    fileData?.let {
                        it.compress(Bitmap.CompressFormat.PNG, 90, out);
                    }
                    out.flush();
                    out.close();


                    val builder = MultipartBody.Builder()
                    builder.setType(MultipartBody.FORM)
                    builder.addFormDataPart(
                        "first_name",
                        SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity)
                    )
                    builder.addFormDataPart(
                        "last_name",
                        SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity)
                    )
                    builder.addFormDataPart(
                        "mobile_no",
                        "2                                                                                                                             723"
                    )
                    builder.addFormDataPart(
                        "country_code",
                        SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity).replace("+","")
                    )
                    builder.addFormDataPart("gender", "Male")
                    builder.addFormDataPart("dob", "1993-11-11")
//                    builder.addFormDataPart("profile_pic", "")
                    builder.addFormDataPart("email", SharedPrefrenceManager.getUserEmail(this@SetupProfileReferralCodeActivity))
                    builder.addFormDataPart(
                        "country",
                        SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity)
                    )
                    builder.addFormDataPart(
                        "lat",
                        SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity)
                    )
                    builder.addFormDataPart(
                        "lang",
                        SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity)
                    )

                    try {
                        val out = FileOutputStream(file);
                        fileData?.let {
                            it.compress(Bitmap.CompressFormat.PNG, 90, out);
                        }
                        out.flush();
                        out.close();


                        val builder = MultipartBody.Builder()
                        builder.setType(MultipartBody.FORM)
                        builder.addFormDataPart(
                            "first_name",
                            SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "last_name",
                            SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart("mobile_no", "22723")
                        builder.addFormDataPart(
                            "country_code",
                            SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart("gender", SharedPrefrenceManager.getUserGender(this@SetupProfileReferralCodeActivity))
                        builder.addFormDataPart("dob", "1993-11-11")
//                    builder.addFormDataPart("profile_pic", "")
                        builder.addFormDataPart("email", SharedPrefrenceManager.getUserEmail(this@SetupProfileReferralCodeActivity))
                        builder.addFormDataPart(
                            "country",
                            SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "lat",
                            SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "lang",
                            SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity)
                        )
//                    builder.addFormDataPart("device_type", "android")
                    builder.addFormDataPart("device_id", SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity))
                        builder.addFormDataPart(
                            "for_date",
                            SharedPrefrenceManager.getLookingForDate(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "for_bussiness",
                            SharedPrefrenceManager.getLookingForBusniess(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "for_friendship",
                            SharedPrefrenceManager.getLookingForFriendship(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "fb_id",
                            SharedPrefrenceManager.getFacebookId(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "fb_token",
                            SharedPrefrenceManager.getFacebookLoginToken(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "insta_id",
                            SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "insta_token",
                            SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "google_id",
                            SharedPrefrenceManager.getGoogleId(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "google_token",
                            SharedPrefrenceManager.getGoogleLoginToken(this@SetupProfileReferralCodeActivity)
                        )
                        builder.addFormDataPart(
                            "reference_code",
                            SharedPrefrenceManager.getRefrenceCode(this@SetupProfileReferralCodeActivity)
                        )

                        if (file != null && file.exists()) {
                            builder.addFormDataPart(
                                "profile_pic",
                                file.name,
                                RequestBody.create(MediaType.parse("multipart/form-data"), file)
                            )
                        }
                        makeApiCall(builder.build())

                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }


            }
        }

        else{
            Toast.makeText(this@SetupProfileReferralCodeActivity,"Please allow External Storage permission",Toast.LENGTH_SHORT).show()
        }
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
    fun StringToBitmap(img: String): Bitmap? {
        var bitmap: Bitmap? =null
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size);
        }
        return bitmap
    }
    fun makeApiCall(requestBody : RequestBody)
    {
        val call = ApiClient.getClient.createUser(requestBody)
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(this@SetupProfileReferralCodeActivity,"Somthing want wrong,Please Try ageain",Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                if(response.code() == 200){
                    dialog.dismiss()
                    var token=""
                    response.body()?.let{
                        token = it.data.token
                        if(!TextUtils.isEmpty(token)){
                            SharedPrefrenceManager.setUserToken(this@SetupProfileReferralCodeActivity,token)
                            SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity,edtReferelCode.text.toString())
                            launchActivity<SetupProfileCompletedActivity> {  }
                        }                            //do token related code and also store user json

                    }
                }else
                {
                    dialog.dismiss()
                    val data = response.errorBody()?.string()
                    val mJsonObject =  JSONObject(data)
                    val mJsonObjectMessage = mJsonObject.optString("message")
//                    Toast.makeText(this@SetupProfileReferralCodeActivity,mJsonObjectMessage,Toast.LENGTH_LONG).show()
                    launchActivity<SetupProfileCompletedActivity> {  }

                }
            }
        })
    }
    private fun checkWriteExternalPermission():Boolean
    {
        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        val res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}