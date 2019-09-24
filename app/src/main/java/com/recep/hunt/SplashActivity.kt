package com.recep.hunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.inviteFriend.InviteAFriendActivity
import com.recep.hunt.inviteFriend.InviteFriendContactActivity
import com.recep.hunt.login.LoginActivity
import com.recep.hunt.login.SocialLoginActivity
import com.recep.hunt.login.WelcomeScreenActivity
import com.recep.hunt.payment.PaymentMethodActivity
import com.recep.hunt.payment.SelectPaymentMethodsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.setupProfile.*
import com.recep.hunt.userDetail.UserDetailActivity
import com.recep.hunt.utilis.SharedPrefrenceManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 6900 //3 seconds
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val isOtpVerified = SharedPrefrenceManager.getIsOtpVerified(this@SplashActivity)
            val isLoggedIn = SharedPrefrenceManager.getIsLoggedIn(this)
            if (isOtpVerified) {
                if (isLoggedIn) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, SocialLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this, WelcomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Glide.with(this).asGif().load(R.drawable.ani).into(splashImage)
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

}
