package com.recep.hunt.userDetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image


class UserDetailActivity : AppCompatActivity(),StoriesProgressView.StoriesListener{

    var counter = 0
    private lateinit var storyProgressView: StoriesProgressView
    private lateinit var storyImageView: ImageView
    private var userImagesStoriesData = ArrayList<String>()
    private var isIncognito :Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        isIncognito=SharedPrefrenceManager.getisIncognito(applicationContext)
        init()
    }
    private fun init(){
        storyProgressView = find(R.id.stories)
        storyImageView = find(R.id.story_image_userdetail)
        setupUserDetailBottomSheet()
        val count = getStoryData().size
        Log.e("Stories","Total count : $count")

        userImagesStoriesData = getStoryData()
        if(userImagesStoriesData.size != 0 ){
            storyProgressView.setStoriesCount(count)
            storyProgressView.setStoryDuration(3500L)
            storyImageView.setImageBitmap(Helpers.stringToBitmap(userImagesStoriesData[counter]))
            storyProgressView.setStoriesListener(this)
            skip.setOnClickListener { onNext() }
            reverse.setOnClickListener { onPrev() }
            storyProgressView.startStories()
        }

        if(isIncognito){
            
        }




    }
    override fun onNext() {
        if(counter != userImagesStoriesData.size)
        storyImageView.setImageBitmap(Helpers.stringToBitmap(userImagesStoriesData[++counter]))
        else
            onComplete()
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

        return userImagesStoriesData
    }
    private fun setupUserDetailBottomSheet(){
        val bottomSheet = UserDetalBottomSheetFragment(this)
        bottomSheet.show(supportFragmentManager, "userDetailBottomSheet")
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

        if(isIncognito){
            SharedPrefrenceManager.setisIncognito(this,false)
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
        }else{
            user_detail_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)
            user_detail_incoginoti_btn.setColorFilter(ContextCompat.getColor(this, R.color.white))
            SharedPrefrenceManager.setisIncognito(this,true)
        }

    }


}
