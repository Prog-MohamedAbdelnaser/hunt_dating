package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_info_you_provide.*
import kotlinx.android.synthetic.main.activity_setup_profile_referral_code.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class SetupProfileReferralCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_referral_code)

        init()
    }



    private fun init() {
        tvSkip.setOnClickListener {
            launchActivity<SetupProfileCompletedActivity> {  }
        }
        ivBack.onClick {
            onBackPressed()
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        check_code_btn.setOnClickListener {

            if(edtReferelCode.text.equals("huntwelcome"))
            {
                launchActivity<SetupProfileCompletedActivity> {  }
            }
            else{
                showTryAgainAlert()
            }


        }
    }

    private fun showTryAgainAlert(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.try_again_dailog_layout, null)
        val dialog = Dialog(this@SetupProfileReferralCodeActivity)

        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val textView = dialog.find<TextView>(R.id.we_will_send_you_otp_tv)


        textView.text =  "your given referral code is not correct,\n please enter a valid code."

        val sendOtpAgainBtn = dialog.find<Button>(R.id.try_again_btn)

        sendOtpAgainBtn.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()

    }


}
