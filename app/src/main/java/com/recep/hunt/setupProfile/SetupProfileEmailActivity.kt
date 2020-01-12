package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kaopiz.kprogresshud.KProgressHUD
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.CheckUserEmail
import com.recep.hunt.model.isEmailRegister.isEmailRegisterResponse
import com.recep.hunt.utilis.*
import kotlinx.android.synthetic.main.activity_email_profile_xml.*
import org.jetbrains.anko.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetupProfileEmailActivity : BaseActivity(), View.OnClickListener {


    val emailCheckStatus: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var dialog: KProgressHUD
    private var emailAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_profile_xml)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }

        dialog = Helpers.showDialog(this, this, "Processing")





        setObservers()
        init()
    }


    private fun init() {
        email_address_next_btn.setOnClickListener(this)
    }

    private fun setObservers() {

        emailCheckStatus.observe(this, Observer {
            //process
            when (it) {
                false -> {
                    Helpers.showErrorSnackBar(
                        this@SetupProfileEmailActivity,
                        resources.getString(R.string.complete_form),
                        resources.getString(R.string.already_registered_email)
                    )
                }
                true -> {
                    SharedPrefrenceManager.setUserEmail(
                        this@SetupProfileEmailActivity,
                        emailAddress
                    )
                    launchActivity<SetupProfileDobActivity>()
                    Logger.d("SetupProfileEmail: ${SharedPrefrenceManager.getUserEmail(this)}")
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.email_address_next_btn -> {

                    emailAddress = email_address_editText.text.toString()
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

                        else -> {
                            checkIsUserRegister(emailAddress)
                            dialog.show()


                        }

                    }

                }
            }
        }
    }


    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    private fun checkIsUserRegister(email: String) {
        val checkIsEmailValid = CheckUserEmail(email)

        val call = ApiClient.getClient.checkIsEmailRegister(
            SharedPrefrenceManager.getUserToken(this),
            checkIsEmailValid
        )

        call.enqueue(object : Callback<isEmailRegisterResponse> {
            override fun onFailure(call: Call<isEmailRegisterResponse>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(
                call: Call<isEmailRegisterResponse>,
                response: Response<isEmailRegisterResponse>
            ) {
                dialog.dismiss()
                response.body()?.let {
                    emailCheckStatus.postValue(it.status)

                }
            }

        })


    }


}