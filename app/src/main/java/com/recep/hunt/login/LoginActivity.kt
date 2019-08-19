package com.recep.hunt.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.find

class LoginActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager
    private lateinit var springDotsIndicator: SpringDotsIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }
    private fun init(){
        viewPager = find(R.id.login_viewPager)
        springDotsIndicator = find(R.id.login_spring_dots_indicator)
        setupViewPager()

        login_nxt_btn.setOnClickListener {
            launchActivity<OtpVerificationActivity>()
        }

    }
    //Setting up view pager
    private fun setupViewPager(){
        val images = arrayListOf(R.drawable.on_board_bg_1,R.drawable.on_board_bg_1,R.drawable.on_board_bg_1)
        val adapter = OnBoardAdapter(images)
        viewPager.adapter = adapter
        springDotsIndicator.setViewPager(viewPager)
    }


}
class OnBoardAdapter(private val images:ArrayList<Int>) : PagerAdapter(){
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(R.layout.on_board_adapter, container, false)
        //adapter imageView - change it with positions

        val adapterImage : ImageView = item.find(R.id.on_board_image_View)
        container.addView(item)
        return item
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}