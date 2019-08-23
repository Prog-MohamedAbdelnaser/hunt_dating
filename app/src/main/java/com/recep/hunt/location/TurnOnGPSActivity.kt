package com.recep.hunt.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_turn_on_gps.*
import org.jetbrains.anko.toast

class TurnOnGPSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_on_gps)
        init()
    }

    private fun init() {
        setSupportActionBar(turn_on_gps_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        turn_on_gps_toolbar.title=""
        turn_on_gps_toolbar.subtitle=""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hunt_main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.profile_item -> toast("profile")
                R.id.notify_item -> toast("notify")
                R.id.ghost_item -> toast("hunt")
                R.id.settings_item -> toast("settings")

                else -> finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
