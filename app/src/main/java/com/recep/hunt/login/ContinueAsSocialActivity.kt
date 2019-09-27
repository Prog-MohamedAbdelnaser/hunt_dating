package com.recep.hunt.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.login.model.UserSocialModel
import com.recep.hunt.setupProfile.SetupProfileGenderActivity
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_continue_as_social.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import java.lang.Exception

class ContinueAsSocialActivity : AppCompatActivity() {

    private var socialLoginType = ""
    private lateinit var userDetailModel: UserSocialModel
    private lateinit var backButton: ImageButton
    private lateinit var socialImage: ImageView
    private lateinit var continueUserNameTv: TextView
    private lateinit var userImageView: CircleImageView

    //    var userImage = ""
    var userName = ""
    var userId = ""
    var userEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_as_social)
        init()
    }

    private fun init() {

        try {
            socialLoginType = intent.getStringExtra(SocialLoginActivity.socialTypeKey)
            val userJson = intent.getStringExtra(SocialLoginActivity.userSocialModel)
            userDetailModel = Gson().fromJson(userJson, UserSocialModel::class.java)
            backButton = find(R.id.continue_social_back_btn)
            socialImage = find(R.id.continue_as_social_image)
            continueUserNameTv = find(R.id.continue_as_username_txtView)
            userImageView = find(R.id.continue_as_user_imageView)

            userName = userDetailModel.userName

            var img: String = userDetailModel.userImage
            // Picasso.get().load(userDetailModel.userImage.replace("s96-c", "s384-c", true)).into(userImageView)
            Picasso.get().load(userDetailModel.userImage).into(userImageView)
            backButton.setOnClickListener {
                finish(
                )
            }
            edit_this_continue_as_btn.setOnClickListener {
                launchActivity<InfoYouProvideActivity>()
            }
            continueUserNameTv.text = userName
            setupSocialLoginImage()

            continue_as_social_nextBtn.setOnClickListener {
                launchActivity<SetupProfileGenderActivity>()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setupSocialLoginImage() {
        when (socialLoginType) {
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
