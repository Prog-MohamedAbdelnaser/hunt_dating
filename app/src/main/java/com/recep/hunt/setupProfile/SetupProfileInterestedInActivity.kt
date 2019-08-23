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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.adapters.SetupProfileInterestedInAdapter
import com.recep.hunt.adapters.SetupProfileLookingForAdapter
import com.recep.hunt.models.LookingForModel
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_setup_profile_interested_in.*
import kotlinx.android.synthetic.main.activity_setup_profile_looking_for.*
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

        setSupportActionBar(setupProfile_interested_in_toolbar)
        setupRecyclerView()

        setup_interested_in_continue_btn.setOnClickListener {
            val count = selectedInterests.size
            if(count < 3){
                showAddExtraChoiceDialog()
            }else{
                toast("lets move to next screen")
            }

        }

    }
    private fun setupRecyclerView(){
        val lookingForData= dummyImageData()
        rc_view1.layoutManager = LinearLayoutManager(this)
        rc_view1.adapter = SetupProfileInterestedInAdapter(lookingForData,this@SetupProfileInterestedInActivity)



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
    private fun dummyImageData():ArrayList<LookingForModel>{
        val data = ArrayList<LookingForModel>()
        if(data.size == 0){
            data.add(LookingForModel(R.drawable.ic_man,R.drawable.ic_man_white,"Male",false))
            data.add(LookingForModel(R.drawable.ic_female,R.drawable.ic_female_white,"Female",false))
            data.add(LookingForModel(R.drawable.ic_others_gender,R.drawable.ic_other_white,"Both",false))


        }
        return data
    }
}
