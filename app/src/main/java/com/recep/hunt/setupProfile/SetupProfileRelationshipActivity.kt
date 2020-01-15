package com.recep.hunt.setupProfile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.setupProfile.adapters.AddRemoveMode
import com.recep.hunt.setupProfile.adapters.LookingForListeners
import com.recep.hunt.setupProfile.adapters.SetupProfileInterestedInAdapter
import com.recep.hunt.setupProfile.model.LookingForModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_reletionship.*
import org.jetbrains.anko.find

class SetupProfileRelationshipActivity : BaseActivity() , LookingForListeners {
    var avatarFilePath = ""

    private var selectedOption = ""
    private lateinit var reletionshipRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_reletionship)
        setScreenTitle(R.string.setup_profile)
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        getBackButton().setOnClickListener {
            finish()
        }
        init()
    }
    private fun init(){
        avatarFilePath = intent.getStringExtra(IMGURI)
        reletionshipRecyclerView = find(R.id.reletionship_recyclerView)

        setup_reletionship_continue_btn.setOnClickListener {
            if(selectedOption.isNotEmpty()){

                SharedPrefrenceManager.setUserGender(this@SetupProfileRelationshipActivity,selectedOption)
                launchActivity<SetupProfileLookingForActivity>{
                    putExtra(IMGURI, avatarFilePath)
                }
            }else{
                Helpers.showErrorSnackBar(this,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }

        }
        setupRecyclerView()

    }

    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        reletionshipRecyclerView.layoutManager = LinearLayoutManager(this)
        reletionshipRecyclerView.adapter =
            SetupProfileInterestedInAdapter(
                lookingForData,
                this@SetupProfileRelationshipActivity,
                this
            )

    }

    override fun getSelectedLookingFor(lookingFor: String, state: AddRemoveMode?) {
        selectedOption = lookingFor
        SharedPrefrenceManager.setUsrRelationship(this@SetupProfileRelationshipActivity,selectedOption)
        launchActivity<SetupProfileLookingForActivity>{
            putExtra(IMGURI, avatarFilePath)
        }
    }

    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
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
                    Constants.INRELATION,
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_others_gender,
                    R.drawable.ic_other_white,
                    Constants.MARRIED,
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_others_gender,
                    R.drawable.ic_other_white,
                    Constants.DIV0RCED,
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
