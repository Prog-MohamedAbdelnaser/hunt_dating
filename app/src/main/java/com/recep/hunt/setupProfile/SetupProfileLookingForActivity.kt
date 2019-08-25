package com.recep.hunt.setupProfile


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.recep.hunt.R
import com.recep.hunt.adapters.AddRemoveMode
import com.recep.hunt.adapters.LookingForListeners
import com.recep.hunt.adapters.SetupProfileLookingForAdapter
import com.recep.hunt.models.LookingForModel
import com.recep.hunt.models.LookingForSelectionModel
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_setup_profile_looking_for.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

class SetupProfileLookingForActivity : AppCompatActivity(),LookingForListeners {

    override fun getSelectedLookingFor(lookingFor: String,state:AddRemoveMode?) {
        when(state){
            AddRemoveMode.Added ->  {
                selectedLookingForArray.add(lookingFor)
                selectedLookingFor = selectedLookingForArray.joinToString(",")
                Log.e(SetupProfileLookingForAdapter::class.java.simpleName,"selectedLookingFor : $selectedLookingFor")
            }
            else ->{
                selectedLookingForArray.remove(lookingFor)
                selectedLookingFor = selectedLookingForArray.joinToString(",")
                Log.e(SetupProfileLookingForAdapter::class.java.simpleName,"selectedLookingFor : $selectedLookingFor")
            }
        }

    }

    companion object{
        const val selectedInterestKey = "selectedInterestKey"
    }
    private var selectedLookingFor = ""
    private val selectedLookingForArray = ArrayList<String>()
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_looking_for)
        init()
    }
    private fun init(){
        setSupportActionBar(setupProfile_lookingFor_toolbar)
        setup_looking_for_continue_btn.setOnClickListener {
            if(selectedLookingForArray.size != 0){
                SharedPrefrenceManager.setUserLookingFor(this@SetupProfileLookingForActivity,selectedLookingFor)
                launchActivity<SetupProfileInterestedInActivity>{
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
            adapter.add(SetupProfileLookingForAdapter(this@SetupProfileLookingForActivity,data,this))
        }


    }
    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(LookingForModel(R.drawable.ic_heart,R.drawable.ic_date_white,"Date",false,null))
            data.add(LookingForModel(R.drawable.ic_buisness_icon,R.drawable.ic_buisness_white,"Business",false,null))
            data.add(LookingForModel(R.drawable.ic_friendship_icon,R.drawable.ic_friendship_white,"Friendship",false,null))


        }
        return data
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
