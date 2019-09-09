package com.recep.hunt.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.hbb20.CountryCodePicker
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import kotlinx.android.synthetic.main.activity_change_my_phone_number.*
import kotlinx.android.synthetic.main.number_changed_success_layout.*
import org.jetbrains.anko.find

class ChangeMyPhoneNumberActivity : BaseActivity() {

    private lateinit var countryCodePicker: CountryCodePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_my_phone_number)
        setScreenTitle(R.string.change_my_number)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()
    }
    private fun init(){

        countryCodePicker = find(R.id.ccp2)

        change_number_btn.setOnClickListener {
            showSuccessDialog()
        }

    }

    private fun showSuccessDialog(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.number_changed_success_layout, null)
        val dialog = Dialog(this@ChangeMyPhoneNumberActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.lottieAnimationView2.playAnimation()
        dialog.ok_btn.setOnClickListener {
            dialog.dismiss()

            finish()
        }
        dialog.show()

    }
}
