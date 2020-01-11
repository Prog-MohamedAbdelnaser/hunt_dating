package com.recep.hunt.setupProfile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.CheckUserEmail
import com.recep.hunt.model.isEmailRegister.isEmailRegisterResponse
import com.recep.hunt.utilis.*
import kotlinx.android.synthetic.main.activity_email_profile_xml.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            when {
                emailAddress.isEmpty() -> {
                    Helpers.showErrorSnackBar(
                        this@SetupProfileEmailActivity,
                        resources.getString(R.string.complete_form),
                        resources.getString(R.string.complete_form)
                    )
                }
                !isEmailValid(emailAddress) -> {
                    Helpers.showErrorSnackBar(
                        this@SetupProfileEmailActivity,
                        resources.getString(R.string.complete_form),
                        resources.getString(R.string.invalid_email_address)
                    )
                }

                !checkIsUserRegister(emailAddress) -> {
                    Helpers.showErrorSnackBar(
                        this@SetupProfileEmailActivity,
                        resources.getString(R.string.complete_form),
                        resources.getString(R.string.already_registered_email)
                    )
                }

                else -> {
                    SharedPrefrenceManager.setUserEmail(
                        this@SetupProfileEmailActivity,
                        emailAddress
                    )
                    launchActivity<SetupProfileDobActivity>()
                }
            }
        }


    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkIsUserRegister(email: String): Boolean {
        var isEmailAlreadyUse = false
        val checkIsEmailValid = CheckUserEmail(email)

        val call = ApiClient.getClient.checkIsEmailRegister(
            SharedPrefrenceManager.getUserToken(this),
            checkIsEmailValid
        )

        call.enqueue(object : Callback<isEmailRegisterResponse> {
            override fun onFailure(call: Call<isEmailRegisterResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<isEmailRegisterResponse>,
                response: Response<isEmailRegisterResponse>
            ) {
                response.body()?.let {
                    if (!it.status) {
                        Toast.makeText(
                            this@SetupProfileEmailActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    isEmailAlreadyUse = it.status

                }
            }


        })

        return isEmailAlreadyUse
    }
}