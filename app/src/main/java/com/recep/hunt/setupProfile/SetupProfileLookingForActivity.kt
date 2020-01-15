package com.recep.hunt.setupProfile


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.setupProfile.adapters.AddRemoveMode
import com.recep.hunt.setupProfile.adapters.LookingForListeners
import com.recep.hunt.setupProfile.adapters.SetupProfileLookingForAdapter
import com.recep.hunt.setupProfile.model.LookingForModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_setup_profile_looking_for.*

class SetupProfileLookingForActivity : BaseActivity(),
    LookingForListeners {

    var avatarFilePath = ""

    companion object{
        const val selectedInterestKey = "selectedInterestKey"
    }
    private var selectedLookingFor = ""
    private val selectedLookingForArray = ArrayList<String>()
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_looking_for)
        setScreenTitle(R.string.setup_profile)
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        getBackButton().setOnClickListener {
            finish()
        }
        init()
    }
    private fun init(){
        avatarFilePath = intent.getStringExtra(Constants.IMGURI)
        setup_looking_for_continue_btn.setOnClickListener {
            if(selectedLookingForArray.size != 0){
                SharedPrefrenceManager.setUserLookingFor(this@SetupProfileLookingForActivity,selectedLookingFor)
                launchActivity<SetupProfileInterestedInActivity>{
                    putExtra(Constants.IMGURI, avatarFilePath)
                    putStringArrayListExtra(selectedInterestKey,selectedLookingForArray)
                }
            }else{
                Helpers.showErrorSnackBar(this@SetupProfileLookingForActivity,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            }

        }

        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        rc_view.layoutManager = LinearLayoutManager(this)
        rc_view.adapter = adapter

        for(data in lookingForData){
            adapter.add(
                SetupProfileLookingForAdapter(
                    this@SetupProfileLookingForActivity,
                    data,
                    this
                )
            )
        }


    }

    override fun getSelectedLookingFor(lookingFor: String,state: AddRemoveMode?) {
        when(state){
            AddRemoveMode.Added ->  {
                selectedLookingForArray.add(lookingFor)
                selectedLookingFor = selectedLookingForArray.joinToString(",")
                Log.e(SetupProfileLookingForAdapter::class.java.simpleName,"selectedLookingFor : $selectedLookingFor")
                SharedPrefrenceManager.setUserLookingFor(this@SetupProfileLookingForActivity,selectedLookingFor)
                launchActivity<SetupProfileInterestedInActivity>{
                    putExtra(Constants.IMGURI, avatarFilePath)
                    putStringArrayListExtra(selectedInterestKey,selectedLookingForArray)
                }
            }
            else ->{
                selectedLookingForArray.remove(lookingFor)
                selectedLookingFor = selectedLookingForArray.joinToString(",")
                Log.e(SetupProfileLookingForAdapter::class.java.simpleName,"selectedLookingFor : $selectedLookingFor")
            }
        }

    }

    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(
                LookingForModel(
                    R.drawable.ic_heart,
                    R.drawable.ic_date_white,
                    "Date",
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.ic_buisness_icon,
                    R.drawable.ic_buisness_white,
                    "Business",
                    false,
                    null
                )
            )
            data.add(
                LookingForModel(
                    R.drawable.friendship_icon,
                    R.drawable.friendship_white,
                    "Friendship",
                    false,
                    null
                )
            )
        }
        return data
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
