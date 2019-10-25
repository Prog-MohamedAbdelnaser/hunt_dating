package com.recep.hunt.swipe

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_swipe.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.lang.Exception

class SwipeActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {


    var counter = 0
    var dummyImages : IntArray = intArrayOf(R.drawable.demo_user, R.drawable.demo_user_1, R.drawable.demo_user_2, R.drawable.demo_user_3, R.drawable.demo_user_4)

    private lateinit var storyProgressView: StoriesProgressView
    private lateinit var storyImageView: ImageView
    private var userImagesStoriesData = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)
        init()
    }

    private fun init() {
        storyProgressView = find(R.id.stories)
        storyImageView = find(R.id.story_image_userdetail)
        val count = 5

//        userImagesStoriesData = getStoryData()
//        if(userImagesStoriesData.size != 0 ){
            storyProgressView.setStoriesCount(count)
            storyProgressView.setStoryDuration(1000L)

            Picasso.get().load(dummyImages[0]).fit().centerCrop().into(storyImageView, object: Callback {
                override fun onSuccess() {
                    storyProgressView.startStories()
                }

                override fun onError(e: Exception?) {

                }

            })

            //Show next stories
            storyProgressView.setStoriesListener(this)
            skip_detail.setOnClickListener{ onNext() }
            reverse_detail.setOnClickListener{ onPrev() }

//        }
    }

    override fun onComplete() {
//        counter = 0
//        toast("Load finished")
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

    /*
    private fun getStoryData():ArrayList<String> {
        val userImagesStoriesData = ArrayList<String>()

        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)
        val userImage = SharedPrefrenceManager.getProfileImg(this)

        if(userImage != Constants.NULL)
            userImagesStoriesData.add(userImage)
        if(firstImage != Constants.NULL)
            userImagesStoriesData.add(firstImage)
        if(secondImage != Constants.NULL)
            userImagesStoriesData.add(secondImage)
        if(thirdImage != Constants.NULL)
            userImagesStoriesData.add(thirdImage)
        if(fourthImage != Constants.NULL)
            userImagesStoriesData.add(fourthImage)
        if(fifthImage != Constants.NULL)
            userImagesStoriesData.add(fifthImage)
        if(sixthImage != Constants.NULL)
            userImagesStoriesData.add(sixthImage)

        userImagesStoriesData.add("")

        return userImagesStoriesData
    }
    */

    override fun onDestroy() {
        storyProgressView.destroy()
        super.onDestroy()
    }
}
