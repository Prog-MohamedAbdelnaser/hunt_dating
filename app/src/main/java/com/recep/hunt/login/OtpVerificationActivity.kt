package com.recep.hunt.login

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.goodiebag.pinview.Pinview
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.model.LoginModel
import com.recep.hunt.model.login.LoginResponse
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SMSReceiver
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6
    }

    private var pStatus = 0
    private var pStatusVisible = 60
    private lateinit var cl_progressbar: ConstraintLayout
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var phoneNumber = ""
    private var countryCode = ""
    private var otp = ""
    private var verificationId = ""
    private val handler = Handler()
    private lateinit var otpPinView: Pinview
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var verifyingDialog: KProgressHUD
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var dialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        mAuth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signIn(p0)

                verifyingDialog.dismiss()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                verifyingDialog.dismiss()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verifyingDialog.dismiss()
                Log.e("OnCodeSent", "OTP : $p0")
                verificationId = p0
                verifyingDialog.dismiss()
                otp = p0

                resend_otp_btn.text = "Didn't receive the code? Please wait..."
                resend_otp_btn.setTextColor(resources.getColor(R.color.light_grey))
                resend_otp_btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0F)
                progressBar.visibility = View.VISIBLE
                otp_progrss_txt.visibility = View.VISIBLE
                we_will_send_you_otp_tv.visibility = View.GONE
                send_otp_again_btn.visibility = View.GONE
                otpPinView.setPinBackgroundRes(R.drawable.otp_pin_bg)
                try {
                    otpPinView.clearValue()
                    we_will_send_you_otp_tv.visibility = View.GONE
                    send_otp_again_btn.visibility = View.GONE
                } catch (e: Exception) {
                }


            }
        }
        try {
            //11.10.2019
            verificationId = intent.getStringExtra(WelcomeScreenActivity.verificationIdKey)
            otp = intent.getStringExtra(WelcomeScreenActivity.otpKey)
            countryCode = intent.getStringExtra(WelcomeScreenActivity.countryCodeKey)
            phoneNumber = intent.getStringExtra(WelcomeScreenActivity.numberKey)

            verify(phoneNumber, countryCode)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        init()
    }


    private fun init() {


        verifyingDialog = Helpers.showDialog(
            this@OtpVerificationActivity,
            this@OtpVerificationActivity,
            "Verifying"
        )
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar = find(R.id.cl_progress_bar)
        otpPinView = find(R.id.otp_pin_view)
        setupProgressTimer()
        otpPinView.requestFocus()

        we_will_send_you_otp_tv.visibility = View.GONE
        send_otp_again_btn.visibility = View.GONE

        toolbar.setNavigationOnClickListener {
            finish()
        }

        otpPinView.setPinViewEventListener { pinview, fromUser ->
            verify(phoneNumber, countryCode)
        }

        //Start Receiving SMS
        val client = SmsRetriever.getClient(this)
        val retriever = client.startSmsRetriever()
        retriever.addOnSuccessListener {
            val listener = object : SMSReceiver.Listener {
                override fun onSMSReceived(pin: String) {
                    // HERE you have the pin and can call your server to check. =)
                    otpPinView.value = pin
                }

                override fun onTimeOut() {
                    //TimeOut
                }
            }

        }
        retriever.addOnFailureListener {
            //Problem to start listener
            Log.d("Error : ", "Can't start receiver")
        }


    }

    //Resend OTP Alert Card Show
    private fun showResendOtpAlert() {

        resend_otp_btn.text = "Didn't receive the code yet ?"
        resend_otp_btn.setTextColor(resources.getColor(R.color.app_text_black))
        resend_otp_btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F)

        we_will_send_you_otp_tv.visibility = View.VISIBLE
        send_otp_again_btn.visibility = View.VISIBLE

        progressBar.visibility = View.GONE
        otp_progrss_txt.visibility = View.GONE
        val textView = find<TextView>(R.id.we_will_send_you_otp_tv)
        val styledText =
            "We will send you another six digit OTP on <br> <font color='#F64C1F'>\"$countryCode $phoneNumber\"</font>"
        textView.text = Html.fromHtml(styledText)

        val sendOtpAgainBtn = find<Button>(R.id.send_otp_again_btn)
        sendOtpAgainBtn.setOnClickListener {
            //            dialog.dismiss()
            progressBar.visibility = View.VISIBLE
            otp_progrss_txt.visibility = View.VISIBLE
            we_will_send_you_otp_tv.visibility = View.GONE

            send_otp_again_btn.visibility = View.GONE
            pStatus = 0
            pStatusVisible = 60
            resendOtp()

        }
//        dialog.show()

    }

    //Setup ProgressTimer
    private fun setupProgressTimer() {
//        val styledText = "We will send you six digit <br> OTP on <font color='red'>$countryCode $phoneNumber</font>."
//        resend_otp_btn.text = Html.fromHtml(styledText)
        val res = resources
        val drawable = res.getDrawable(R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 60



        Thread(Runnable {
            while (pStatus < 60) {
                pStatus += 1
                pStatusVisible -= 1

                handler.post {
                    progressBar.progress = pStatus
                    otp_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {
                            showResendOtpAlert()
                        }
                    }
                }
                try {
                    Thread.sleep(1000)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }


        }).start()


    }

    private fun resendOtp() {
        pStatus = 0
        pStatusVisible = 60
        setupProgressTimer()

        verifyingDialog.show()
        verify(phoneNumber, countryCode)

    }

    private fun verify(number: String, countryCode: String) {
        verificationCallbacks(number, countryCode)
        val phoneNumber = "$countryCode$number"
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun verificationCallbacks(number: String, countryCode: String) {

    }


    private fun authenticate(number: String) {
        try {
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, number)
            signIn(credential)

        } catch (e: Exception) {
            Toast.makeText(
                this@OtpVerificationActivity,
                "Please enter correct otp",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signIn(credential: PhoneAuthCredential) {
        dialog =
            Helpers.showDialog(
                this@OtpVerificationActivity,
                this@OtpVerificationActivity,
                "Verifying OTP"
            )
        dialog.show()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("signInWithCredential", "signInWithCredential:success")
                    SharedPrefrenceManager.setIsOtpVerified(
                        this@OtpVerificationActivity,
                        Constants.isOTPVerified
                    )
                    SharedPrefrenceManager.setUserMobileNumber(
                        this@OtpVerificationActivity,
                        phoneNumber
                    )
                    otpPinView.setPinBackgroundRes(R.drawable.otp_pin_invalid_bg)
                    getDeviceToken()
                } else {
                    dialog.dismiss()
                    Log.w("Error : ", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        SharedPrefrenceManager.setIsOtpVerified(
                            this@OtpVerificationActivity,
                            !Constants.isOTPVerified
                        )
                        toast("Verification Failed")
                        //Get Last Pin View
                        otpPinView.setPinBackgroundRes(R.drawable.otp_pin_invalid_bg)
                        otpPinView.requestPinEntryFocus()
                            .setBackgroundResource(R.drawable.otp_pin_invalid_last_bg)
                    }
                }
            }
    }


    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            if (token != null) {
                var latitude: Double = 0.0
                var longitude: Double = 0.0

                SharedPrefrenceManager.setDeviceToken(this@OtpVerificationActivity, token)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        latitude = location?.latitude!!
                        longitude = location.longitude

                        var countryCode =
                            SharedPrefrenceManager.getUserCountryCode(this).replace("+", "")
                        val loginModel = LoginModel(
                            SharedPrefrenceManager.getUserMobileNumber(this),
                            countryCode,
                            1

                        )

                        val call = ApiClient.getClient.loginUser(loginModel)


                        call.enqueue(object : Callback<LoginResponse> {
                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                launchActivity<SocialLoginActivity>()
                                finish()
                                dialog.run { dismiss() }

                                Toast.makeText(
                                    this@OtpVerificationActivity,
                                    "Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {

                                var userInfo = response.body()?.data?.user
                                userInfo?.apply {
                                    SharedPrefrenceManager.setUserMobileNumber(
                                        this@OtpVerificationActivity,
                                        mobile_no
                                    )
                                    SharedPrefrenceManager.setProfileImg(
                                        this@OtpVerificationActivity,
                                        profile_pic
                                    )
                                    SharedPrefrenceManager.setUserCountry(
                                        this@OtpVerificationActivity,
                                        country
                                    )
                                    SharedPrefrenceManager.setUserCountryCode(
                                        this@OtpVerificationActivity,
                                        country_code
                                    )
                                    SharedPrefrenceManager.setUserFirstName(
                                        this@OtpVerificationActivity,
                                        first_name
                                    )
                                    SharedPrefrenceManager.setUserLastName(
                                        this@OtpVerificationActivity,
                                        last_name
                                    )
                                    SharedPrefrenceManager.setDeviceToken(
                                        this@OtpVerificationActivity,
                                        device_token
                                    )
                                    SharedPrefrenceManager.setUserEmail(
                                        this@OtpVerificationActivity,
                                        email
                                    )
                                    SharedPrefrenceManager.setUserDob(
                                        this@OtpVerificationActivity,
                                        dob
                                    )
                                    SharedPrefrenceManager.setUserGender(
                                        this@OtpVerificationActivity,
                                        gender
                                    )
                                }
                                response.body()?.data?.token?.let { it1 ->
                                    SharedPrefrenceManager.setUserToken(
                                        this@OtpVerificationActivity,
                                        it1
                                    )
                                }
                                if (response.body()?.status == 1) {
                                    launchActivity<HomeActivity>()
                                    SharedPrefrenceManager.setIsLoggedIn(this@OtpVerificationActivity, true)
                                    SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity, true)
                                } else {
                                    launchActivity<SocialLoginActivity>()
                                }
                                dialog.run { dismiss() }
                                finish()


                            }


                        })

                    }

                Log.e(TAG, "Device Token : $token")
            }
        }
    }

}
