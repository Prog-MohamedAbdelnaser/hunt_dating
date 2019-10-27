package com.recep.hunt.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import com.recep.hunt.R
import com.recep.hunt.swipe.model.SwipeUserModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class SwipeMainActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var parentView : RelativeLayout
    private lateinit var context : Context

    var CLICK_ACTION_THRESHOLD : Int = 200
    var windowwidth : Int = 0
    var screenCenter : Int = 0
    var x_cord : Int = 0
    var y_cord : Int = 0
    var x: Int = 0
    var y : Int = 0
    var startX: Float = 0.0f
    var startY : Float = 0.0f
    var startClickTime : Long = 0L
    var Likes : Int = 0
    private var items =  ArrayList<SwipeUserModel>()

    private lateinit var matchProgressBar: ProgressBar
    private lateinit var attendanceProgressBar: ProgressBar
    var counter = ArrayList<Int>()
    var currentUser : Int = 0
    private var storyProgressViews = ArrayList<StoriesProgressView>()
    private var storyImageView = ArrayList<ImageView>()
    var dummyImages : IntArray = intArrayOf(R.drawable.demo_user, R.drawable.demo_user_1, R.drawable.demo_user_2, R.drawable.demo_user_3, R.drawable.demo_user_4)
    private lateinit var homeView : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_main)
        init()
    }

    @SuppressLint("NewApi")
    private fun init() {
        context = this
        parentView = find(R.id.main_layoutView)
        windowwidth = windowManager.defaultDisplay.width
        screenCenter = windowwidth / 2
        items = dummyUsersdata()

        var size = items.size - 1

        for (i in 0..size) {

            var containerView = LayoutInflater.from(context).inflate(R.layout.swipe_screen_item, null)
            //match status progress bar
            matchProgressBar = containerView.findViewById(R.id.match_status_progressBar)
            val drawableMatch = context.resources.getDrawable(R.drawable.circular_progressbar_inside_bg)
            matchProgressBar.progressDrawable = drawableMatch
            matchProgressBar.progress = 75
            matchProgressBar.max = 100

            attendanceProgressBar = containerView.findViewById(R.id.attendance_status_progressBar)
            val drawableAttendance = context.resources.getDrawable(R.drawable.circular_progressbar_attendance_inside_bg)
            attendanceProgressBar.progressDrawable = drawableAttendance
            attendanceProgressBar.progress = 90
            attendanceProgressBar.max = 100

            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            containerView.layoutParams = layoutParams

            containerView.tag = i
            storyProgressViews.add(containerView.findViewById(R.id.stories))
            storyImageView.add(containerView.findViewById(R.id.story_image_userdetail))
            val count = 5
            counter.add(0)
            currentUser = i
            storyProgressViews[i].setStoriesCount(count)
            storyProgressViews[i].setStoryDuration(1000L)

            Picasso.get().load(dummyImages[0]).fit().centerCrop().into(storyImageView[i], object:
                Callback {
                override fun onSuccess() {
                    if (i == size)
                        storyProgressViews[i].startStories()
                }

                override fun onError(e: Exception?) {

                }
            })

            homeView = containerView.findViewById(R.id.return_home_layout)
            val titleView = containerView.findViewById<TextView>(R.id.user_detail_username_txtView)
            titleView.text = items[i].title
            homeView.setOnClickListener {
                finish()
            }

            //Show next stories
            storyProgressViews[i].setStoriesListener(this)

            val relativeLayoutContainer = containerView.findViewById<ConstraintLayout>(R.id.relative_container)

            //Touch listener on the layout to swipe image right or left
            relativeLayoutContainer.setOnTouchListener(View.OnTouchListener { v, event ->
                x_cord = event.rawX.toInt()
                y_cord = event.rawY.toInt()

                containerView.x = 0f
                containerView.y = 0f

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.x.toInt()
                        y = event.y.toInt()
                        startX = event.x
                        startY = event.y
                        startClickTime = Calendar.getInstance().timeInMillis

                        Log.v("On touch", x.toString() + " " + y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        storyProgressViews[currentUser].pause()

                        x_cord = event.rawX.toInt()
                        y_cord = event.rawY.toInt()

                        val endX = event.x
                        val endY = event.y

                        if (event.eventTime - event.downTime < 200) {
                            containerView.x = 0f
                            containerView.y = 0f
                        }
                        else {
                            containerView.x = ( x_cord - x ).toFloat()
                            containerView.rotation = ((x_cord - x) * (Math.PI / 256)).toFloat()

                            if (x_cord > x) {
                                containerView.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 1.0f
                                containerView.findViewById<ImageView>(R.id.like_dislike_imageView).imageResource = R.drawable.swipe_like
                                if ( x_cord - x >= 255 || (x_cord - x) % 255 >= 179)
                                    containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb(179, 58, 204, 225))
                                else if ( (x_cord - x) % 255 < 179)
                                    containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb((x_cord - x) % 255, 58, 204, 225))
                                if (x_cord >= (screenCenter + (screenCenter / 4))) {
                                    Likes = 2
                                } else {
                                    Likes = 0
                                }
                            }
                            else if (x_cord < x){
                                containerView.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 1.0f
                                containerView.findViewById<ImageView>(R.id.like_dislike_imageView).imageResource = R.drawable.swipe_dislike
                                if (x - x_cord >= 255 || (x - x_cord) % 255 >= 153)
                                    containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb(153, 255, 42, 78))
                                else if ( (x - x_cord) % 255 < 153)
                                    containerView.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb((x - x_cord) % 255, 255, 42, 78))
                                if (x_cord <= screenCenter - screenCenter / 4) {
                                    Likes = 1
                                } else {
                                    Likes = 0
                                }
                            }
                            else {
                                containerView.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 0.0f
                                containerView.findViewById<ImageView>(R.id.story_image_userdetail).colorFilter = null
                                Likes = 0
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        containerView.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 0.0f
                        containerView.findViewById<ImageView>(R.id.story_image_userdetail).colorFilter = null

                        var clickDuration = event.eventTime - event.downTime
                        if (clickDuration < 200) {
                            Log.e("Event_Status :->", "Only Clicked")
                            if (x >= screenCenter) {
                                onNext()
                            }
                            else {
                                onPrev()
                            }
                            v.parent.requestDisallowInterceptTouchEvent(true)
                        }

                        else {
                            x_cord = event.rawX.toInt()
                            y_cord = event.rawY.toInt()

                            if (Likes == 0) {
                                if (currentUser > 0) {
                                    parentView.getChildAt(currentUser - 1).scaleX = 0.95f
                                    parentView.getChildAt(currentUser - 1).scaleY = 0.95f
                                }

                                containerView.animate().x(0f).y(0f).rotation(0f).setDuration(300)
//
                                storyProgressViews[currentUser].resume()
                            } else if (Likes == 1) {
                                Log.e("Event_Status :->", "Unlike")
                                parentView.removeView(containerView)
                                if (currentUser > 0) {
                                    storyProgressViews[currentUser - 1].startStories()
                                    currentUser --
                                    parentView.getChildAt(currentUser).animate().scaleX(1f).scaleY(1f)
                                    parentView.getChildAt(currentUser).elevation = 5f
                                }

                            } else if (Likes == 2) {
                                Log.e("Event_Status :->", "Liked")
                                parentView.removeView(containerView)
                                if (currentUser > 0) {
                                    storyProgressViews[currentUser - 1].startStories()
                                    currentUser --
                                    parentView.getChildAt(currentUser).animate().scaleX(1f).scaleY(1f)
                                    parentView.getChildAt(currentUser).elevation = 5f
                                }
                            }
                        }
                    }
                }
                true
            })

            containerView.scaleX = 0.95f
            containerView.scaleY = 0.95f
            containerView.elevation = 5f
            if ( i == size) {
                containerView.scaleX = 1f
                containerView.scaleY = 1f
            }
            parentView.addView(containerView)
        }
    }

    //Dummy Users Data
    private fun dummyUsersdata():ArrayList<SwipeUserModel>{
        val data = ArrayList<SwipeUserModel>()
        if(data.size == 0){
            data.add(
                SwipeUserModel(
                    "Valentina, 28",
                    "Actor at Max Studio"
                )
            )
            data.add(
                SwipeUserModel(
                    "Stella, 28",
                    "Model at Fashion Club"
                )
            )
            data.add(
                SwipeUserModel(
                    "Serena, 23",
                    "Digital Artist at Blue Tea Productions"
                )
            )
            data.add(
                SwipeUserModel(
                    "Jasmin, 25",
                    "Technical Producer"
                )
            )
            data.add(
                SwipeUserModel(
                    "Allena, 26",
                    "Pop Singer"
                )
            )
        }
        return data
    }

    fun isAClick(startX : Float, endX : Float, startY : Float, endY : Float): Boolean {
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        return !(differenceX > CLICK_ACTION_THRESHOLD || differenceY > CLICK_ACTION_THRESHOLD)
    }

    override fun onComplete() {
        counter[currentUser] = 4
    }

    override fun onPrev() {
        if ( counter[currentUser] > 0) {
            counter[currentUser] --
            Picasso.get().load(dummyImages[counter[currentUser]]).fit().centerCrop().into(storyImageView[currentUser])
        }
    }

    override fun onNext() {
        if ( counter[currentUser] < 4) {
            counter[currentUser] ++
            Picasso.get().load(dummyImages[counter[currentUser]]).fit().centerCrop().into(storyImageView[currentUser])
        }
    }
}
