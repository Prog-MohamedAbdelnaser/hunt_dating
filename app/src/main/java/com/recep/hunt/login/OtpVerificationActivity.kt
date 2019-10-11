package com.recep.hunt.login

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
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
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : AppCompatActivity() {

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
    private lateinit var verifyingDialog : KProgressHUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        mAuth = FirebaseAuth.getInstance()
        init()
    }

    private fun init() {
        try {
            //11.10.2019
            verificationId = intent.getStringExtra(WelcomeScreenActivity.verificationIdKey)
            otp = intent.getStringExtra(WelcomeScreenActivity.otpKey)
            countryCode = intent.getStringExtra(WelcomeScreenActivity.countryCodeKey)
            phoneNumber = intent.getStringExtra(WelcomeScreenActivity.numberKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        verifyingDialog = Helpers.showDialog(this@OtpVerificationActivity,this@OtpVerificationActivity,"Verifying")
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar = find(R.id.cl_progress_bar)
        otpPinView = find(R.id.otp_pin_view)
        setupProgressTimer()

        resend_otp_btn.setOnClickListener {
            showResendOtpAlert()
        }

        otpPinView.setPinViewEventListener { pinview, fromUser ->
            authenticate(pinview.value)
//            launchActivity<SocialLoginActivity>()
//            Log.e("OTP","${pinview.value}")
//            Log.e("OTP FROM FIRE","${otp}")
//            authenticate(pinview.value)

        }


    }

    //Resend OTP Alert Dialog
    private fun showResendOtpAlert() {
        val ll = LayoutInflater.from(this).inflate(R.layout.resend_otp_dialog_layout, null)
        val dialog = Dialog(this@OtpVerificationActivity)

        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setCancelable(false)
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)
        val styledText = "We will send you another 6 digit <br> OTP on <font color='red'>$countryCode $phoneNumber</font>."
        textView.text = Html.fromHtml(styledText)

        val sendOtpAgainBtn = dialog.find<Button>(R.id.send_otp_again_btn)
        sendOtpAgainBtn.setOnClickListener {
            dialog.dismiss()
            resendOtp()
        }
        dialog.show()

    }

    //Setup ProgressTimer
    private fun setupProgressTimer() {
        val styledText = "We will send you 6 digit <br> OTP on <font color='red'>$countryCode $phoneNumber</font>."
        resend_otp_btn.text = Html.fromHtml(styledText)

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
        verifyingDialog.show();
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
        mCallbacks = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
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
                setupProgressTimer()
            }
        }
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
                    launchActivity<InfoYouProvideActivity>()
                    finish()
                } else {
                    dialog.dismiss()
                    Log.w("Error : ", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity, !Constants.isOTPVerified)
                        toast("Verification Failed")
                    }
                }
            }
    }

}
