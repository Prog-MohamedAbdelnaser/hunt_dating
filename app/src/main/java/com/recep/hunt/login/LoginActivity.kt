package com.recep.hunt.login

import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import android.widget.AdapterView
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.adapters.OnBoardAdapter
import com.recep.hunt.utilis.Helpers
import java.util.concurrent.TimeUnit


//9560246054
class LoginActivity : AppCompatActivity() {

    companion object{
        const val numberKey = "userPhoneNumberKey"
        const val otpKey = "otpKey"
        const val verificationIdKey = "verificationId"
    }
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog : KProgressHUD

    private var verificationId = ""
    private lateinit var viewPager : ViewPager
    private lateinit var springDotsIndicator: SpringDotsIndicator
    private lateinit var countryCodeSpinner:Spinner
    private var countryCodes = listOf("+91","+92","+93")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        init()
    }
    private fun init(){
        dialog = Helpers.showDialog(this@LoginActivity,this@LoginActivity,"Verifying")
        viewPager = find(R.id.login_viewPager)
        springDotsIndicator = find(R.id.login_spring_dots_indicator)
        countryCodeSpinner = find(R.id.country_code_spinner)

        login_nxt_btn.setOnClickListener {
            val number = user_number_edittext.text.toString()
            if(number.isNotEmpty()){
                dialog.show()
               verify(number)
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
        countryCodeSpinner.background.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                (parent.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.white))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        countryCodeSpinner.onItemSelectedListener = listener
    }
     fun verificationCallbacks(number: String){
        mCallbacks = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                dialog.dismiss()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                dialog.dismiss()
            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(p0, p1)
                dialog.dismiss()
                Log.e("OnCodeSent","OTP : $p0")
                verificationId = p0
                    dialog.dismiss()
                launchActivity<OtpVerificationActivity>{
                        putExtra(verificationIdKey,verificationId)
                        putExtra(otpKey,p0)
                        putExtra(numberKey,number)
                    }
            }
        }
    }
    private fun verify(number:String){
        verificationCallbacks(number)
        val phoneNumber = "+91$number"
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }




}
