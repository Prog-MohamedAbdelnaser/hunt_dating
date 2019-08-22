package com.recep.hunt.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    companion object{
        const val numberKey = "userPhoneNumberKey"
    }
    private lateinit var viewPager : ViewPager
    private lateinit var springDotsIndicator: SpringDotsIndicator
    private lateinit var countryCodeSpinner:Spinner
    private var countryCodes = listOf("+91","+92","+93")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }
    private fun init(){
        viewPager = find(R.id.login_viewPager)
        springDotsIndicator = find(R.id.login_spring_dots_indicator)
        countryCodeSpinner = find(R.id.country_code_spinner)

        login_nxt_btn.setOnClickListener {
            val number = user_number_edittext.text.toString()
            if(number.isNotEmpty()){
                launchActivity<OtpVerificationActivity>{
                    putExtra(numberKey,number)
                }
            }else{
                toast("Enter Number")
            }

        }

        setupViewPager()
        setupCountryCodeSpinner()

    }
    //Setting up view pager
    private fun setupViewPager(){
        val imagesArray = arrayListOf(R.drawable.on_board_bg_1,R.drawable.on_board_bg_1,R.drawable.on_board_bg_1)
        val adapter = OnBoardAdapter(this@LoginActivity,imagesArray)
        viewPager.adapter = adapter
        springDotsIndicator.setViewPager(viewPager)
    }

    private fun setupCountryCodeSpinner(){
        val aa = ArrayAdapter(this@LoginActivity, android.R.layout.simple_spinner_item, countryCodes)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        countryCodeSpinner.adapter = aa
    }


}
class OnBoardAdapter(private val context: Context,private val images:ArrayList<Int>) : PagerAdapter(){
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(context).inflate(R.layout.on_board_adapter, container, false)
        //adapter imageView - change it with positions
//        val adapterImage : ImageView = item.find(R.id.on_board_image_View)
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