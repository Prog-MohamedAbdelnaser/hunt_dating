package com.recep.hunt.login

import android.app.Dialog
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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
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


class OtpVerificationActivity : AppCompatActivity() {

    private var pStatus = 0
    private var pStatusVisible = 60
    private lateinit var cl_progressbar : ConstraintLayout
    private var phoneNumber = ""
    private var otp = ""
    private var verificationId = ""
    private val handler = Handler()
    private lateinit var otpPinView : Pinview
    private lateinit var progressBar : ProgressBar
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        mAuth = FirebaseAuth.getInstance()
        init()
    }

    private fun init(){
        verificationId = intent.getStringExtra(LoginActivity.verificationIdKey)
        otp = intent.getStringExtra(LoginActivity.otpKey)
        phoneNumber = intent.getStringExtra(LoginActivity.numberKey)
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar=find(R.id.cl_progress_bar)
        otpPinView = find(R.id.otp_pin_view)
        setupProgressTimer()

        resend_otp_btn.setOnClickListener {
            showResendOtpAlert()
        }

        otpPinView.setPinViewEventListener{ pinview, fromUser ->

            Log.e("OTP","${pinview.value}")
            Log.e("OTP FROM FIRE","${otp}")
            authenticate(pinview.value)

        }


    }
    //Resend OTP Alert Dialog
    private fun showResendOtpAlert(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.resend_otp_dialog_layout, null)
        val dialog = Dialog(this@OtpVerificationActivity)

        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setCancelable(false)
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)
        val styledText = "We will send you another four digit <br> OTP on <font color='red'>+91 $phoneNumber</font>."
        textView.text =  Html.fromHtml(styledText)

        val sendOtpAgainBtn = dialog.find<Button>(R.id.send_otp_again_btn)
        sendOtpAgainBtn.setOnClickListener {
            dialog.dismiss()
            resendOtp()
        }
        dialog.show()

    }

    //Setup ProgressTimer
    private fun setupProgressTimer(){
        val styledText = "We will send you four digit <br> OTP on <font color='red'>+91 $phoneNumber</font>."
        resend_otp_btn.text= Html.fromHtml(styledText)

        val res = resources
        val drawable = res.getDrawable(R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 60


        Thread(Runnable {
            while (pStatus < 60) {
                pStatus += 1
                pStatusVisible -=1

                handler.post {
                    progressBar.progress = pStatus
                    otp_progrss_txt.text =  pStatusVisible.toString()

                    if(pStatusVisible == 0){
                        runOnUiThread {
                            showResendOtpAlert()
                        }
                    }
                }
                try {
                    Thread.sleep(  600)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }


        }).start()




    }
    private fun resendOtp(){
        pStatus = 0
        pStatusVisible = 60
        setupProgressTimer()

    }


    private fun authenticate (number:String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, number)
        signIn(credential)

    }

    private fun signIn(credential: PhoneAuthCredential){
        val dialog : KProgressHUD = Helpers.showDialog(this@OtpVerificationActivity,this@OtpVerificationActivity,"Verifying OTP")
        dialog.show()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task: Task<AuthResult> ->
                if(task.isSuccessful){
                    dialog.run { dismiss() }
                    SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity,Constants.isOTPVerified)
                    SharedPrefrenceManager.setUserMobileNumber(this@OtpVerificationActivity,phoneNumber)
                    launchActivity<SocialLoginActivity>()
                }else{
                    dialog.dismiss()
                    Log.e("Error : ",task.result.toString())
                    SharedPrefrenceManager.setIsOtpVerified(this@OtpVerificationActivity,!Constants.isOTPVerified)
                    toast("Failed")
                }
            }
    }

}
