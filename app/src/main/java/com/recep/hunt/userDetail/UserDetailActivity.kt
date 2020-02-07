package com.recep.hunt.userDetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import antonkozyriatskyi.circularprogressindicator.PatternProgressTextAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.recep.hunt.utilis.*
import org.jetbrains.anko.imageResource


class UserDetailActivity : AppCompatActivity(), StoriesProgressView.StoriesListener,
    SimpleGestureFilter.SimpleGestureListener {
    override fun onSwipe(direction: Int) {
        when (direction) {
            SimpleGestureFilter.SWIPE_UP -> {

                setupUserDetailBottomSheet()
            }
        }
    }

    override fun onDoubleTap() {
    }

    private lateinit var behavior: BottomSheetBehavior<View>

    var counter = 0
    var screenCenter: Int = 0
    var x_cord: Int = 0
    var y_cord: Int = 0
    var x: Int = 0
    var y: Int = 0
    private lateinit var swipe: Swipe
    private lateinit var attendeeProgress: CircularProgressIndicator
    private lateinit var matchRateProgress: CircularProgressIndicator

    private lateinit var storyProgressView: StoriesProgressView
    private lateinit var storyImageView: ImageView
    private var userImagesStoriesData = ArrayList<String>()
    private var isIncognito: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        isIncognito = SharedPrefrenceManager.getisIncognito(applicationContext)
        init()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        swipe = Swipe(200, 200)


        swipe.setListener(object : SwipeListener {
            override fun onSwipedUp(event: MotionEvent?): Boolean {
                //setupUserDetailBottomSheet()
                Log.e("TAG", "onSwipedUp")
                return false
            }

            override fun onSwipedDown(event: MotionEvent?): Boolean {
                return false

            }

            override fun onSwipingUp(event: MotionEvent?) {
                setupUserDetailBottomSheet()
            }

            override fun onSwipedRight(event: MotionEvent?): Boolean {
                return false

            }

            override fun onSwipingLeft(event: MotionEvent?) {
            }

            override fun onSwipingRight(event: MotionEvent?) {
            }

            override fun onSwipingDown(event: MotionEvent?) {
            }

            override fun onSwipedLeft(event: MotionEvent?): Boolean {
                return false

            }

        })
        attendeeProgress = find(R.id.attendance_circularProgress)
        attendeeProgress.setProgress(75.00, 100.00)
        attendeeProgress.setProgressTextAdapter(CustomProgressTextAdapter())
        attendeeProgress.progressTextAdapter.formatText(75.00)
        matchRateProgress = find(R.id.matchRate_circularProgress)
        matchRateProgress.setProgress(90.00, 100.00)
        matchRateProgress.setProgressTextAdapter(CustomProgressTextAdapter())
        matchRateProgress.progressTextAdapter.formatText(90.00)
        storyProgressView = find(R.id.stories)
        storyImageView = find(R.id.story_image_userdetail)
        setupUserDetailBottomSheet()
        val count = getStoryData().size
        screenCenter = Resources.getSystem().displayMetrics.widthPixels / 2
        userImagesStoriesData = getStoryData()
        if (userImagesStoriesData.size != 0) {
            storyProgressView.setStoriesCount(count)
            storyProgressView.setStoryDuration(10000L)
            Glide.with(this)
                .load(Base64.decode(userImagesStoriesData[counter], Base64.DEFAULT))
                .into(storyImageView)
            storyProgressView.setStoriesListener(this)
            storyProgressView.startStories()
        }
        storyImageView.setOnTouchListener { _, event ->
            x_cord = event.rawX.toInt()
            y_cord = event.rawY.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x.toInt()
                    y = event.y.toInt()
                    storyProgressView.pause()
                }
                MotionEvent.ACTION_MOVE -> {


                }
                MotionEvent.ACTION_UP -> {
                    if (isAClick(event.eventTime, event.downTime)) {
                        Log.e("Event_Status :->", "Only Clicked")
                        if (x >= screenCenter) {
                            storyProgressView.skip()
                            //  onNext()
                        } else {
                            storyProgressView.reverse()

                            //  onPrev()
                        }
                    } else {
                        storyProgressView.resume()

                    }
                }
            }

            true
        }

    }

    fun isAClick(dragTime: Long, downTime: Long): Boolean {
        return dragTime - downTime < Constants.CLICK_ACTION_THRESHOLD
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onNext() {
        if (counter != userImagesStoriesData.size) {
            try {

                Glide.with(this)
                    .load(Base64.decode(userImagesStoriesData[++counter], Base64.DEFAULT))
                    .into(storyImageView)
            } catch (e: Exception) {
                onComplete()
            }
        } else
            onComplete()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        Glide.with(this)
            .load(Base64.decode(userImagesStoriesData[--counter], Base64.DEFAULT))
            .into(storyImageView)
    }

    override fun onComplete() {

    }

    private fun getStoryData(): ArrayList<String> {
        val userImagesStoriesData = ArrayList<String>()

        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)
        val userImage = SharedPrefrenceManager.getProfileImg(this)

        if (userImage != Constants.NULL)
            userImagesStoriesData.add(userImage)
        if (firstImage != Constants.NULL)
            userImagesStoriesData.add(firstImage)
        if (secondImage != Constants.NULL)
            userImagesStoriesData.add(secondImage)
        if (thirdImage != Constants.NULL)
            userImagesStoriesData.add(thirdImage)
        if (fourthImage != Constants.NULL)
            userImagesStoriesData.add(fourthImage)
        if (fifthImage != Constants.NULL)
            userImagesStoriesData.add(fifthImage)
        if (sixthImage != Constants.NULL)
            userImagesStoriesData.add(sixthImage)
        return userImagesStoriesData
    }

    var bottomSheet: UserDetalBottomSheetFragment? = null;
    private fun setupUserDetailBottomSheet() {
        if (bottomSheet != null) {
            supportFragmentManager.beginTransaction().remove(bottomSheet!!).commit();
        }
        bottomSheet = UserDetalBottomSheetFragment(this)
        bottomSheet!!.allowEnterTransitionOverlap=true
        bottomSheet!!.allowReturnTransitionOverlap=true

        bottomSheet?.show(supportFragmentManager, "userDetailBottomSheet")

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

        if (isIncognito) {
            SharedPrefrenceManager.setisIncognito(this, false)
            val ll = LayoutInflater.from(this).inflate(R.layout.incoginito_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val gotItBtn: Button = dialog.find(R.id.got_it_btn)
            gotItBtn.setOnClickListener {
                user_detail_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost_on)
                user_detail_incoginoti_btn.colorFilter = null
                dialog.dismiss()
            }
            dialog.show()
        } else {
            user_detail_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)
            user_detail_incoginoti_btn.setColorFilter(ContextCompat.getColor(this, R.color.white))
            SharedPrefrenceManager.setisIncognito(this, true)
        }

    }


}
