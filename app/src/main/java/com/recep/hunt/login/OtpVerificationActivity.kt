package com.recep.hunt.login

import android.animation.ObjectAnimator
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.Window
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.mukesh.OtpView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Custom_Alert_Dialog
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor

class OtpVerificationActivity : AppCompatActivity() {

    private var pStatus = 0
    private val handler = Handler()
    private lateinit var otpPinView : OtpView
    private lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        init()
    }
    private fun init(){
        progressBar = find(R.id.otp_progressBar)
        otpPinView = find(R.id.otp_pin_view)
        setupProgressTimer()

        resend_otp_btn.setOnClickListener {
            showResendOtpAlert()
        }

        otpPinView.setOtpCompletionListener {
            launchActivity<SocialLoginActivity>()
        }


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
        val res = resources
        val drawable = res.getDrawable(R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 100
        Thread(Runnable {
            while (pStatus < 100) {
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
}
