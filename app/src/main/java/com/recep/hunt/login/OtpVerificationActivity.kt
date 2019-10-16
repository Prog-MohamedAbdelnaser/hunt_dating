package com.recep.hunt.login

import android.app.Dialog
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.goodiebag.pinview.Pinview
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.lang.Exception
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
    private var pStatus = 60
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
    private lateinit var verifyingDialog : KProgressHUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        mAuth = FirebaseAuth.getInstance()

        mCallbacks = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signIn(p0)
                verifyingDialog.dismiss()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                verifyingDialog.dismiss()
            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verifyingDialog.dismiss()
                Log.e("OnCodeSent","OTP : $p0")
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
                try{
                    otpPinView.clearValue()

                    setupProgressTimer()
                }
                catch (e:Exception)
                {}


            }
        }
        try {
            //11.10.2019
            verificationId = intent.getStringExtra(WelcomeScreenActivity.verificationIdKey)
            otp = intent.getStringExtra(WelcomeScreenActivity.otpKey)
            countryCode = intent.getStringExtra(WelcomeScreenActivity.countryCodeKey)
            phoneNumber = intent.getStringExtra(WelcomeScreenActivity.numberKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun init() {


        verifyingDialog = Helpers.showDialog(this@OtpVerificationActivity,this@OtpVerificationActivity,"Verifying")
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar = find(R.id.cl_progress_bar)
        otpPinView = find(R.id.otp_pin_view)
        setupProgressTimer()

        we_will_send_you_otp_tv.visibility = View.GONE
        send_otp_again_btn.visibility = View.GONE

        toolbar.setNavigationOnClickListener {
            finish()
        }

        otpPinView.setPinViewEventListener { pinview, fromUser ->
            authenticate(pinview.value)
//            launchActivity<SocialLoginActivity>()
//            Log.e("OTP","${pinview.value}")
//            Log.e("OTP FROM FIRE","${otp}")
//            authenticate(pinview.value)

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
        val styledText = "We will send you another six digit OTP on <br> <font color='#F64C1F'>\"$countryCode $phoneNumber\"</font>"
        textView.text = Html.fromHtml(styledText)

        val sendOtpAgainBtn = find<Button>(R.id.send_otp_again_btn)
        sendOtpAgainBtn.setOnClickListener {
//            dialog.dismiss()
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
            while (pStatus > 0) {
                pStatus -= 1
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
        pStatus = 60
        pStatusVisible = 60
        verifyingDialog.show()
        verify(phoneNumber, countryCode)

    }

    private fun verify(number:String,countryCode:String){
        verificationCallbacks(number, countryCode)
        val phoneNumber = "$countryCode$number"
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            120,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun verificationCallbacks(number: String, countryCode: String){

    }


    private fun authenticate(number: String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, number)
        signIn(credential)

    }

    private fun signIn(credential: PhoneAuthCredential) {
        val dialog: KProgressHUD =
            Helpers.showDialog(this@OtpVerificationActivity, this@OtpVerificationActivity, "Verifying OTP")
        dialog.show()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dialog.run { dismiss() }
                    Log.d("signInWithCredential", "signInWithCredential:success")
                    SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity, Constants.isOTPVerified)
                    SharedPrefrenceManager.setUserMobileNumber(this@OtpVerificationActivity, phoneNumber)
                    otpPinView.setPinBackgroundRes(R.drawable.otp_pin_invalid_bg)
                    launchActivity<SocialLoginActivity>()
                    finish()
                } else {
                    dialog.dismiss()
                    Log.w("Error : ", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity, !Constants.isOTPVerified)
                        toast("Verification Failed")
                        //Get Last Pin View
                        otpPinView.setPinBackgroundRes(R.drawable.otp_pin_invalid_bg)
                        otpPinView.requestPinEntryFocus().setBackgroundResource(R.drawable.otp_pin_invalid_last_bg)
                    }
                }
            }
    }

}
