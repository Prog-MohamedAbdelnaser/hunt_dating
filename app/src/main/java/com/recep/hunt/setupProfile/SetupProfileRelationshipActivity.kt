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
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor

class SetupProfileRelationshipActivity : AppCompatActivity() {
    private lateinit var relatiionShipData: java.util.ArrayList<LookingForModel>
    var avatarFilePath = ""

    private var selectedOption = -1
    lateinit var mTextViewScreenTitle: TextView
    lateinit var cancelBtn: Button
    lateinit var mImageButtonBack: ImageButton
    lateinit var mImageViewSingle: ImageView
    lateinit var mImageViewMarried: ImageView
    lateinit var mTextVieSingle: TextView
    lateinit var mTextVieMarried: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_reletionship)
        init()
    }

    private fun init() {
        avatarFilePath = intent.getStringExtra(IMGURI)
        mTextViewScreenTitle = find(R.id.text_screen_title)
        mImageButtonBack = find(R.id.image_back_button)
        mImageButtonBack.setOnClickListener { finish() }
        cancelBtn = find(R.id.base_cancel_btn)
        mTextViewScreenTitle.text = resources.getString(R.string.setup_profile)
        cancelBtn.setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        setup_reletionship_continue_btn.setOnClickListener {
            if (selectedOption != -1) {

                SharedPrefrenceManager.setUserGender(
                    this@SetupProfileRelationshipActivity,
                    relatiionShipData[selectedOption].label
                )
                launchActivity<SetupProfileLookingForActivity> {
                    putExtra(IMGURI, avatarFilePath)
                }
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
        mImageViewSingle = find(R.id.imageView_single)
        mImageViewMarried = find(R.id.imageView_married)
        mTextVieSingle = find(R.id.title_single)
        mTextVieMarried = find(R.id.title_married)
        restStatus()
        mImageViewSingle.setOnClickListener {
            if (selectedOption != -1) {
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
                if (selectedOption == 1) {
                    relatiionShipData[0].isSelected = true;
                }
            } else {
                selectedOption = 0;
                relatiionShipData[selectedOption].isSelected =
                    !relatiionShipData[selectedOption].isSelected
            }
            selectedOption = if (relatiionShipData[0].isSelected) {
                0
            } else -1;
            restStatus()
        }
        mImageViewMarried.setOnClickListener {
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
        }

    }

    private fun restStatus() {
        if (relatiionShipData[0].isSelected) {
            selectedOption = 0;
            mImageViewSingle.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            mImageViewSingle.image = resources.getDrawable(relatiionShipData[0].selectedImage)
            mTextVieSingle.textColor = resources.getColor(R.color.app_text_black)
        } else {
            mImageViewSingle.background =
                resources.getDrawable(R.drawable.unselected_circular_btn)
            mImageViewSingle.image =
                resources.getDrawable(relatiionShipData[0].unSelectedImage)
            mTextVieSingle.textColor = resources.getColor(R.color.app_light_text_color)

        }
        if (relatiionShipData[1].isSelected) {
            selectedOption = 1
            mImageViewMarried.background = resources.getDrawable(R.drawable.selected_cirular_btn)
            mImageViewMarried.image = resources.getDrawable(relatiionShipData[1].selectedImage)
            mTextVieMarried.textColor = resources.getColor(R.color.app_text_black)
        } else {
            mImageViewMarried.background =
                resources.getDrawable(R.drawable.unselected_circular_btn)
            mImageViewMarried.image =
                resources.getDrawable(relatiionShipData[1].unSelectedImage)
            mTextVieMarried.textColor = resources.getColor(R.color.app_light_text_color)

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
}
