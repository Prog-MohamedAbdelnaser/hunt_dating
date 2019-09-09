package com.recep.hunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.login.SocialLoginActivity
import com.recep.hunt.utilis.SharedPrefrenceManager

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val isOtpVerified = SharedPrefrenceManager.getIsOtpVerified(this@SplashActivity)
            val isLoggedIn = SharedPrefrenceManager.getIsLoggedIn(this)
            if(isOtpVerified){
                if(isLoggedIn){
                    val intent = Intent(applicationContext,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(applicationContext,SocialLoginActivity::class.java)
//                    val intent = Intent(applicationContext,UserProfileSettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                val intent = Intent(applicationContext,SocialLoginActivity::class.java)
//                val intent = Intent(applicationContext, SocialLoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
