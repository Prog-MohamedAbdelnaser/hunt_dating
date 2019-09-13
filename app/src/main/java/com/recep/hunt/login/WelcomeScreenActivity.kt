package com.recep.hunt.login

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.recep.hunt.R
import com.recep.hunt.login.adapter.OnBoardAdapter
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_welcome_screen.*
import org.jetbrains.anko.find
import java.util.*




class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var viewPager:ViewPager
    private lateinit var indicators:TabLayout
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        init()
    }

    private fun init(){
        videoView = find(R.id.video_view)
        viewPager = find(R.id.welcome_screen_viewPager)
        indicators = find(R.id.welcome_screen_indicator)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.moments)
        videoView.setVideoURI(uri)
        videoView.start()
        videoView.setOnCompletionListener { videoView.start() }

        login_nxt_btn.setOnClickListener {
            launchActivity<SocialLoginActivity>()
        }
        setupViewPager()
    }
    private fun setupViewPager(){
        val titleArray = arrayListOf(R.string.wanna_go_hunting,R.string.wanna_go_hunting1,R.string.wanna_go_hunting2)
        val subtitleArray = arrayListOf(R.string.be_part_of_hunt,R.string.be_part_of_hunt2,R.string.be_part_of_hunt1)
        viewPager.adapter = WelcomePagerAdapter()
        indicators.setupWithViewPager(viewPager)

        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == 3) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread
        timer?.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }
}
class WelcomePagerAdapter():PagerAdapter(){
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(R.layout.on_board_adapter, container, false)
        container.addView(item)
        return item
    }


    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
