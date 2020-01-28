package com.recep.hunt.matchs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.recep.hunt.R
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import kotlinx.android.synthetic.main.activity_lets_meet.*


class LetsMeetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lets_meet)
        displaySwipeUser()
    }

//    override fun onPause() {
//        super.onPause()
//        makeUserOfline()
//    }
//
//
//
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
//                    if (Utils.isSessionExpire(this@LetsMeetActivity, strErrorJson)) {
//                        return
//                    }
//                }
//            }
//
//        })
//
//    }


    private fun displaySwipeUser() {
        if (intent != null && intent.extras != null) {
            val mSwipeUserModel = intent.getParcelableExtra<SwipeUserModel>("swipeUsers")
            if (mSwipeUserModel != null) {
                Glide.with(this)
                    .load(mSwipeUserModel.images!![0])
                    .centerCrop()
                    .into(ivLikedPersonImage)

                val userProfile = SharedPrefrenceManager.getProfileImg(this)

                if (userProfile.contains("http")) {
                    Glide.with(this)
                        .load(userProfile)
                        .centerCrop()
                        .into(ivUserImage)
                }

//                tvWantToMeetWithQ.text = "Do you want to meet with ${mSwipeUserModel.firstName}?"
                textView47.text = "You and ${mSwipeUserModel.firstName} like each other!"

            }
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        startActivity(Intent(this@LetsMeetActivity, HomeActivity::class.java))
//        finish()
//    }

}
