package com.recep.hunt.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_continue_as_social.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

class ContinueAsSocialActivity : AppCompatActivity() {

    private var socialLoginType = ""
    private lateinit var backButton : ImageButton
    private lateinit var socialImage : ImageView
    private lateinit var continueUserNameTv : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_as_social)

        init()
    }
    private fun init(){
        socialLoginType = intent.getStringExtra(SocialLoginActivity.socialTypeKey)
        backButton = find(R.id.continue_social_back_btn)
        socialImage = find(R.id.continue_as_social_image)
        continueUserNameTv = find(R.id.continue_as_username_txtView)

        backButton.setOnClickListener { finish() }
        edit_this_continue_as_btn.setOnClickListener {
            launchActivity<InfoYouProvideActivity>()
        }
        continueUserNameTv.text = getString(R.string.continue_as,"Rishabh")
        setupSocialLoginImage()

    }
    private fun setupSocialLoginImage(){
        when(socialLoginType){
            Constants.socialFBType -> {      //Fb Icon with fb bg


                socialImage.image = resources.getDrawable(R.drawable.fb_icon)
                socialImage.background = resources.getDrawable(R.drawable.fb_bg_color)

            }
            Constants.socialInstaType -> {      //Instagram Icon with Instagram bg


                socialImage.image = resources.getDrawable(R.drawable.insta_icon)
                socialImage.background = resources.getDrawable(R.drawable.insta_bg_image)

            }
            Constants.socialGoogleType -> {      //google Icon with google bg


                socialImage.image = resources.getDrawable(R.drawable.google_icon)
                socialImage.background = resources.getDrawable(R.drawable.google_bg_color)

            }

        }
    }
}
