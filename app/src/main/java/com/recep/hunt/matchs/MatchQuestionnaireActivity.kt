package com.recep.hunt.matchs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
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
        cl_progressbar = find(R.id.cl_progress_bar_two)


        check_code_btn.setOnClickListener {
            setpTwo.visibility=View.VISIBLE
            setpOne.visibility=View.GONE
            setProgressStart()
        }

        btn_yes.setOnClickListener {
            setpThree.visibility=View.GONE
            stepFour.visibility=View.VISIBLE

        }
        btn_goto_next_five.setOnClickListener {
            stepFour.visibility=View.GONE
            stepFive.visibility=View.VISIBLE

        }

        btn.setOnClickListener {
            stepFive.visibility=View.GONE
            stepSix.visibility=View.VISIBLE

        }

        idSubmit.setOnClickListener {
            tvStepSix.visibility=View.GONE
            var mIntent= Intent(this,LetsMeetActivity::class.java)
            startActivity(mIntent)

        }



    }

    fun goTonext(view: View)
    {
        setpThree.visibility=View.VISIBLE
        setpTwo.visibility=View.GONE
    }

    fun setProgressStart()
    {
        progressBar.visibility = View.VISIBLE
        two_progrss_txt.visibility = View.VISIBLE
        setupProgressTimer()

    }

    private fun setupProgressTimer() {
//        val styledText = "We will send you six digit <br> OTP on <font color='red'>$countryCode $phoneNumber</font>."
//        resend_otp_btn.text = Html.fromHtml(styledText)

        val res = resources
        val drawable = res.getDrawable(R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 15



        Thread(Runnable {
            while (pStatus >= 0 ) {
                pStatus -= 1
                pStatusVisible -= 1

                handler.post {
                    progressBar.progress = pStatus
                    two_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {
                            runOnUiThread {
                                setpThree.visibility=View.VISIBLE
                                setpThree.visibility=View.GONE
                            }
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
