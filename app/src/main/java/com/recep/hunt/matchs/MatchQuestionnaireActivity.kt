package com.recep.hunt.matchs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import kotlinx.android.synthetic.main.activity_match_questionnaire.otp_progrss_txt
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.jetbrains.anko.find

class MatchQuestionnaireActivity : AppCompatActivity() {

    private var pStatus = 15
    private var pStatusVisible = 15
    private lateinit var cl_progressbar: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private val handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_questionnaire)
        progressBar = find(R.id.otp_progressBar)
        cl_progressbar = find(R.id.cl_progress_bar)


    }

    fun setProgressStart()
    {
        progressBar.visibility = View.VISIBLE
        otp_progrss_txt.visibility = View.VISIBLE
        setupProgressTimer()
    }

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
                            //Change ui
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

}
