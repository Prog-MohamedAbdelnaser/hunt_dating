package com.recep.hunt.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.crashlytics.android.Crashlytics
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.setupProfile.TurnOnGPSActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_welcome_screen.*
import kotlinx.android.synthetic.main.on_board_adapter.view.*
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList


class WelcomeScreenActivity : AppCompatActivity() {

    companion object {
        const val numberKey = "userPhoneNumberKey"
        const val otpKey = "otpKey"
        const val verificationIdKey = "verificationId"
        const val countryCodeKey = "countryCodeKey"
        val observeEditText = MutableLiveData<Boolean>()
    }

    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    private lateinit var countryCodePicker: CountryCodePicker
    private var verificationId = ""

    private lateinit var videoView: VideoView
    private lateinit var viewPager: ViewPager
    private lateinit var indicators: TabLayout
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500//delay in m illiseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        mAuth = FirebaseAuth.getInstance()
        my_scroll_view.scrollToBottom()

        observeEditText.observe(this, androidx.lifecycle.Observer {
            user_number_edittext.requestFocus()
            my_scroll_view.scrollToBottom()
        })
        init()
    }

    fun ScrollView.scrollToBottom() {
        // use this for scroll immediately
        scrollTo(0, this.getChildAt(0).height)

    }

    private fun init() {
        dialog =
            Helpers.showDialog(this@WelcomeScreenActivity, this@WelcomeScreenActivity, "Verifying")
        countryCodePicker = find(R.id.ccp)

        user_number_edittext.setImeOptions(EditorInfo.IME_ACTION_DONE);

        user_number_edittext.requestFocus()
        my_scroll_view.scrollToBottom()


        user_number_edittext.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                val number = user_number_edittext.text.toString()
                val numberCode = countryCodePicker.selectedCountryCodeWithPlus
                val selectedCountry = countryCodePicker.selectedCountryName


                if (number.isNotEmpty()) {
                    try {
                        dialog.show()
                    } catch (e: Exception) {
                    }

                    SharedPrefrenceManager.setUserCountryCode(
                        this@WelcomeScreenActivity,
                        numberCode
                    )
                    SharedPrefrenceManager.setUserCountry(
                        this@WelcomeScreenActivity,
                        selectedCountry
                    )

//                launchActivity<SocialLoginActivity> ()
                    launchActivity<OtpVerificationActivity> {
                        putExtra(WelcomeScreenActivity.verificationIdKey, verificationId)
                        putExtra(WelcomeScreenActivity.otpKey, "")
                        putExtra(WelcomeScreenActivity.countryCodeKey, numberCode)
                        putExtra(WelcomeScreenActivity.numberKey, number)
                    }

                } else {
                    Helpers.showErrorSnackBar(this@WelcomeScreenActivity, "Enter number", "")
                }
                return true
            }

        })

        videoView = find(R.id.video_view)
        viewPager = find(R.id.welcome_screen_viewPager)
        indicators = find(R.id.welcome_screen_indicator)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.moments)
        videoView.setVideoURI(uri)
        videoView.start()
        videoView.setOnCompletionListener { videoView.start() }

        login_nxt_btn.setOnClickListener {
            //            launchActivity<SocialLoginActivity>()
            //11.10.19 Guang
            val number = user_number_edittext.text.toString()
            val numberCode = countryCodePicker.selectedCountryCodeWithPlus
            val selectedCountry = countryCodePicker.selectedCountryName


            if (number.isNotEmpty()) {

                try {
                    dialog.show()
                } catch (e: Exception) {
                }

                SharedPrefrenceManager.setUserCountryCode(this@WelcomeScreenActivity, numberCode)
                SharedPrefrenceManager.setUserCountry(this@WelcomeScreenActivity, selectedCountry)

                dialog.dismiss()
//                launchActivity<SocialLoginActivity> ()
                launchActivity<OtpVerificationActivity> {
                    putExtra(WelcomeScreenActivity.verificationIdKey, verificationId)
                    putExtra(WelcomeScreenActivity.otpKey, "")
                    putExtra(WelcomeScreenActivity.countryCodeKey, numberCode)
                    putExtra(WelcomeScreenActivity.numberKey, number)
                }

            } else {
                Helpers.showErrorSnackBar(this@WelcomeScreenActivity, "Enter number", "")
            }
        }
        setupViewPager()
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        user_number_edittext.requestFocus()

        videoView.start()
    }

    private fun setupViewPager() {
        val titleArray = arrayListOf(
            R.string.wanna_go_hunting,
            R.string.wanna_go_hunting1,
            R.string.wanna_go_hunting2
        )
        val subtitleArray = arrayListOf(
            R.string.be_part_of_hunt,
            R.string.be_part_of_hunt1,
            R.string.be_part_of_hunt2
        )
        user_number_edittext.requestFocus()
        my_scroll_view.scrollToBottom()


        viewPager.adapter = WelcomePagerAdapter(this, subtitleArray)
        indicators.setupWithViewPager(viewPager)

        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            user_number_edittext.requestFocus()
            my_scroll_view.scrollToBottom()

            if (currentPage == 3) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO
                ),
                TurnOnGPSActivity.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        user_number_edittext.requestFocus()
        my_scroll_view.scrollToBottom()


    }

}

class WelcomePagerAdapter(private val context: Context, private val subtitle: ArrayList<Int>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context)
            .inflate(R.layout.on_board_adapter, container, false)
        item.on_board_adapter_subtitle_view.text = context.getString(subtitle[position])
        WelcomeScreenActivity.observeEditText.value = true
        container.addView(item)
        return item
    }


    override fun getCount(): Int {
        return subtitle.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
