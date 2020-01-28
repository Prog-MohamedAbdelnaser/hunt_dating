package com.recep.hunt.setupProfile

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.setupProfile.adapters.AddRemoveMode
import com.recep.hunt.setupProfile.adapters.LookingForListeners
import com.recep.hunt.setupProfile.model.LookingForModel
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_reletionship.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor

class SetupProfileRelationshipActivity : AppCompatActivity() {
    private lateinit var relatiionShipData: java.util.ArrayList<LookingForModel>
    var avatarFilePath = ""

    private var selectedOption = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_reletionship)
        init()
    }

    private fun init() {
        avatarFilePath = intent.getStringExtra(IMGURI)

        image_back_button.setOnClickListener { finish() }
        text_screen_title.text = resources.getString(R.string.setup_profile)
        base_cancel_btn.setOnClickListener { Helpers.segueToSocialLoginScreen(this) }

        setup_reletionship_continue_btn.setOnClickListener {
            if (selectedOption != -1) {
                goToNextStep()
            } else {
                Helpers.showErrorSnackBar(
                    this,
                    resources.getString(R.string.complete_form),
                    resources.getString(R.string.you_have_complete_form)
                )
            }
        }
        setupRelationShipData()

    }

    private fun setupRelationShipData() {
        relatiionShipData = dummyImageData()
        restStatus()
        imageView_single.setOnClickListener {
            if (selectedOption != -1) {
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
                if (selectedOption == 1) {
                    relatiionShipData[0].isSelected = true;
                }
            } else {
                selectedOption = 0
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
            }
            selectedOption = if (relatiionShipData[0].isSelected) {
                0
            } else -1;
            restStatus()
            goToNextStep()
        }
        imageView_married.setOnClickListener {
            if (selectedOption != -1) {
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
                if (selectedOption == 0) {
                    relatiionShipData[1].isSelected = true;
                }
            } else {
                selectedOption = 1;
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
            }
            selectedOption = if (relatiionShipData[1].isSelected) {
                1
            } else -1;
            restStatus()
            goToNextStep()
        }

    }

    /** listener part **/
    private fun restStatus() {
        if (relatiionShipData[0].isSelected) {
            selectedOption = 0
            //todo fix icon
            imageView_single.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            imageView_single.image = resources.getDrawable(relatiionShipData[0].selectedImage)
            title_single.textColor = resources.getColor(R.color.app_text_black)
        } else {
            imageView_single.background =
                resources.getDrawable(R.drawable.unselected_circular_btn)
            imageView_single.image =
                resources.getDrawable(relatiionShipData[0].unSelectedImage)
            title_single.textColor = resources.getColor(R.color.app_light_text_color)
        }
        if (relatiionShipData[1].isSelected) {
            selectedOption = 1
            imageView_married.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            imageView_married.image = resources.getDrawable(relatiionShipData[1].selectedImage)
            title_married.textColor = resources.getColor(R.color.app_text_black)
        } else {
            imageView_married.background =
                resources.getDrawable(R.drawable.unselected_circular_btn)
            imageView_married.image =
                resources.getDrawable(relatiionShipData[1].unSelectedImage)
            title_married.textColor = resources.getColor(R.color.app_light_text_color)

        }
    }

    private fun dummyImageData(): ArrayList<LookingForModel> {
        val data = ArrayList<LookingForModel>()
        if (data.size == 0) {
            data.add(
                LookingForModel(
                    R.drawable.ic_man,
                    R.drawable.ic_man_white,
                    Constants.SINGLE,
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_female,
                    R.drawable.ic_female_white,
                    Constants.MARRIED,
                    false,
                    null
                )
            )
        }
        return data
    }

    private fun goToNextStep(){
        SharedPrefrenceManager.setRelationshipStatus(
            this@SetupProfileRelationshipActivity,
            relatiionShipData[selectedOption].label
        )
        launchActivity<SetupProfileLookingForActivity> {
            putExtra(IMGURI, avatarFilePath)
        }
    }
}
