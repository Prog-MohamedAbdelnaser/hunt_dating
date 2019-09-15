package com.recep.hunt.userDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.recep.hunt.R
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.find

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        init()
    }
    private fun init(){

        setupUserDetailBottomSheet()
    }
    private fun setupUserDetailBottomSheet(){
        val bottomSheet = UserDetalBottomSheetFragment(this)
        bottomSheet.show(supportFragmentManager, "userDetailBottomSheet")
    }
}
