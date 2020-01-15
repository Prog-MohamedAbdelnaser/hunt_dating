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
import com.bumptech.glide.Glide
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.model.AnswerRandomQuestions
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import de.hdodenhof.circleimageview.CircleImageView
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

    private lateinit var ivLikedPersonImage: CircleImageView
    private lateinit var ivUserImage: CircleImageView

    private var mSwipeUserModel: SwipeUserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_questionnaire)


        ivLikedPersonImage = find(R.id.ivLikedPersonImage)
        ivUserImage = find(R.id.ivUserImage)

        progressBar = find(R.id.otp_progressBar)
        pbMatchTime = find(R.id.pbTimer)
        cl_progressbar = find(R.id.cl_progress_bar_two)


        check_code_btn.setOnClickListener {


            getQuestions()

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
            val mIntent = Intent(this, LetsMeetActivity::class.java)
            mIntent.putExtra("swipeUsers", mSwipeUserModel)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)

        }

        optionButton.setOnClickListener {
            gotoHomeScreen()
        }

        btn_no.setOnClickListener {
            gotoHomeScreen()
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

        displaySwipeUser()
    }

    private fun gotoHomeScreen() {
        if (!isFinishing) {
            val mIntent = Intent(this, HomeActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mIntent)
        }
    }

    private fun getQuestions() {
        val call =
            ApiClient.getClient.getRandomQuestion(SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<RandomQuestionResponse> {
            override fun onFailure(call: Call<RandomQuestionResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<RandomQuestionResponse>,
                response: Response<RandomQuestionResponse>
            ) {


//                setpTwo.visibility = View.VISIBLE
//                setpOne.visibility = View.GONE
                setpTwo.visibility = View.VISIBLE
                setpOne.visibility = View.GONE
                setProgressStart()

                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                        return
                    }
                }
                val randomQuestions = response.body()
                if (randomQuestions != null) {
                    val question = randomQuestions.data.question
                    ranQuestion.text = question
                    tvQuestiom.text = question
                    tvDoYouWantToMeet.text = question
                    tvDoYouWantTo.text = question
                    btnOption1.text = randomQuestions.data.answer[0]
                    btnOption2.text = randomQuestions.data.answer[1]
                    btnOption3.text = randomQuestions.data.answer[2]
                }
            }

        })
    }


    private fun answerQuestion(answers: String) {
        val call =
            ApiClient.getClient.answerRandomQuestion(
                SharedPrefrenceManager.getUserToken(this),
                AnswerRandomQuestions(
                    question = tvQuestiom.text.toString().trim(),
                    answer = answers
                )
            )

        call.enqueue(object : Callback<AnswerRandomQuestions> {
            override fun onFailure(call: Call<AnswerRandomQuestions>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<AnswerRandomQuestions>,
                response: Response<AnswerRandomQuestions>
            ) {
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                        return
                    }
                }
            }

        })
    }


//    override fun onPause() {
//        super.onPause()
//        makeUserOfline()
//    }
//    fun makeUserOfline()
//    {
//        val makeUserOnline= MakeUserOnline(false)
//
//        val call = ApiClient.getClient.makeUserOnline(makeUserOnline, SharedPrefrenceManager.getUserToken(this))
//
//        call.enqueue(object : Callback<MakeUserOnlineResponse> {
//            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {
//
//            }
//
//            override fun onResponse(
//                call: Call<MakeUserOnlineResponse>,
//                response: Response<MakeUserOnlineResponse>
//            ) {
//                if (!response.isSuccessful && !isFinishing) {
//                    val strErrorJson = response.errorBody()?.string()
//                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
//                        return
//                    }
//                }
//            }
//
//        })
//
//    }


    fun goTonext(view: View) {
        setpThree.visibility = View.VISIBLE
        setpTwo.visibility = View.GONE
        pStatus = -1

        when (view) {
            btnOption1 -> {
                button4.text = btnOption1.text.toString().trim()

            }
            btnOption2 -> {
                button4.text = btnOption2.text.toString().trim()
            }
            btnOption3 -> {
                button4.text = btnOption3.text.toString().trim()
            }
        }
        answerQuestion(button4.text.toString().trim())
    }

    fun setProgressStart() {
        progressBar.visibility = View.VISIBLE
        two_progrss_txt.visibility = View.VISIBLE
        setupProgressTimer()

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

                if (pStatus < 0) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MatchQuestionnaireActivity,
                            "Your time is completed!",
                            Toast.LENGTH_LONG
                        ).show()
                        gotoHomeScreen()
                    }
                    return@Runnable
                }

                handler.post {
                    progressBar.progress = pStatus
                    two_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {

                            setpThree.visibility = View.VISIBLE
                            setpThree.visibility = View.GONE

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


    private fun displaySwipeUser() {
        if (intent != null && intent.extras != null) {
            mSwipeUserModel = intent.getParcelableExtra("swipeUsers")
            if (mSwipeUserModel != null) {
                Glide.with(this)
                    .load(mSwipeUserModel?.images!![0])
                    .centerCrop()
                    .into(ivLikedPersonImage)

                val userProfile = SharedPrefrenceManager.getProfileImg(this)

                if (userProfile.contains("http")) {
                    Glide.with(this)
                        .load(userProfile)
                        .centerCrop()
                        .into(ivUserImage)
                }

                tvWantToMeetWithQ.text = "Do you want to meet with ${mSwipeUserModel?.firstName}?"
                tvMessage.text = "You and ${mSwipeUserModel?.firstName} like each other!"
                welcomeText.text =
                    "Before you start the HUNT , answer some of ${mSwipeUserModel?.firstName}'s questions."
            }
        }
    }


}
