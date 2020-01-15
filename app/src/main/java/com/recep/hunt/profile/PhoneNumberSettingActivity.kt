package com.recep.hunt.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_phone_number_setting.*

class PhoneNumberSettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_setting)
        setScreenTitle(R.string.phone_no_setting)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }
    private fun init(){
        change_my_phone_no_btn.setOnClickListener {
            launchActivity<ChangeMyPhoneNumberActivity>()
        }
    }
}
