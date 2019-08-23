package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_interested_in.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.toast

class SetupProfileInterestedInActivity : AppCompatActivity() {


    private lateinit var maleImageView : ImageView
    private lateinit var feMaleImageView : ImageView
    private lateinit var otherImageView : ImageView
    private var selectedInterests = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_interested_in)
        init()
    }
    private fun init(){
        selectedInterests = intent.getStringArrayListExtra(SetupProfileLookingForActivity.selectedInterestKey)
        maleImageView = find(R.id.male_imageview)
        feMaleImageView = find(R.id.female_imageview)
        otherImageView = find(R.id.others_imageview)
        setSupportActionBar(setupProfile_interested_in_toolbar)
        setupGenderSelection()

        setup_interested_in_continue_btn.setOnClickListener {
            val count = selectedInterests.size
            if(count < 3){
                showAddExtraChoiceDialog()
            }else{
                toast("lets move to next screen")
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

        }

        female_imageview.setOnClickListener {
            maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            otherImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

            maleImageView.image = resources.getDrawable(R.drawable.ic_man)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female_white)
            otherImageView.image = resources.getDrawable(R.drawable.ic_others_gender)
        }

        otherImageView.setOnClickListener {
            maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            otherImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)

            maleImageView.image = resources.getDrawable(R.drawable.ic_man)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
            otherImageView.image = resources.getDrawable(R.drawable.ic_other_white)
        }
    }

    private fun showAddExtraChoiceDialog(){
        val yesButton : Button
        val noButton : Button
        val ll =  LayoutInflater.from(this).inflate(R.layout.add_extra_choice_dialog, null)
        val dialog = Dialog(this@SetupProfileInterestedInActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        yesButton = dialog.find(R.id.add_extra_choice_yesButton)
        noButton = dialog.find(R.id.add_extra_choice_noButton)
        yesButton.setOnClickListener {
            finish()
        }
        noButton.setOnClickListener {
        launchActivity<SetupProfileReferralCodeActivity> {  }
        }
        dialog.show()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
