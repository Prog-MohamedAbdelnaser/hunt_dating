package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.recep.hunt.R
import com.recep.hunt.model.Registration
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_referral_code.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.DownloadProgressListener
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.RegistrationModule.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            if(edtReferelCode.text.equals("huntwelcome"))
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
        val fileName="abc.png"
        var file=File(filePath,fileName)
        if(file.exists())
            file.delete()

        AndroidNetworking.download(SharedPrefrenceManager.getProfileImg(this),filePath,fileName)
                 .setTag("downloadTest")
                 .setPriority(Priority.MEDIUM)
                 .build().setDownloadProgressListener(object : DownloadProgressListener{
                override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
                }
            }).startDownload(object :DownloadListener{
                override fun onDownloadComplete() {

                    val filePart = MultipartBody.Part.createFormData("pics", file!!.getName(), RequestBody.create(
                        MediaType.parse("image/*"), file));

                    val registrationModel=Registration(
                        SharedPrefrenceManager.getUserFirstName(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserLastName(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserMobileNumber(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserEmail(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserGender(this@SetupProfileReferralCodeActivity),
                        filePart,
                        SharedPrefrenceManager.getUserDob(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserCountry(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserLatitude(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getUserLongitude(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity),
                        "",
                        SharedPrefrenceManager.getLookingForDate(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getLookingForBusniess(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getLookingForFriendship(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getFacebookId(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getFacebookLoginToken(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getInstagramId(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getInstagramLoginToken(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getGoogleId(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getGoogleLoginToken(this@SetupProfileReferralCodeActivity),
                        SharedPrefrenceManager.getRefrenceCode(this@SetupProfileReferralCodeActivity)
                    )


                    val call = ApiClient.getClient.createUser(registrationModel)

                    call.enqueue(object : Callback<RegistrationResponse> {
                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                            Toast.makeText(this@SetupProfileReferralCodeActivity,"Somthing want wrong,Please Try ageain",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>

                        ) {
                            SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity,edtReferelCode.text.toString())
                            launchActivity<SetupProfileCompletedActivity> {  }

                        }


                    })

                }

                override fun onError(anError: ANError?) {

                    Log.d("error","error")
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
