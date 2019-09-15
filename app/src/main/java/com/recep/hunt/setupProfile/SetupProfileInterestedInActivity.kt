package com.recep.hunt.setupProfile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.recep.hunt.R
import com.recep.hunt.setupProfile.adapters.AddRemoveMode
import com.recep.hunt.setupProfile.adapters.LookingForListeners
import com.recep.hunt.setupProfile.adapters.SetupProfileInterestedInAdapter
import com.recep.hunt.constants.Constants
import com.recep.hunt.setupProfile.model.LookingForModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_interested_in.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class SetupProfileInterestedInActivity : BaseActivity(),
    LookingForListeners {

    override fun getSelectedLookingFor(lookingFor: String, state: AddRemoveMode?) {
        selectedInterstedIn = lookingFor
        SharedPrefrenceManager.setUserInterestedIn(this@SetupProfileInterestedInActivity,lookingFor)
    }

    private var selectedInterstedIn = ""
    private var selectedInterests = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_interested_in)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener {
            finish()
        }
        init()
    }
    private fun init(){
        selectedInterests = intent.getStringArrayListExtra(SetupProfileLookingForActivity.selectedInterestKey)

        setupRecyclerView()

        setup_interested_in_continue_btn.setOnClickListener {
            if(selectedInterstedIn.isNotEmpty()){
                val count = selectedInterests.size
                if(count < 3){
                    showAddExtraChoiceDialog()
                }else{
//                    toast(selectedInterstedIn)
//                    launchActivity<TurnOnGPSActivity>()
                    launchActivity<TurnOnGPSUpdateAcitivity>()
                }
            }else{
                Helpers.showErrorSnackBar(this@SetupProfileInterestedInActivity,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }


        }

    }
    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        rc_view1.layoutManager = LinearLayoutManager(this)
        rc_view1.adapter = SetupProfileInterestedInAdapter(
            lookingForData,
            this@SetupProfileInterestedInActivity,
            this
        )

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
            dialog.dismiss()
            finish()

        }
        noButton.setOnClickListener {
            dialog.dismiss()
            launchActivity<TurnOnGPSActivity>()
        }
        dialog.show()

    }


    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(
                LookingForModel(
                    R.drawable.ic_man,
                    R.drawable.ic_man_white,
                    "Male",
                    false,
                    Constants.male
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_female,
                    R.drawable.ic_female_white,
                    "Female",
                    false,
                    Constants.female
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_others_gender,
                    R.drawable.ic_other_white,
                    "Both",
                    false,
                    Constants.both
                )
            )


        }
        return data
    }
}
