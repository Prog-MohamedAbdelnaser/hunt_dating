package com.recep.hunt.userDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.find





class UserDetailActivity : AppCompatActivity(),StoriesProgressView.StoriesListener {


    var counter = 0
    private lateinit var storyProgressView: StoriesProgressView
    private lateinit var storyImageView: ImageView
    private var userImagesStoriesData = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        init()
    }
    private fun init(){
        storyProgressView = find(R.id.stories)
        storyImageView = find(R.id.story_image_userdetail)
        setupUserDetailBottomSheet()
        val count = getStoryData().size
        userImagesStoriesData = getStoryData()
        storyProgressView.setStoriesCount(count)
        storyProgressView.setStoryDuration(3500L)
        storyImageView.setImageBitmap(Helpers.stringToBitmap(userImagesStoriesData[counter]))
        storyProgressView.setStoriesListener(this)
        storyProgressView.startStories()

        skip.setOnClickListener { onNext() }
        reverse.setOnClickListener { onPrev() }

    }
    override fun onNext() {
        storyImageView.setImageBitmap(Helpers.stringToBitmap(userImagesStoriesData[++counter]))
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        storyImageView.setImageBitmap(Helpers.stringToBitmap(userImagesStoriesData[--counter]))
    }

    override fun onComplete() {

    }

    private fun getStoryData():ArrayList<String> {
        val userImagesStoriesData = ArrayList<String>()
        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)

        userImagesStoriesData.add(SharedPrefrenceManager.getUserImage(this))

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

        return userImagesStoriesData
    }
    private fun setupUserDetailBottomSheet(){
        val bottomSheet = UserDetalBottomSheetFragment(this)
        bottomSheet.show(supportFragmentManager, "userDetailBottomSheet")
    }
}
