package com.recep.hunt.swipe.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.recep.hunt.R
import com.recep.hunt.swipe.SwipeActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.lang.Exception

class SwipeScreenAdapter(val context: Context, private val items: ArrayList<SwipeUserModel>?) : BaseAdapter(), StoriesProgressView.StoriesListener{


    var dummyImages : IntArray = intArrayOf(R.drawable.demo_user, R.drawable.demo_user_1, R.drawable.demo_user_2, R.drawable.demo_user_3, R.drawable.demo_user_4)
    var counter = 0
    private lateinit var storyProgressView: StoriesProgressView
    private lateinit var storyImageView: ImageView
    private lateinit var homeView : ConstraintLayout
    private lateinit var nextView: View
    private lateinit var prevView: View
    private lateinit var matchProgressBar: ProgressBar
    private lateinit var attendanceProgressBar: ProgressBar
    private val activity : SwipeActivity = context as SwipeActivity

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView = convertView
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.swipe_screen_item, parent,false)
        }
        //match status progress bar
        matchProgressBar = rowView!!.findViewById(R.id.match_status_progressBar)
        val drawableMatch = context.resources.getDrawable(R.drawable.circular_progressbar_inside_bg)
        matchProgressBar.progressDrawable = drawableMatch
        matchProgressBar.progress = 75
        matchProgressBar.max = 100

        attendanceProgressBar = rowView!!.findViewById(R.id.attendance_status_progressBar)
        val drawableAttendance = context.resources.getDrawable(R.drawable.circular_progressbar_attendance_inside_bg)
        attendanceProgressBar.progressDrawable = drawableAttendance
        attendanceProgressBar.progress = 90
        attendanceProgressBar.max = 100

        storyProgressView = rowView!!.findViewById(R.id.stories)
        storyImageView = rowView!!.findViewById(R.id.story_image_userdetail)
        val count = 5
        counter = 0
        storyProgressView.setStoriesCount(count)
        storyProgressView.setStoryDuration(1000L)

        Picasso.get().load(dummyImages[0]).fit().centerCrop().into(storyImageView, object:
            Callback {
            override fun onSuccess() {
                storyProgressView.startStories()
            }

            override fun onError(e: Exception?) {

            }

        })
        homeView = rowView!!.findViewById(R.id.return_home_layout)
        val titleView = rowView!!.findViewById<TextView>(R.id.user_detail_username_txtView)
        titleView.text = items!![position].title
        homeView.setOnClickListener {
            activity.finish()
        }
        nextView = rowView!!.findViewById(R.id.skip_detail)
        prevView = rowView!!.findViewById(R.id.reverse_detail)
        nextView.setOnClickListener {
            onNext()
        }
        prevView.setOnClickListener {
            onPrev()
        }


        //Show next stories
        storyProgressView.setStoriesListener(this)
        return rowView!!
    }

    override fun onComplete() {
        counter = 4
    }

    override fun onPrev() {
        if ( counter > 0) {
            counter --
            Picasso.get().load(dummyImages[counter]).fit().centerCrop().into(storyImageView)
        }
    }

    override fun onNext() {
        if ( counter < 4) {
            counter ++
            Picasso.get().load(dummyImages[counter]).fit().centerCrop().into(storyImageView)
        }
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount() = items!!.size

}