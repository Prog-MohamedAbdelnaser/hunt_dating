package com.recep.hunt.login

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var viewPager:ViewPager
    private lateinit var indicators:TabLayout
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
    }
}
class WelcomePagerAdapter():PagerAdapter(){
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(R.layout.on_board_adapter, container, false)
//        item.on_board_adapter_title_view.text = context.resources.getString(titleArray[position])
//        item.on_board_adapter_subtitle_view.text = context.resources.getString(subtitle[position])
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
