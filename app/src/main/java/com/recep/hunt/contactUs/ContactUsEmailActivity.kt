package com.recep.hunt.contactUs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_contact_us_email.*

class ContactUsEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us_email)
        init()
    }
    private fun init(){
        contact_us_email_close_btn.setOnClickListener { finish() }
    }
}
