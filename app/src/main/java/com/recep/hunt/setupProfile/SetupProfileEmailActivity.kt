package com.recep.hunt.setupProfile

import android.os.Bundle
import android.util.Log
import com.recep.hunt.R
import com.recep.hunt.utilis.*
import kotlinx.android.synthetic.main.activity_email_profile_xml.*

class SetupProfileEmailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_profile_xml)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }

        init()
    }


    private fun init() {
        email_address_next_btn.setOnClickListener {
            val emailAddress = email_address_editText.text.toString()
            this.hideKeyboard()
            if(emailAddress.isNotEmpty() && isEmailValid(emailAddress)) {
                SharedPrefrenceManager.setUserEmail(this@SetupProfileEmailActivity,emailAddress)
                launchActivity<SetupProfileDobActivity>()
            }else{
                Helpers.showErrorSnackBar(this@SetupProfileEmailActivity,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}