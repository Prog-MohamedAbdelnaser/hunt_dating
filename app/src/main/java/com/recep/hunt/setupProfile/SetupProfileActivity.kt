package com.recep.hunt.setupProfile

import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor
import java.util.*

class SetupProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)
        init()
    }

    private fun init(){
        full_name_next_btn.setOnClickListener {
            val firstName = first_name_editText.text.toString()
            val lastName = last_name_editText.text.toString()
            if(firstName.isNotEmpty() && lastName.isNotEmpty()){
                SharedPrefrenceManager.setUserFirstName(this@SetupProfileActivity,firstName)
                SharedPrefrenceManager.setUserLastName(this@SetupProfileActivity,lastName)
                launchActivity<SetupProfileDobActivity>()
            }else{
                Helpers.showErrorSnackBar(this@SetupProfileActivity,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }

        }
        setSupportActionBar(setupProfile_toolbar)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}
