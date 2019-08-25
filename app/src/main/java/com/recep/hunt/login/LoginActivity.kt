package com.recep.hunt.login

import android.app.Activity
import android.content.Context
import android.content.Intent
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

class LoginActivity : AppCompatActivity() {

    companion object{
        const val numberKey = "userPhoneNumberKey"
        const val otpKey = "otpKey"
        const val verificationIdKey = "verificationId"
        const val searchRequestCode = 1
    }
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog : KProgressHUD
    private lateinit var countryCodeTextView: TextView

    private var verificationId = ""
    private lateinit var viewPager : ViewPager
    private lateinit var springDotsIndicator: SpringDotsIndicator

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
        countryCodeTextView = find(R.id.country_code_txtView)

        login_nxt_btn.setOnClickListener {
            val number = user_number_edittext.text.toString()
            if(number.isNotEmpty()){
                dialog.show()
               verify(number,countryCodeTextView.text.toString())
            }else{
                Helpers.showErrorSnackBar(this@LoginActivity,"Enter number","")
            }

        }

        setupViewPager()

        countryCodeTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity,SelectCountryCodeActivity::class.java)
            startActivityForResult(intent,searchRequestCode)
        }

    }
    //Setting up view pager
    private fun setupViewPager(){
        val imagesArray = arrayListOf(R.drawable.on_board_bg_1,R.drawable.on_board_bg_1,R.drawable.on_board_bg_1)
        val adapter = OnBoardAdapter(this@LoginActivity,imagesArray)
        viewPager.adapter = adapter
        springDotsIndicator.setViewPager(viewPager)
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
    private fun verify(number:String,countryCode:String){
        verificationCallbacks(number)
        val phoneNumber = "$countryCode$number"
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null) {
                val name = data.getStringExtra(SelectCountryCodeActivity.countryNameKey)
                val code = data.getStringExtra(SelectCountryCodeActivity.countryCodeKey)
                countryCodeTextView.text = code
            }
        }
        super.onActivityResult(requestCode, resultCode, data)


    }




}
