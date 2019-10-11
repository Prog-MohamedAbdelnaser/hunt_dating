package com.recep.hunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.login.SocialLoginActivity
import com.recep.hunt.login.WelcomeScreenActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import kotlinx.android.synthetic.main.activity_splash.*
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //TODO
        Helpers.setupBasicSharedPrefrences(this)

        Glide.with(this).asGif().load(R.drawable.splashanimation)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if(resource != null){
                        resource.setLoopCount(1)
                        resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback(){
                            override fun onAnimationEnd(drawable: Drawable?) {
                                super.onAnimationEnd(drawable)
                                segueToApp()

                            }
                        })
                    }
                    return false
                }
            }).into(splashImage)

    }
    private fun segueToApp(){
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
