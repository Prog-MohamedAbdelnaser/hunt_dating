package com.recep.hunt.setupProfile


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_looking_for)
        init()
    }
    private fun init(){
        datesImageView = find(R.id.dates_imageView)
        buisnessImageView = find(R.id.buisness_ImageView)
        friendShipImageView = find(R.id.friendship_imageView)
        setSupportActionBar(setupProfile_lookingFor_toolbar)
        setupLookingForSelection()
        setup_looking_for_continue_btn.setOnClickListener {

            launchActivity<SetupProfileInterestedInActivity>{
                putStringArrayListExtra(selectedInterestKey,selectedChoices)
            }
        }
    }

    private fun setupLookingForSelection(){
        datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
        buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
        friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)

        datesImageView.setOnClickListener {

            if(isDateSelected){
                isDateSelected = false
                datesImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_date_white)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)
                selectedChoices.add(resources.getString(R.string.dates))
            }else{
                isDateSelected = true
                datesImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)
                selectedChoices.remove(resources.getString(R.string.dates))
            }


        }

        buisnessImageView.setOnClickListener {
            if(isBusinessSelected){
                isBusinessSelected = false

                datesImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_white)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)
                selectedChoices.add(resources.getString(R.string.buisness))
            }else{
                isBusinessSelected = true
                datesImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)
                selectedChoices.remove(resources.getString(R.string.buisness))
            }

        }

        friendShipImageView.setOnClickListener {
            if(isFriendShipSelected){
                isFriendShipSelected = false
                datesImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_white)
            }else{
                isFriendShipSelected = true
                datesImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                buisnessImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                friendShipImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)

                datesImageView.image = resources.getDrawable(R.drawable.ic_heart)
                buisnessImageView.image = resources.getDrawable(R.drawable.ic_buisness_icon)
                friendShipImageView.image = resources.getDrawable(R.drawable.ic_friendship_icon)
            }

        }
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
