package com.recep.hunt.matchs

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import org.jetbrains.anko.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MatchQuestionnaireActivity : AppCompatActivity() {

    private var pStatus = 15
    private var pStatusVisible = 15
    private lateinit var cl_progressbar: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var pbMatchTime: ProgressBar
    private val handler = Handler()
    private var currentTime = 0L

    private lateinit var cl_timer_progrss: ConstraintLayout
    private lateinit var progressBarTimer: ProgressBar
    private val timerHandler = Handler()

    lateinit var countDownTimer: CountDownTimer
    private var timerPstatus = 360

    private var addTime = 1L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_questionnaire)
        progressBar = find(R.id.otp_progressBar)
        pbMatchTime = find(R.id.pbTimer)
        cl_progressbar = find(R.id.cl_progress_bar_two)


        check_code_btn.setOnClickListener {
            setpTwo.visibility = View.VISIBLE
            setpOne.visibility = View.GONE

            val call =
                ApiClient.getClient.getRandomQuestion(SharedPrefrenceManager.getUserToken(this))

            call.enqueue(object : Callback<RandomQuestionResponse> {
                override fun onFailure(call: Call<RandomQuestionResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<RandomQuestionResponse>,
                    response: Response<RandomQuestionResponse>
                ) {
                    if (!response.isSuccessful) {
                        val strErrorJson = response.errorBody()?.string()
                        if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                            return
                        }
                    }


                    setpTwo.visibility = View.VISIBLE
                    setpOne.visibility = View.GONE
                    setProgressStart()


                }

            })

        }

        btn_yes.setOnClickListener {
            setpThree.visibility = View.GONE
            stepFour.visibility = View.VISIBLE
            tvTitle.text = "Let the Hunt begin!"

        }
        btn_goto_next_five.setOnClickListener {
            stepFour.visibility = View.GONE
            stepFive.visibility = View.VISIBLE

        }

        btn.setOnClickListener {
            stepFive.visibility = View.GONE
            stepSix.visibility = View.VISIBLE
            setTimer(6 * 60 * 1000)
            tvTitle.text = "Time to Hunt!"

        }

        idSubmit.setOnClickListener {
            tvStepSix.visibility = View.GONE
            var mIntent = Intent(this, LetsMeetActivity::class.java)
            startActivity(mIntent)

        }
        id_add_time.setOnClickListener {
            when (addTime) {
                1L -> {
                    setTimerAgain(addTime)
                    addTime = 5
                    id_add_time.text = "+ 5 Min"
                    id_add_time.setBackgroundResource(R.drawable.magento_corner_card)
                }
                5L -> {
                    setTimerAgain(addTime)
                    addTime = 0
                }
                else -> Toast.makeText(
                    this,
                    "Now you could not add more time.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }


    override fun onPause() {
        super.onPause()
        makeUserOfline()
    }
    fun makeUserOfline()
    {
        val makeUserOnline= MakeUserOnline(false)

        val call = ApiClient.getClient.makeUserOnline(makeUserOnline, SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
                if (!response.isSuccessful && !isFinishing) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                        return
                    }
                }
            }

        })

    }



    fun goTonext(view: View) {
        setpThree.visibility = View.VISIBLE
        setpTwo.visibility = View.GONE
    }

    fun setProgressStart() {
        progressBar.visibility = View.VISIBLE
        two_progrss_txt.visibility = View.VISIBLE
        setupProgressTimer()

    }

    private fun setProgressMatch() {


    }

    private fun setupProgressTimer() {
//        val styledText = "We will send you six digit <br> OTP on <font color='red'>$countryCode $phoneNumber</font>."
//        resend_otp_btn.text = Html.fromHtml(styledText)

        val res = resources
        val drawable = res.getDrawable(com.recep.hunt.R.drawable.circular_progress_bg)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
//        progressBar.max = 15



        Thread(Runnable {
            while (pStatus >= 0) {
                pStatus -= 1
                pStatusVisible -= 1

                handler.post {
                    progressBar.progress = pStatus
                    two_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {
                            runOnUiThread {
                                setpThree.visibility = View.VISIBLE
                                setpThree.visibility = View.GONE
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


    private fun setupTimerProgress() {

    }


    private fun startTimer(noOfMinutes: Long) {

        countDownTimer = (object : CountDownTimer(noOfMinutes, 1000) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                //Convert milliseconds into hour,minute and seconds
                currentTime = millisUntilFinished
                pbMatchTime.progress = currentTime.toInt()
                val hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(
                        millisUntilFinished
                    ),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millisUntilFinished
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisUntilFinished
                        )
                    )
                )
                tvRemaningTime.text = hms//set text
            }

        }).start()


    }

    private fun setTimer(min: Long) {
        pbMatchTime.visibility = View.VISIBLE
        pStatus = 15
        val drawable = ContextCompat.getDrawable(this, R.drawable.circular_progress_bg)
        pbMatchTime.progressDrawable = drawable
        pbMatchTime.progress = 0
        pbMatchTime.max = min.toInt()
        startTimer(min)
    }

    fun setTimerAgain(min: Long) {
        countDownTimer.cancel()
        var nowTime = currentTime
        nowTime = min * 60 * 1000 + currentTime
        setTimer(nowTime)
    }


}
