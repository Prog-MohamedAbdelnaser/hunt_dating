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
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find


class OtpVerificationActivity : AppCompatActivity() {

    private var pStatus = 0
    private lateinit var cl_progressbar : ConstraintLayout
    private lateinit var cl_resend_otp : ConstraintLayout
    private lateinit var sendButton : Button
    private val handler = Handler()
    private lateinit var otpPinView : Pinview
    private lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        init()
    }
    private fun init(){

        progressBar = find(R.id.otp_progressBar)
        cl_progressbar=find(R.id.cl_progress_bar)
        cl_resend_otp=find(R.id.cl_we_will_send_otp)
        otpPinView = find(R.id.otp_pin_view)
        sendButton=find(R.id.sendButton)
       // setupProgressTimer()
        showResendOption()

        resend_otp_btn.setOnClickListener {
            showResendOtpAlert()
        }

        sendButton.setOnClickListener{
            setupProgressTimer()

        }

        otpPinView.setPinViewEventListener{ pinview, fromUser ->
            launchActivity<SocialLoginActivity>()
        }


    }
    //Set Resend color Red
    private fun setSpannable(){
        val text1= "Didn't receive the code? "
        val text2= "Resend"
        val text3 = text1+text2
        val spannable = SpannableString(text3)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
            text1.length,
            text3.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        resend_otp_btn.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    //Resend OTP Alert Dialog
    private fun showResendOtpAlert(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.resend_otp_dialog_layout, null)
        val dialog = Dialog(this@OtpVerificationActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)
        val styledText = "We will send you another four digit <br> OTP on <font color='red'>+91 9650900979</font>."
        textView.text =  Html.fromHtml(styledText)
        dialog.show()

    }

    //Setup ProgressTimer
    private fun setupProgressTimer(){
        cl_progressbar.visibility = View.VISIBLE
        cl_resend_otp.visibility=View.GONE
        val res = resources
        val drawable = res.getDrawable(R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 60


        Thread(Runnable {
            while (pStatus < 60) {
                pStatus += 1

                handler.post {
                    progressBar.progress = pStatus

                    otp_progrss_txt.text =  pStatus.toString()
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

    private fun showResendOption() {

        cl_progressbar.visibility = View.GONE
        cl_resend_otp.visibility=View.VISIBLE
    }
}
