package com.recep.hunt.login

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.goodiebag.pinview.Pinview
import com.recep.hunt.R
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor


class OtpVerificationActivity : AppCompatActivity() {

    private var pStatus = 0
    private var pStatusVisible = 60
    private lateinit var cl_progressbar : ConstraintLayout
    private var phoneNumber = ""
//    private lateinit var cl_resend_otp : ConstraintLayout
//    private lateinit var sendButton : Button
    private val handler = Handler()
    private lateinit var otpPinView : Pinview
    private lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        init()
    }
    private fun init(){
        phoneNumber = intent.getStringExtra(LoginActivity.numberKey)
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar=find(R.id.cl_progress_bar)
//        cl_resend_otp=find(R.id.cl_we_will_send_otp)
        otpPinView = find(R.id.otp_pin_view)
//        sendButton=find(R.id.sendButton)
        setupProgressTimer()
//        showResendOption()

        resend_otp_btn.setOnClickListener {
            showResendOtpAlert()
        }

//        sendButton.setOnClickListener{
//            setupProgressTimer()
//
//        }

        otpPinView.setPinViewEventListener{ pinview, fromUser ->
            this.hideKeyboard()
            launchActivity<SocialLoginActivity>()
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
                    // Sleep for 200 milliseconds.
                    // Just to display the progress slowly
                    Thread.sleep(  60) //thread will take approx 1.5 seconds to finish

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

}
