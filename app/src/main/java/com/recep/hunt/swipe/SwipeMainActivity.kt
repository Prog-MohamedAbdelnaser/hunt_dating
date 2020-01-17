package com.recep.hunt.swipe

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeListener
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants.Companion.CLICK_ACTION_THRESHOLD
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.matchs.MatchQuestionnaireActivity
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.UserSwipe
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.swipeUser.SwipeUserResponse
import com.recep.hunt.model.usersList.BasicInfo
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.userDetail.UserDetalBottomSheetFragment
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.StoriesProgressView
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.swipe_screen_item.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipeMainActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var currentView: View
    private lateinit var containerView: View
    private lateinit var parentView: RelativeLayout
    private lateinit var context: Context
    private lateinit var matchProgressBar: ProgressBar
    private lateinit var textView50: TextView
    private lateinit var textView51: TextView
    private lateinit var attendanceProgressBar: ProgressBar
    private lateinit var homeView: ConstraintLayout
    private lateinit var ivForDate: ImageView
    private lateinit var ivForBusiness: ImageView
    private lateinit var ivForFriendship: ImageView
    private lateinit var ivStatusOnline: ImageView
    private var storyProgressViews = ArrayList<StoriesProgressView>()
    private var storyImageView = ArrayList<ImageView>()
    private lateinit var swipe: Swipe
    private var items = ArrayList<SwipeUserModel>()

    var windowwidth: Int = 0
    var screenCenter: Int = 0
    var x_cord: Int = 0
    var y_cord: Int = 0
    var x: Int = 0
    var y: Int = 0
    var Likes: Int = 0
    var counter = ArrayList<Int>()
    var currentUser: Int = 0
    var dummyImages: IntArray = intArrayOf(
        R.drawable.demo_user,
        R.drawable.demo_user_1,
        R.drawable.demo_user_2,
        R.drawable.demo_user_3,
        R.drawable.demo_user_4
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_main)
        init()
    }

    @SuppressLint("NewApi")
    private fun init() {
        context = this
        swipe = Swipe(200, 200)
        parentView = find(R.id.main_layoutView)
        windowwidth = windowManager.defaultDisplay.width
        screenCenter = windowwidth / 2
        // items = dummyUsersdata()
        try {
            items = intent.getParcelableArrayListExtra("swipeUsers")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addNearbyUsersToSwipe()
        setupToolbarClicks()
    }

    //todo implement viewpager
    @SuppressLint("ClickableViewAccessibility")
    private fun addNearbyUsersToSwipe() {

        var last = items.size - 1
        for (i in 0..last) {
            var containerView =
                LayoutInflater.from(context).inflate(R.layout.swipe_screen_item, null)
            //match status progress bar
            matchProgressBar = containerView.findViewById(R.id.match_status_progressBar)
            textView50 = containerView.findViewById(R.id.textView50)
            textView51 = containerView.findViewById(R.id.textView51)
            ivForDate = containerView.findViewById(R.id.ivForDate)
            ivForBusiness = containerView.findViewById(R.id.ivForBusiness)
            ivForFriendship = containerView.findViewById(R.id.ivForFriendship)
            ivStatusOnline = containerView.findViewById(R.id.ivStatusOnline)


            textView51.text = "" + items[i].totalMeeting
            textView50.text = "" + items[i].totalMatching.toInt()

            if (items[i].is_online == "true") {
                ivStatusOnline.visibility = VISIBLE
            }
            if (items[i].for_bussiness != null) {
                if (items[i]?.for_bussiness?.isNotEmpty()!!) {
                    ivForBusiness.visibility = VISIBLE
                }
            }
            if (items[i].for_date.isNotEmpty()) {
                ivForDate.visibility = VISIBLE
            }
            if (items[i].for_friendship.isNotEmpty()) {
                ivForFriendship.visibility = VISIBLE
            }

            textView51.text = "" + items[i].totalMeeting
            textView50.text = "" + items[i].totalMatching.toInt()

            val drawableMatch =
                context.resources.getDrawable(R.drawable.circular_progressbar_inside_bg)
            matchProgressBar.progressDrawable = drawableMatch
            matchProgressBar.progress = items[i].totalMatching.toInt()
            matchProgressBar.max = 100
            //attendance status progress bar
            attendanceProgressBar = containerView.findViewById(R.id.attendance_status_progressBar)
            val drawableAttendance =
                context.resources.getDrawable(R.drawable.circular_progressbar_attendance_inside_bg)
            attendanceProgressBar.progressDrawable = drawableAttendance
            attendanceProgressBar.progress = items[i].totalMeeting
            attendanceProgressBar.max = 100

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            containerView.layoutParams = layoutParams

            containerView.tag = i
            storyProgressViews.add(containerView.findViewById(R.id.stories))
            storyImageView.add(containerView.findViewById(R.id.story_image_userdetail))
            counter.add(0)
            currentUser = i
            storyProgressViews[i].setStoriesCount(items[i].images!!.size)
            storyProgressViews[i].setStoryDuration(10000L)

            if (items[i].images!!.size > 0)
                Glide.with(this)
                    .load(items[i].images!![0])
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(ObjectKey(items[currentUser].images!![counter[currentUser]]))
                    .into(storyImageView[i])
            if (i == last)
                storyProgressViews[i].startStories()
            val nameView = containerView.findViewById<TextView>(R.id.user_detail_username_txtView)
            val placeView = containerView.findViewById<TextView>(R.id.user_detail_place_txtView)
            val titleView = containerView.findViewById<TextView>(R.id.user_detail_job_title)
            val detailView = containerView.findViewById<TextView>(R.id.user_detail_about_you)
            nameView.text = items[i].firstName + ", " + items[i].age.toString()
            placeView.text = items[i].locationName
            titleView.text = items[i].title
            detailView.text = items[i].detail

            homeView = containerView.findViewById(R.id.return_home_layout)
            homeView.setOnClickListener {
                finish()
            }

            //Show next stories
            storyProgressViews[i].setStoriesListener(this)

            val relativeLayoutContainer =
                containerView.findViewById<ConstraintLayout>(R.id.relative_container)
            var isCalled = false;
            //Touch listener on the layout to swipe image right or left
            relativeLayoutContainer.setOnTouchListener { v, event ->
                x_cord = event.rawX.toInt()
                y_cord = event.rawY.toInt()
                isCalled = false
                containerView.x = 0f
                containerView.y = 0f
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.x.toInt()
                        y = event.y.toInt()
                        storyProgressViews[currentUser].pause()
                        Log.v("On touch", "$x $y")
                    }
                    MotionEvent.ACTION_MOVE -> {

                        x_cord = event.rawX.toInt()
                        y_cord = event.rawY.toInt()

                        if (isAClick(event.eventTime, event.downTime)) {
                            containerView.x = 0f
                            containerView.y = 0f
                        } else {
                            containerView.x = (x_cord - x).toFloat()
                            containerView.rotation = ((x_cord - x) * (Math.PI / 256)).toFloat()

                            if ((x - x_cord) <= -150 || (x - x_cord) >= 150) {
                                when {
                                    x_cord > x -> {
                                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView)
                                            .alpha = 1.0f
                                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView)
                                            .imageResource = R.drawable.swipe_like
                                        if (x_cord - x >= 255 || (x_cord - x) % 255 >= 179)
                                            containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(
                                                Color.argb(179, 58, 204, 225)
                                            )
                                        else if ((x_cord - x) % 255 < 179)
                                            containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(
                                                Color.argb((x_cord - x) % 255, 58, 204, 225)
                                            )
                                        Likes = if (x_cord >= (screenCenter + 50)) {
                                            2
                                        } else {
                                            0
                                        }
                                    }
                                    x_cord < x -> {
                                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView)
                                            .alpha = 1.0f
                                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView)
                                            .imageResource = R.drawable.swipe_dislike


                                        if (x - x_cord >= 255 || (x - x_cord) % 255 >= 153)
                                            containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(
                                                Color.argb(153, 255, 42, 78)
                                            )
                                        else if ((x - x_cord) % 255 < 153)
                                            containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(
                                                Color.argb((x - x_cord) % 255, 255, 42, 78)
                                            )

                                        Likes = if (x_cord <= screenCenter - 50) {
                                            1
                                        } else {
                                            0
                                        }
                                    }
                                    else -> {
                                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView)
                                            .alpha = 0.0f
                                        containerView.findViewById<ImageView>(R.id.story_image_userdetail)
                                            .colorFilter = null
                                        Likes = 0
                                    }
                                }
                            } else if ((y - y_cord) >= 50) {
                                // setupUserDetailBottomSheet(items[i])
                            }

                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView).alpha =
                            0.0f
                        containerView.findViewById<ImageView>(R.id.story_image_userdetail)
                            .colorFilter = null

                        if (isAClick(event.eventTime, event.downTime)) {
                            Log.e("Event_Status :->", "Only Clicked")
                            if (x >= screenCenter) {
                                storyProgressViews[currentUser].skip()
                                //  onNext()
                            } else {
                                storyProgressViews[currentUser].reverse()

                                //  onPrev()
                            }
                            currentView.parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            storyProgressViews[currentUser].resume()
                            x_cord = event.rawX.toInt()
                            y_cord = event.rawY.toInt()

                            if (((x - x_cord) <= -150 || (x - x_cord) >= 150)) {
                                if (Likes == 0) {
                                    containerView.animate().x(0f).y(0f).rotation(0f).duration = 300
                                    //                                storyProgressViews[currentUser].resume()
                                } else if (Likes == 1) {
                                    Log.e("Event_Status :->", "Dislike")
                                    parentView.removeView(containerView)
                                     callSwipeUserApi(items[currentUser], Likes)
                                    if (currentUser > 0) {
                                        storyProgressViews[currentUser].destroy()
                                        currentUser--
                                        storyProgressViews[currentUser].startStories()
                                        Likes = 0
                                        parentView.getChildAt(currentUser).animate().scaleX(1f)
                                            .scaleY(1f)

                                    } else {
                                        gotoMainScreen()
                                        //                                    context.launchActivity<HomeActivity> {}
                                        //                                    finish()
                                    }

                                } else if (Likes == 2) {
                                    Log.e("Event_Status :->", "Like")
                                    parentView.removeView(containerView)
                                    callSwipeUserApi(items[currentUser], Likes)
                                    if (currentUser > 0) {
                                        storyProgressViews[currentUser].destroy()
                                        currentUser--
                                        storyProgressViews[currentUser].startStories()
                                        Likes = 0
                                        parentView.getChildAt(currentUser).animate().scaleX(1f)
                                            .scaleY(1f)
                                    } else {
                                        gotoMainScreen()
                                    }
                                }
                            }
                        }

                    }
                }
                currentView = v
                // swipe.dispatchTouchEvent(event)
                true
            }
            parentView.addView(containerView)
        }

        swipe.setListener(object : SwipeListener {
            override fun onSwipedUp(event: MotionEvent?): Boolean {
                // setupUserDetailBottomSheet(items[i])
                return false
            }

            override fun onSwipedDown(event: MotionEvent?): Boolean {
                return false
            }

            override fun onSwipingUp(event: MotionEvent?) {
                setupUserDetailBottomSheet(items[currentUser])
            }

            override fun onSwipedRight(event: MotionEvent?): Boolean {
                event?.let { checkSwipState(it) }
                return false
            }

            override fun onSwipingLeft(event: MotionEvent?) {


            }

            override fun onSwipingRight(event: MotionEvent?) {


            }

            override fun onSwipingDown(event: MotionEvent?) {
            }

            override fun onSwipedLeft(event: MotionEvent?): Boolean {
                //  event?.let { checkSwipState(it) }
                return false
            }

        })

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    //Control Action Up
    private fun checkSwipState(event: MotionEvent) {

    }

    //Control Acton Move
    private fun computeSwipe(event: MotionEvent) {

    }

    private fun gotoMainScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun gotoMatchQuestionScreen(mSwipeUserModel: SwipeUserModel) {
        val intent = Intent(this, MatchQuestionnaireActivity::class.java)
        intent.putExtra("swipeUsers", mSwipeUserModel)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun callSwipeUserApi(mSwipeUserModel: SwipeUserModel, like: Int) {
        var likes: String = ""
        if (like == 1) //Dislike this user
        {
            likes = "dislike"
        }
        if (like == 2) //Like this user
        {
            likes = "like"
        }

        val swipe = UserSwipe(mSwipeUserModel.id, likes)
        val call = ApiClient.getClient.swipeUser(swipe, SharedPrefrenceManager.getUserToken(this))
        call.enqueue(object : Callback<SwipeUserResponse> {
            override fun onFailure(call: Call<SwipeUserResponse>, t: Throwable) {
                Log.d("Api call failure -> ", "" + call)
            }

            override fun onResponse(
                call: Call<SwipeUserResponse>,
                response: Response<SwipeUserResponse>
            ) {


                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@SwipeMainActivity, strErrorJson)) {
                        return
                    }
                }

                val status = response.body()?.status

                if (status == 2) {
                    Toast.makeText(
                        this@SwipeMainActivity,
                        "Congratulation, You matched",
                        Toast.LENGTH_LONG
                    ).show()
                    gotoMatchQuestionScreen(mSwipeUserModel)
                }

            }

        })
    }

    //Dummy Users Data
    private fun dummyUsersdata(): ArrayList<SwipeUserModel> {
        val data = ArrayList<SwipeUserModel>()
        val images: ArrayList<String> = ArrayList<String>()
        images.add("https://hunt.nyc3.digitaloceanspaces.com/User/1573485502.jfif")
        images.add("https://hunt.nyc3.digitaloceanspaces.com/User/1573546803.jpg")
        val images1 = ArrayList<String>()
        images1.add("https://hunt.nyc3.digitaloceanspaces.com/User/1573574714.jpg")
        images1.add("https://hunt.nyc3.digitaloceanspaces.com/User/1573574732.jfif")
        if (data.size == 0) {
            data.add(
                SwipeUserModel(
                    2,
                    "Hookah Loungh",
                    "Valentina",
                    28,
                    "title ", "test detial", 80f, 60, "true", "both", "both", "both",
                    images,
                    BasicInfo(
                        "test",
                        "test",
                        "test job",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
            data.add(
                SwipeUserModel(
                    2,
                    "Hookah Loungh",
                    "Valentina",
                    28,
                    "title ", "test detial", 80f, 60, "true", "both", "both", "both",
                    images1,
                    BasicInfo(
                        "test",
                        "test",
                        "test job",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
            data.add(
                SwipeUserModel(
                    2,
                    "Hookah Loungh",
                    "Valentina",
                    28,
                    "title ", "test detial", 80f, 60, "true", "both", "both", "both",
                    images,
                    BasicInfo(
                        "test",
                        "test",
                        "test job",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
            data.add(
                SwipeUserModel(
                    2,
                    "Hookah Loungh",
                    "Valentina",
                    28,
                    "title ", "test detial", 80f, 60, "true", "both", "both", "both",
                    images1,
                    BasicInfo(
                        "test",
                        "test",
                        "test job",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
            data.add(
                SwipeUserModel(
                    2,
                    "Hookah Loungh",
                    "Valentina",
                    28,
                    "title ", "test detial", 80f, 60, "true", "both", "both", "both",
                    images,
                    BasicInfo(
                        "test",
                        "test",
                        "test job",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
        }
        return data
    }

    fun isAClick(dragTime: Long, downTime: Long): Boolean {
        return dragTime - downTime < CLICK_ACTION_THRESHOLD
    }

    var bottomSheet: UserDetalBottomSheetFragment? = null;
    private fun setupUserDetailBottomSheet(swipeUserModel: SwipeUserModel) {
        Log.e("TAG", " called ")

        if (bottomSheet != null) {
            try {
                if (!bottomSheet?.dialog?.isShowing!!) {
                    supportFragmentManager.beginTransaction().remove(bottomSheet!!).commit();
                }
            } catch (e: Exception) {
                try {
                    supportFragmentManager.beginTransaction().remove(bottomSheet!!).commit();
                } catch (e: Exception) {
                }
            } catch (e: Exception) {
            }
            try {
                if (!bottomSheet?.dialog?.isShowing!!) {
                    bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
                    bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")
                }
            } catch (e: Exception) {
                bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
                bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")
            }
        } else {
            bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
            bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")

        }

    }

    override fun onComplete() {
        counter[currentUser] = items[currentUser].images!!.size

    }

    override fun onPrev() {
        Log.e("TAG", "OnPrev")
        if (counter[currentUser] > 0) {
            counter[currentUser]--
            Glide.with(this)
                .load(items[currentUser].images!![counter[currentUser]])
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(items[currentUser].images!![counter[currentUser]]))
                .into(storyImageView[currentUser])
        }
    }

    override fun onNext() {
        Log.e("TAG", "OnNext")
        if (counter[currentUser] < items[currentUser].images!!.size - 1) {
            counter[currentUser]++
            Glide.with(this)
                .load(items[currentUser].images!![counter[currentUser]])
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(items[currentUser].images!![counter[currentUser]]))
                .into(storyImageView[currentUser])
        }
    }

    private fun setupToolbarClicks() {
        user_detail_filter_btn.setOnClickListener {
            val bottomSheet = FilterBottomSheetDialog(this)
            bottomSheet.show(supportFragmentManager, "FilterBottomSheetDialog")
        }

        user_detail_incoginoti_btn.setOnClickListener {
            showIncognitoBtn()
        }

        user_detail_notification_btn.setOnClickListener {
            launchActivity<NotificationsActivity>()
        }

        user_detail_profile_btn.setOnClickListener {
            launchActivity<UserProfileActivity>()
        }
    }

    private fun showIncognitoBtn() {
        val isIncognito = SharedPrefrenceManager.getisIncognito(this)
        if (isIncognito) {
            SharedPrefrenceManager.setisIncognito(this, false)
            val ll = LayoutInflater.from(this).inflate(R.layout.incoginito_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val gotItBtn: Button = dialog.find(R.id.got_it_btn)
            gotItBtn.setOnClickListener {
                home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost_on)
                makeUserOnOffline(false)
                dialog.dismiss()
            }
            dialog.show()
        } else {
            home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)
            makeUserOnOffline(true)
            SharedPrefrenceManager.setisIncognito(this, true)
        }

    }

    private fun makeUserOnOffline(is_online: Boolean) {
        val makeUserOnline = MakeUserOnline(is_online)

        val call = ApiClient.getClient.makeUserOnline(
            makeUserOnline,
            SharedPrefrenceManager.getUserToken(this)
        )

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
                if (!response.isSuccessful && !isFinishing) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@SwipeMainActivity, strErrorJson)) {
                        return
                    }
                }

                if (is_online == false)
                    Toast.makeText(
                        this@SwipeMainActivity,
                        "You're offline",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(
                        this@SwipeMainActivity,
                        "You're online",
                        Toast.LENGTH_SHORT
                    ).show()
            }

        })

    }
}
