package com.recep.hunt.matchs

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.base.activity.BaseActivity
import com.recep.hunt.base.adapter.BaseAdapter
import com.recep.hunt.base.adapter.BaseViewHolder
import com.recep.hunt.base.extentions.handleApiErrorWithSnackBar
import com.recep.hunt.domain.entities.BeginHuntLocationParams
import com.recep.hunt.domain.entities.UpdateHuntBeginParams
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.matchs.vm.MatchQuestionsViewModel
import com.recep.hunt.model.AnswerRandomQuestions
import com.recep.hunt.model.randomQuestion.QuestionData
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import kotlinx.android.synthetic.main.question_item.view.*
import kotlinx.android.synthetic.main.question_layout.*
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MatchQuestionnaireActivity : BaseActivity() {

    private lateinit var currentQuestion: QuestionData
    private var questionNumber =0

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

    private var addTime = 0L

    private lateinit var ivLikedPersonImage: CircleImageView

    private lateinit var ivUserImage: CircleImageView

    private var mSwipeUserModel: SwipeUserModel? = null

    private val matchQuestionViewModel:MatchQuestionsViewModel by viewModel()

    private val questionAdapter by lazy { QuestionsAdapter() }

    private var placeName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_questionnaire)

        ivLikedPersonImage = find(R.id.ivLikedPersonImage)

        ivUserImage = find(R.id.ivUserImage)

        progressBar = find(R.id.progressBarCountDown)

        pbMatchTime = find(R.id.pbTimer)

        cl_progressbar = find(R.id.cl_progress_bar_two)

        btnLetsGo.setOnClickListener {
            matchQuestionViewModel.getQuestions(this)
        }

        idSubmit.setOnClickListener {
            tvStepSix.visibility = View.GONE
            updateHuntBegin(UpdateHuntBeginParams(0,"yes",if(addTime > 0L) "yes" else "no","$addTime"))

        }

        btnCancelStepOne.setOnClickListener {
            //            gotoHomeScreen()
            onBackPressed()
        }

        btnAddTime.setOnClickListener {
            when (addTime) {
                1L -> {
                    setTimerAgain(addTime)
                    addTime = 5
                    btnAddTime.text = "+ 5 Min"
                    btnAddTime.setBackgroundResource(R.drawable.magento_corner_card)
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

        placeName=getLocationNameFromArgs()

        initUI()

        displaySwipeUser()

        initRecyclerQuestions()

        initModelObservers()

    }

    @Throws(IllegalArgumentException::class)
    fun getLocationNameFromArgs():String?{
        if (intent.extras.isEmpty.not()&&intent.extras.containsKey(FeaturesConstants.LOCATION_OBJECT_KEY)){
            return intent.extras.getString(FeaturesConstants.LOCATION_OBJECT_KEY)
        } else throw IllegalArgumentException("${this.javaClass.simpleName} arguments must contains Location name Params")


    }

    private fun initUI() {
        btnYesWantMeet.setOnClickListener {
            askAboutMeetingLocationLayout.visibility=View.VISIBLE
            askAboutWantToMeetLayout.visibility=View.GONE


        }

        btnAddLocation.setOnClickListener {
            if (edtEnterLocation.text.toString().length>3) {
                addLocation(edtEnterLocation.text.toString())
                edtEnterLocation.setText("")
            }else Helpers.showErrorSnackBar(this,getString(R.string.notice),getString(R.string.invalid_length))
        }

        btnCancelMeet.setOnClickListener {
            //            gotoHomeScreen()
            onBackPressed()
        }

        btnLocation1.setOnClickListener {
            sendHuntLocation(btnLocation1.text.toString())
        }
        btnLocation2.setOnClickListener {
            sendHuntLocation(btnLocation2.text.toString())

        }
        btnLocation3.setOnClickListener {
            sendHuntLocation(btnLocation3.text.toString())
        }

        btnCancelLocation.setOnClickListener {
            //            gotoHomeScreen()
            onBackPressed()
        }

    }

    fun sendHuntLocation(locationName:String){
        matchQuestionViewModel.sendHuntLocation(createHuntLocation(locationName))
    }

    private fun createHuntLocation(locationName: String): BeginHuntLocationParams {
        return BeginHuntLocationParams(mSwipeUserModel?.id!!,placeName!!,locationName)
    }

    fun gotToSixStep(){
        setTimer(6 * 60 * 1000)
        tvTitle.text = "Time to Hunt!"
        stepSix.visibility=View.VISIBLE
        askAboutMeetingLocationLayout.visibility=View.GONE
    }
    private fun addLocation(customeLocation: String) {
        if (btnLocation1.isVisible.not()) {
            btnLocation1.visibility = View.VISIBLE
            btnLocation1.text=customeLocation

        }else {

            if (btnLocation2.isVisible.not()) {
                btnLocation2.visibility = View.VISIBLE
                btnLocation2.text = customeLocation

            }else{
                if (btnLocation3.isVisible.not()) {
                    btnLocation3.visibility = View.VISIBLE
                    btnLocation3.text = customeLocation
                    edtEnterLocation.isEnabled = false
                    btnAddLocation.isEnabled = false
                }
            }
        }

    }

    private fun initModelObservers() {
        matchQuestionViewModel.apply {
            getQuestionLiveData.observe(this@MatchQuestionnaireActivity, Observer {
                Log.i("matchQuestionViewModel","getQuestionLiveData : ${it.toString()}")
                handleGetQuestionState(it)
            })

            sendHuntLocationLiveData.observe(this@MatchQuestionnaireActivity, Observer {
                handleSendHuntLocationState(it)
            })

            updateHuntBeginLiveData.observe(this@MatchQuestionnaireActivity, Observer {
                handleUpdateHuntState(it)

            })
        }
    }

    private fun handleUpdateHuntState(state: CommonState<Any>?) {
        when(state){
            CommonState.LoadingShow->showProgressDialog()
            CommonState.LoadingFinished->hideProgressDialog()
            is CommonState.Success->{

                val mIntent = Intent(this, LetsMeetActivity::class.java)
                mIntent.putExtra("swipeUsers", mSwipeUserModel)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(mIntent)
                finish()

                Helpers.showSuccesSnackBar(this,"Successfully",state.data.toString())
            }
            is CommonState.Error->{
                handleApiErrorWithSnackBar(state.exception)
            }
        }
    }

    private fun handleSendHuntLocationState(state: CommonState<Any>?) {

        when (state) {
            CommonState.LoadingShow->showProgressDialog()
            CommonState.LoadingFinished->hideProgressDialog()
            is CommonState.Success -> {
                Log.i("matchQuestionViewModel","getQuestionLiveData : ${state.data.toString()}")
                gotToSixStep()
            }
            is CommonState.Error -> {
                handleApiErrorWithSnackBar(state.exception)
                state.exception.printStackTrace()
                Log.i("matchQuestionViewModel","getQuestionLiveData : ${state.exception.toString()}")

            }
        }

    }

    private fun initRecyclerQuestions() {
        recyclerViewQuestions.apply {
            this.setHasFixedSize(true)
            adapter=questionAdapter
            layoutManager = object : LinearLayoutManager(context) {
                override fun isAutoMeasureEnabled(): Boolean {
                    return false
                }
            }
        }

    }


    private fun handleGetQuestionState(state: CommonState<Response<RandomQuestionResponse>>?) {
        when(state){
            is CommonState.Success->{

                setpOne.visibility = View.GONE
                setProgressStart()

                if (!state.data.isSuccessful) {
                    val strErrorJson = state.data.errorBody()?.string()
                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                        return
                    }
                }

                questionlayout.visibility=View.VISIBLE

                val randomQuestions = state.data.body()
                if (randomQuestions != null) {
                    setQuestion(state.data.body()!!.data)
                }
            }
            is CommonState.Error->{

            }
        }
    }

    private fun gotoHomeScreen() {
//        if (!isFinishing) {
//            val mIntent = Intent(this, HomeActivity::class.java)
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(mIntent)
//        }
    }

//    private fun getQuestions() {
//        val call =
//            ApiClient.getClient.getRandomQuestion(SharedPrefrenceManager.getUserToken(this))
//
//        call.enqueue(object : Callback<RandomQuestionResponse> {
//            override fun onFailure(call: Call<RandomQuestionResponse>, t: Throwable) {
//
//            }
//
//            override fun onResponse(
//                call: Call<RandomQuestionResponse>,
//                response: Response<RandomQuestionResponse>) {
//
//
////                setpTwo.visibility = View.VISIBLE
////                setpOne.visibility = View.GONE
//                setpOne.visibility = View.GONE
//                setProgressStart()
//
//                if (!response.isSuccessful) {
//                    val strErrorJson = response.errorBody()?.string()
//                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
//                        return
//                    }
//                }
//                val randomQuestions = response.body()
//                if (randomQuestions != null) {
//
//                }
//            }
//
//        })
//    }


    fun showLastQuestion(question:String , answers: String){
        askAboutWantToMeetLayout.visibility=View.VISIBLE
        btnLastAnswer.text=answers
        tvLastQuestion.text=question
        questionlayout.visibility=View.GONE

    }

    private fun answerQuestion(answers: String) {
        val call =
            ApiClient.getClient.answerRandomQuestion(

                SharedPrefrenceManager.getUserToken(this),
                AnswerRandomQuestions(question = tvQuestionTitle.text.toString().trim(), answer = answers)

            )

        call.enqueue(object : Callback<AnswerRandomQuestions> {
            override fun onFailure(call: Call<AnswerRandomQuestions>, t: Throwable) {

            }

            override fun onResponse(call: Call<AnswerRandomQuestions>, response: Response<AnswerRandomQuestions>) {

                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@MatchQuestionnaireActivity, strErrorJson)) {
                        return
                    }
                }

            }

        })
    }




    fun goTonext(answers: String) {
        pStatus = -1
        answerQuestion(answers)
        if (questionNumber==1){
            showLastQuestion(currentQuestion.question,answers)
        }
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
//                        gotoHomeScreen()
                        onBackPressed()
                    }
                    return@Runnable
                }

                handler.post {
                    progressBar.progress = pStatus
                    two_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {
                            questionlayout.visibility=View.VISIBLE
//                            setpThree.visibility = View.VISIBLE
//                            setpThree.visibility = View.GONE

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

                tvWantToMeetWithUser.text = "Do you want to meet with ${mSwipeUserModel?.firstName}?"
                tvMessage.text = "You and ${mSwipeUserModel?.firstName} like each other!"
                welcomeText.text =
                    "Before you start the HUNT , answer some of ${mSwipeUserModel?.firstName}'s questions."
            }
        }
    }

    fun setQuestion(questionData: QuestionData){
        questionNumber = +1
        currentQuestion=questionData
        tvQuestionTitle.text =  questionData.question
        questionAdapter.updateItems(questionData.answer)
    }

    fun updateHuntBegin(updateHuntBeginParams: UpdateHuntBeginParams){
        matchQuestionViewModel.updateHuntBegin(updateHuntBeginParams)
    }

    inner class QuestionsAdapter(): BaseAdapter<String>(itemLayoutRes = R.layout.question_item) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return QuestionsViewHolder(getItemView(parent))
        }

        inner class QuestionsViewHolder(view: View): BaseViewHolder<String>(view) {
            override fun fillData() {
                Log.i("QuestionsAdapter","fillData ${item}")
                itemView. btnAnswerOption.text=item

                itemView.btnAnswerOption.setOnClickListener {
                    Log.i("btnAnswerOption","click ${item}")
                    goTonext(item!!) }
            }

        }
    }
}
