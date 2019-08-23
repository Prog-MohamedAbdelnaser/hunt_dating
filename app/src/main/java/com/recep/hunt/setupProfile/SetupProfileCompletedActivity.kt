package com.recep.hunt.setupProfile

import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.recep.hunt.R
import com.recep.hunt.location.TurnOnGPSActivity
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_completed.*

class SetupProfileCompletedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_completed)
        init()
    }

    private fun init() {
        setSupportActionBar(setupProfile_complete_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        lets_start_btn.setOnClickListener {
            launchActivity<TurnOnGPSActivity>()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
