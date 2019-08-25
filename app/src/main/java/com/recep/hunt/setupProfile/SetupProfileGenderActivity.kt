package com.recep.hunt.setupProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_gender.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

class SetupProfileGenderActivity : AppCompatActivity() {

    private lateinit var maleImageView : ImageView
    private lateinit var feMaleImageView : ImageView
    private lateinit var otherImageView : ImageView
    private var selectedGender = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_gender)
        init()
    }
    private fun init(){
        maleImageView = find(R.id.male_imageview)
        feMaleImageView = find(R.id.female_imageview)
        otherImageView = find(R.id.others_imageview)
        setSupportActionBar(setupProfile_gender_toolbar)
        setupGenderSelection()

        setup_gender_continue_btn.setOnClickListener {
            if(selectedGender.isNotEmpty()){
                SharedPrefrenceManager.setUserGender(this@SetupProfileGenderActivity,selectedGender)
                launchActivity<SetupProfileLookingForActivity>()
            }else{
                Helpers.showErrorSnackBar(this,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }

        }

    }
    private fun setupGenderSelection(){

        maleImageView.image = resources.getDrawable(R.drawable.ic_man)
        feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
        otherImageView.image = resources.getDrawable(R.drawable.ic_others_gender)

        maleImageView.setOnClickListener {
            maleImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            otherImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

            maleImageView.image = resources.getDrawable(R.drawable.ic_man_white)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
            otherImageView.image = resources.getDrawable(R.drawable.ic_others_gender)
            selectedGender = Constants.male
        }

        female_imageview.setOnClickListener {
            maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            otherImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

            maleImageView.image = resources.getDrawable(R.drawable.ic_man)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female_white)
            otherImageView.image = resources.getDrawable(R.drawable.ic_others_gender)

            selectedGender = Constants.female
        }

        otherImageView.setOnClickListener {
            maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            otherImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)

            maleImageView.image = resources.getDrawable(R.drawable.ic_man)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
            otherImageView.image = resources.getDrawable(R.drawable.ic_other_white)

            selectedGender = Constants.others
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
