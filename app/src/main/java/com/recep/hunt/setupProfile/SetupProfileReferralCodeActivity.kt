package com.recep.hunt.setupProfile

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.DownloadProgressListener
import com.kaopiz.kprogresshud.KProgressHUD
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants
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
import java.io.*

class SetupProfileReferralCodeActivity : AppCompatActivity() {

    private lateinit var dialog: KProgressHUD
    private val REQUEST_CODE_ASK_PERMISSIONS=101
    private var avatarFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_referral_code)
        AndroidNetworking.initialize(applicationContext)
        dialog = Helpers.showDialog(this, this, "Processing")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat
                .requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_ASK_PERMISSIONS);
        }
        else{
            init()

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            REQUEST_CODE_ASK_PERMISSIONS->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    init()
                }
                else{
                    Helpers.showErrorSnackBar(this, "Please allow location access!", "Please allow for location")

                }
            }
        }

    }
    private fun init() {
        avatarFilePath = intent.getStringExtra(Constants.IMGURI)

        tvSkip.setOnClickListener {
            SharedPrefrenceManager.setRefrencecode(this@SetupProfileReferralCodeActivity, "")
            registerUser()
        }
        ivBack.onClick {
            onBackPressed()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        check_code_btn.setOnClickListener {
            if (edtReferelCode.text.toString().equals("huntwelcome")) {
                SharedPrefrenceManager.setRefrencecode(
                    this@SetupProfileReferralCodeActivity,
                    edtReferelCode.text.toString()
                )
                registerUser()
            } else {
                showTryAgainAlert()
            }
        }
    }

    private fun registerUser() {

        dialog.show()

        if (checkWriteExternalPermission()) {
            val filePath = Environment.getExternalStorageDirectory().absolutePath
            val fileName = "abc.jpg"

            val file = File(filePath, fileName)

            if (!SharedPrefrenceManager.getIsFromSocial(this)) {
                if (file.exists())
                    file.delete()

                AndroidNetworking.download(
                    SharedPrefrenceManager.getProfileImg(this),
                    filePath,
                    fileName)
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build().setDownloadProgressListener(object : DownloadProgressListener {
                            override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
                            }
                        }).startDownload(object : DownloadListener {
                            override fun onDownloadComplete() {
                                val builder = createMultiPartBuilder(file)
                                makeApiCall(builder.build())
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("error", anError?.message)
                            }


                        })

            } else {
                val fileData =
                    StringToBitmap(SharedPrefrenceManager.getProfileImg(this@SetupProfileReferralCodeActivity))

                try {

                    val file = File(Uri.parse(avatarFilePath).path)
                    Logger.d("fileData size = ${file.length()}")
                    val builder = createMultiPartBuilder(file)
                    makeApiCall(builder.build())

                } catch (e: Exception) {
                    Logger.e(e, "registerUser")
                }
            }
        } else {
            Toast.makeText(
                this@SetupProfileReferralCodeActivity,
                "Please allow External Storage permission",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createMultiPartBuilder(file : File) : MultipartBody.Builder{

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
            SharedPrefrenceManager.getUserMobileNumber(this@SetupProfileReferralCodeActivity).replace("+",""))
        builder.addFormDataPart(
            "country_code",
            SharedPrefrenceManager.getUserCountryCode(this@SetupProfileReferralCodeActivity)
        )
        builder.addFormDataPart(
            "gender",
            SharedPrefrenceManager.getUserGender(this@SetupProfileReferralCodeActivity)
        )
        builder.addFormDataPart(
            "dob",
            SharedPrefrenceManager.getUserDob(this@SetupProfileReferralCodeActivity)
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
        builder.addFormDataPart(
            "device_id",
            SharedPrefrenceManager.getDeviceToken(this@SetupProfileReferralCodeActivity)
        )
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
                RequestBody.create(MediaType.parse("multipart/form-data"),
                    file)
            )
        }

        return builder
    }

    private fun showTryAgainAlert() {
        val ll = LayoutInflater.from(this).inflate(R.layout.try_again_dailog_layout, null)
        val dialog = Dialog(this@SetupProfileReferralCodeActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)
        textView.text = "your given referral code is not correct,\n please enter a valid code."
        val sendOtpAgainBtn = dialog.find<Button>(R.id.try_again_btn)
        sendOtpAgainBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun StringToBitmap(img: String): Bitmap? {
        var bitmap: Bitmap? = null
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
        }
        return bitmap
    }

    fun makeApiCall(requestBody: RequestBody) {
        val call = ApiClient.getClient.createUser(requestBody)
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(
                    this@SetupProfileReferralCodeActivity,
                    "Somthing want wrong,Please Try ageain",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                if (response.isSuccessful) {
                    dialog.dismiss()
                    var token = ""
                    response.body()?.let {
                        token = it.data.token
                        if (!TextUtils.isEmpty(token)) {
                            SharedPrefrenceManager.setUserToken(
                                this@SetupProfileReferralCodeActivity,
                                             token
                            )
                            SharedPrefrenceManager.setRefrencecode(
                                this@SetupProfileReferralCodeActivity,
                                edtReferelCode.text.toString()
                            )
                            SharedPrefrenceManager.setSwipeCount(this@SetupProfileReferralCodeActivity, 0.toString())
                            launchActivity<SetupProfileCompletedActivity> { }
                        }                            //do token related code and also store user json

                    }
                } else {
                    dialog.dismiss()
                    val data = response.errorBody()?.string()
                    val mJsonObject = JSONObject(data)
                    val mJsonObjectMessage = mJsonObject.optString("message")
                    Toast.makeText(this@SetupProfileReferralCodeActivity,mJsonObjectMessage,Toast.LENGTH_LONG).show()
//                    launchActivity<SetupProfileCompletedActivity> { }

                }
            }
        })
    }

    private fun checkWriteExternalPermission(): Boolean {
        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res = this.checkCallingOrSelfPermission(permission)
        return (res == PackageManager.PERMISSION_GRANTED)
    }
}