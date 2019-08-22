package com.recep.hunt.setupProfile


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.recep.hunt.R
import com.recep.hunt.adapters.SetupProfileLookingForAdapter
import com.recep.hunt.models.LookingForModel
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_setup_profile_looking_for.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

class SetupProfileLookingForActivity : AppCompatActivity() {

    companion object{
        const val selectedInterestKey = "selectedInterestKey"
    }
    private lateinit var datesImageView : ImageView
    private lateinit var buisnessImageView : ImageView
    private lateinit var friendShipImageView : ImageView
    private var isDateSelected = true
    private var isBusinessSelected = true
    private var isFriendShipSelected = true
    private var selectedChoices = ArrayList<String>()
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_looking_for)
        init()
    }
    private fun init(){
        setSupportActionBar(setupProfile_lookingFor_toolbar)
        setup_looking_for_continue_btn.setOnClickListener {

            launchActivity<SetupProfileInterestedInActivity>{
                putStringArrayListExtra(selectedInterestKey,selectedChoices)
            }
        }

        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        rc_view.layoutManager = LinearLayoutManager(this)
        rc_view.adapter = adapter

        for(data in lookingForData){
            adapter.add(SetupProfileLookingForAdapter(this@SetupProfileLookingForActivity,data))
        }

    }
    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(LookingForModel(R.drawable.ic_heart,R.drawable.ic_date_white,"Date",false))
            data.add(LookingForModel(R.drawable.ic_buisness_icon,R.drawable.ic_buisness_white,"Business",false))
            data.add(LookingForModel(R.drawable.ic_friendship_icon,R.drawable.ic_friendship_white,"Friendship",false))


        }
        return data
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
