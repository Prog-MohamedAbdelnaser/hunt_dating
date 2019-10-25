package com.recep.hunt.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.recep.hunt.R
import kotlinx.android.synthetic.main.activity_info_you_provide.*
import org.jetbrains.anko.toast

class InfoYouProvideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_you_provide)
        init()
    }

    private fun init(){
        setSupportActionBar(info_you_provide_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_you_provide_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.info_you_provide_clear_item -> toast("Clear tapped")
                else -> finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
