package com.recep.hunt.setupProfile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.setupProfile.adapters.AddRemoveMode
import com.recep.hunt.setupProfile.adapters.LookingForListeners
import com.recep.hunt.setupProfile.adapters.SetupProfileInterestedInAdapter
import com.recep.hunt.setupProfile.model.LookingForModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_gender.*
import org.jetbrains.anko.find

class SetupProfileGenderActivity : BaseActivity() , LookingForListeners {

    override fun getSelectedLookingFor(lookingFor: String, state: AddRemoveMode?) {
        selectedGender = lookingFor
    }

    private var selectedGender = ""
    private lateinit var genderRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_gender)
        setScreenTitle(R.string.setup_profile)
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        getBackButton().setOnClickListener {
            finish()
        }
        init()
    }
    private fun init(){
        genderRecyclerView = find(R.id.gender_recyclerView)

        setup_gender_continue_btn.setOnClickListener {
            if(selectedGender.isNotEmpty()){
                SharedPrefrenceManager.setUserGender(this@SetupProfileGenderActivity,selectedGender)
                launchActivity<SetupProfileLookingForActivity>()
            }else{
                Helpers.showErrorSnackBar(this,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }

        }
        setupRecyclerView()

    }

    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        genderRecyclerView.layoutManager = LinearLayoutManager(this)
        genderRecyclerView.adapter =
            SetupProfileInterestedInAdapter(
                lookingForData,
                this@SetupProfileGenderActivity,
                this
            )

    }
    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(
                LookingForModel(
                    R.drawable.ic_man,
                    R.drawable.ic_man_white,
                    Constants.MALE,
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_female,
                    R.drawable.ic_female_white,
                    Constants.FEMALE,
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_others_gender,
                    R.drawable.ic_other_white,
                    Constants.OTHERS,
                    false,
                    null
                )
            )
        }
        return data
    }
}
