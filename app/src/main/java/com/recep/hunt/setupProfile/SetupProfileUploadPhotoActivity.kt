package com.recep.hunt.setupProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_upload_photo.*


class SetupProfileUploadPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_upload_photo)
        init()
    }
    private fun init(){
        setSupportActionBar(setupProfile_code__toolbar)
        add_photo_layout.setOnClickListener {
            launchActivity<SetupProfileUploadPhotoStep2Activity>()
        }
        setup_profile_upload_pic_next_btn.setOnClickListener {
            launchActivity<SetupProfileGenderActivity>()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}
