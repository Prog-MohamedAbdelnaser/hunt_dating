package com.recep.hunt

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.recep.hunt.constants.Constants
import com.recep.hunt.location.TurnOnGPSActivity
import com.recep.hunt.login.LoginActivity
import com.recep.hunt.login.SocialLoginActivity
import com.recep.hunt.setupProfile.SetupProfileDobActivity
import com.recep.hunt.setupProfile.SetupProfileGenderActivity
import com.recep.hunt.setupProfile.SetupProfileUploadPhotoActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val isOtpVerified = SharedPrefrenceManager.getIsOtpVerified(this@SplashActivity)
            if(isOtpVerified){
                    val intent = Intent(applicationContext,SocialLoginActivity::class.java)
                    startActivity(intent)
                    finish()

            }else{
                val intent = Intent(applicationContext,LoginActivity::class.java)
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
