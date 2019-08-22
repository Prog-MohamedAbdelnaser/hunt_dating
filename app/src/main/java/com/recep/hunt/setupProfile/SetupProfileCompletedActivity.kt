package com.recep.hunt.setupProfile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_setup_profile_completed.*

class SetupProfileCompletedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_completed)
        init()
    }

    private fun init() {
        setSupportActionBar(setupProfile_complete_toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
